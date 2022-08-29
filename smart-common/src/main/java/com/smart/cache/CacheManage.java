package com.smart.cache;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.smart.enums.TokenEnum;
import com.smart.model.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 缓存管理
 */
@Component
@Slf4j
public class CacheManage {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public List<Object> hashValues(String cacheKey) {
        return redisTemplate.boundHashOps(cacheKey).values();
    }

    public Boolean hasKey(String cacheKey) {
        return redisTemplate.hasKey(cacheKey);
    }

    /**
     * 系统用户缓存管理 ->初始化、增加|修改、单个详情、删除、列表
     */
    public void setSysUser(LoginUser loginUser) {
        if (loginUser == null) {
            log.error("系统用户缓存设置,对象为NULL!!!");
            return;
        }
        log.info("系统用户缓存设置,ID={},name={}--begin", loginUser.getUserId(), loginUser.getUserName());
        String infoKey = CacheConstant.CACHE_KEY_USER_INFO + TokenEnum.WEB.getCode() + ":" + loginUser.getUserId();
        redisTemplate.opsForValue().set(infoKey, JSON.toJSONString(loginUser));
        // 设置过期时间（与token一致）
        redisTemplate.expire(infoKey, TokenEnum.WEB.getTime(), TimeUnit.SECONDS);
        log.info("系统用户缓存设置,ID={},name={}--end", loginUser.getUserId(), loginUser.getUserName());
    }

    public LoginUser getSysUser4Id(String id) {
        if (StringUtils.isEmpty(id)) {
            log.error("系统用户ID={},为空!!", id);
            return null;
        }
        String infoKey = CacheConstant.CACHE_KEY_USER_INFO + TokenEnum.WEB.getCode() + ":" + id;
        Object object = redisTemplate.opsForValue().get(infoKey);
        if (object == null) {
            return null;
        }
        return JSON.parseObject(object.toString(), LoginUser.class);
    }

    public void delSysUser(String id) {
        String infoKey = CacheConstant.CACHE_KEY_USER_INFO + TokenEnum.WEB.getCode() + ":" + id;
        redisTemplate.delete(infoKey);
    }

    public void renewSysUser(String id) {
        //刷新系统用户ID={}时间
        if (StringUtils.isEmpty(id)) {
            log.error("系统用户ID={},为空!!", id);
            return;
        }
        String infoKey = CacheConstant.CACHE_KEY_USER_INFO + TokenEnum.WEB.getCode() + ":" + id;
        log.info("刷新系统用户时间---------ID={}", id);
        redisTemplate.expire(infoKey, TokenEnum.WEB.getTime(), TimeUnit.SECONDS);
    }
//
//
//    /**
//     * 系统菜单缓存管理 ->初始化、增加|修改、单个详情、删除、列表
//     */
//    public void initSysMenu(List<SysMenuVO> list, boolean refresh) {
//        log.info("系统菜单缓存初始化---begin!!!");
//        if (refresh) {
//            redisTemplate.delete(CacheConstant.CACHE_KEY_SYS_MENU);
//        }
//        if (!CollectionUtils.isEmpty(list)) {
//            setSysMenu(list);
//        }
//        log.info("系统菜单缓存初始化---end!!!");
//    }
//
//    public void setSysMenu(List<SysMenuVO> list) {
//        if (CollectionUtils.isEmpty(list)) {
//            log.error("系统菜单缓存设置,对象为NULL!!!");
//            return;
//        }
//        log.info("系统菜单缓存设置--begin");
//        redisTemplate.opsForValue().set(CacheConstant.CACHE_KEY_SYS_MENU, JSON.toJSONString(list));
//        log.info("系统菜单缓存设置--end");
//    }
//
//    public List<SysMenuVO> getSysMenu() {
//        String menus = redisTemplate.opsForValue().get(CacheConstant.CACHE_KEY_SYS_MENU);
//        if (StringUtils.isEmpty(menus)) {
//            return null;
//        }
//        return JSON.parseArray(menus, SysMenuVO.class);
//    }
//
//    public void delSysMenu() {
//        redisTemplate.delete(CacheConstant.CACHE_KEY_SYS_MENU);
//    }

}
