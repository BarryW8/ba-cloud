package com.ba.cache;

import com.alibaba.fastjson.JSON;
import com.ba.base.UserInfo;
import com.ba.model.system.SysMenu;
import com.ba.model.system.SysRoleMenu;
import com.ba.token.TokenEnum;
import com.ba.util.RedisCache;
import com.ba.vo.SysMenuVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 缓存管理
 */
@Component
@Slf4j
public class CacheManage {

    @Autowired
    private RedisCache redisCache;

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
    public void setSysUser(UserInfo userInfo) {
        if (userInfo == null) {
            log.error("系统用户缓存设置,对象为NULL!!!");
            return;
        }
        log.info("系统用户缓存设置,ID={},name={}--begin", userInfo.getId(), userInfo.getUserName());
        String infoKey = CacheConstant.CACHE_KEY_USER_INFO + TokenEnum.WEB.getCode() + ":" + userInfo.getId();
        redisTemplate.opsForValue().set(infoKey, JSON.toJSONString(userInfo));
        // 设置过期时间（与token一致）
        redisTemplate.expire(infoKey, TokenEnum.WEB.getTime(), TimeUnit.SECONDS);
        log.info("系统用户缓存设置,ID={},name={}--end", userInfo.getId(), userInfo.getUserName());
    }

    public UserInfo getSysUser4Id(String id) {
        if (StringUtils.isEmpty(id)) {
            log.error("系统用户ID={},为空!!", id);
            return null;
        }
        String infoKey = CacheConstant.CACHE_KEY_USER_INFO + TokenEnum.WEB.getCode() + ":" + id;
        Object object = redisTemplate.opsForValue().get(infoKey);
        if (object == null) {
            return null;
        }
        return JSON.parseObject(object.toString(), UserInfo.class);
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

    /**
     * 系统用户缓存管理 ->初始化、增加|修改、单个详情、删除、列表
     */
    public void setSysRoleMenu(Long roleId, String menuCode, List<String> permission) {
        if (roleId == null) {
            log.error("系统用户缓存设置,对象为NULL!!!");
            return;
        }
        log.info("系统用户缓存设置,ID={},name={}--begin", roleId, menuCode);
        String infoKey = String.format(CacheConstant.CACHE_STR_KEY_ROLE_PERMISSION, roleId);
        redisCache.setHashValue(infoKey, menuCode, permission.toString());
        log.info("系统用户缓存设置,ID={},name={}--end", roleId, menuCode);
    }

    public List<String> getSysRoleMenu(String roleId, String menuCode) {
        if (StringUtils.isEmpty(roleId)) {
            log.error("系统用户ID={},为空!!", roleId);
            return null;
        }
        String infoKey = String.format(CacheConstant.CACHE_STR_KEY_ROLE_PERMISSION, roleId);
        List<String> hashValue4List = redisCache.getHashValue4List(infoKey, menuCode, String.class);
        return hashValue4List;
    }

    public void delSysRoleMenu(String roleId, String menuCode) {
        String infoKey = String.format(CacheConstant.CACHE_STR_KEY_ROLE_PERMISSION, roleId);
        redisCache.deleteHashValue(infoKey, menuCode);
    }

    public void delSysRoleMenuAll(String roleId) {
        String infoKey = String.format(CacheConstant.CACHE_STR_KEY_ROLE_PERMISSION, roleId);
        redisCache.deleteHashValueAll(infoKey);
    }


    /**
     * 系统菜单缓存管理 ->初始化、增加|修改、单个详情、删除、列表
     */
    public void initSysMenu(List<SysMenu> list, boolean refresh) {
        log.info("系统菜单缓存初始化---begin!!!");
        if (refresh) {
            redisTemplate.delete(CacheConstant.CACHE_KEY_SYS_MENU);
        }
        if (!CollectionUtils.isEmpty(list)) {
            setSysMenu(list);
        }
        log.info("系统菜单缓存初始化---end!!!");
    }

    public void setSysMenu(List<SysMenu> list) {
        if (CollectionUtils.isEmpty(list)) {
            log.error("系统菜单缓存设置,对象为NULL!!!");
            return;
        }
        log.info("系统菜单缓存设置--begin");
        redisTemplate.opsForValue().set(CacheConstant.CACHE_KEY_SYS_MENU, JSON.toJSONString(list));
        log.info("系统菜单缓存设置--end");
    }

    public List<SysMenu> getSysMenu() {
        String menus = redisTemplate.opsForValue().get(CacheConstant.CACHE_KEY_SYS_MENU);
        if (StringUtils.isEmpty(menus)) {
            return null;
        }
        return JSON.parseArray(menus, SysMenu.class);
    }

    public void delSysMenu() {
        redisTemplate.delete(CacheConstant.CACHE_KEY_SYS_MENU);
    }

}
