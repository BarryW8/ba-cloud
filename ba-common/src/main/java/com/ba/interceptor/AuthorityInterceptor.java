package com.ba.interceptor;

import com.ba.base.Permission;
import com.ba.base.UserContext;
import com.ba.base.UserInfo;
import com.ba.cache.CacheConstant;
import com.ba.enums.PermissionEnum;
import com.ba.response.ResEnum;
import com.ba.response.ResData;
import com.ba.util.CommonUtils;
import com.ba.util.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 权限拦截器
 */
@Component
@Slf4j
public class AuthorityInterceptor implements HandlerInterceptor {

    @Resource
    private RedisCache redisCache;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        boolean hasPermission = false;
        if (handler instanceof HandlerMethod) {
            HandlerMethod hm = (HandlerMethod) handler;

            Class<?> clazz = hm.getBeanType();
            Method m = hm.getMethod();
            try {
                if (clazz != null && m != null) {
                    boolean isClzAnnotation = clazz.isAnnotationPresent(Permission.class);
                    boolean isMethondAnnotation = m.isAnnotationPresent(Permission.class);
                    Permission permission = null;
                    // 如果方法和类声明中同时存在这个注解，那么方法中的会覆盖类中的设定。
                    if (isMethondAnnotation) {
                        permission = m.getAnnotation(Permission.class);
                    } else if (isClzAnnotation) {
                        permission = clazz.getAnnotation(Permission.class);
                    }
                    if (permission == null) {
                        return true;
                    }
                    UserInfo userInfo = UserContext.getUserInfo();
                    // 如果是超管，直接放行
                    if (userInfo.isSuperManager()) {
                        return true;
                    }
                    // 判断用户是否授权角色
                    if (userInfo.getRoleId() == null) {
                        CommonUtils.sendJsonMessage(response, ResData.result(ResEnum.SYSTEM_NO_PERMISSION));
                        log.error("未授权角色");
                        return false;
                    }
                    // 判断角色是否具有权限
                    String idKey = String.format(CacheConstant.CACHE_STR_KEY_ROLE_PERMISSION, userInfo.getRoleId());
                    List<String> rolePermsList = redisCache.getHashValue4List(idKey, permission.menuFlag(),String.class);
                    if (CollectionUtils.isEmpty(rolePermsList)) {
                        CommonUtils.sendJsonMessage(response, ResData.result(ResEnum.SYSTEM_NO_PERMISSION));
                        log.error("角色权限不足");
                        return false;
                    }
                    // 判断接口是否设置权限
                    List<PermissionEnum> permsList = Arrays.asList(permission.perms());
                    if (CollectionUtils.isEmpty(permsList)) {
                        CommonUtils.sendJsonMessage(response, ResData.result(ResEnum.SYSTEM_NO_PERMISSION));
                        log.error("接口未设置权限");
                        return false;
                    }
                    // 判断接口权限集合与角色权限集合是否存在交集
                    List<String> methodPermsList = permsList.stream().map(PermissionEnum::getCode).collect(Collectors.toList());
                    Collection<String> intersection = CollectionUtils.intersection(methodPermsList, rolePermsList);
                    if (CollectionUtils.isNotEmpty(intersection)) {
                        hasPermission = true;
                    }
                    if (!hasPermission) {
                        CommonUtils.sendJsonMessage(response, ResData.result(ResEnum.SYSTEM_NO_PERMISSION));
                        log.error("权限不足");
                        return false;
                    }
                }
            } catch (Exception e) {
                hasPermission = false;
            }
        }
        return hasPermission;
    }
}
