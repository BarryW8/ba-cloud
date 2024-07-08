package com.ba.service.impl;

import com.ba.base.PageView;
import com.ba.base.SimpleModel;
import com.ba.base.UserContext;
import com.ba.base.UserInfo;
import com.ba.cache.CacheConstant;
import com.ba.cache.CacheManage;
import com.ba.enums.ResEnum;
import com.ba.exception.ServiceException;
import com.ba.mapper.SysRoleMapper;
import com.ba.mapper.SysUserMapper;
import com.ba.mapper.SysUserRoleMapper;
import com.ba.model.system.SysRole;
import com.ba.model.system.SysUser;
import com.ba.model.system.SysUserRole;
import com.ba.service.SysUserService;
import com.ba.enums.TokenEnum;
import com.ba.token.TokenManage;
import com.ba.uid.impl.CachedUidGenerator;
import com.ba.util.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SysUserServiceImpl implements SysUserService {

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private SysRoleMapper sysRoleMapper;

    @Resource
    private SysUserRoleMapper sysUserRoleMapper;

    @Resource
    private CacheManage cacheManage;

    @Resource
    private TokenManage tokenManage;

    @Resource
    private RedisCache redisCache;

    @Resource
    private CachedUidGenerator uidGenerator;

    @Transactional
    @Override
    public int saveUserRole(SysUserRole model) {
        Long currentUserId = UserContext.getUserId();
        String currentDateStr = CommonUtils.getCurrentDate();
        Long roleId = model.getRoleId();
        Long userId = model.getUserId();
        // 1. 删除旧数据
        sysUserRoleMapper.deleteByUserId(userId);
        // 判断是否需要新增，同时防止空删除保存失败
        if (roleId == null) {
            return 1;
        }
        // 2. 保存新数据
        model.setId(uidGenerator.getUID());
        model.setCreateBy(currentUserId);
        model.setCreateTime(currentDateStr);
        return sysUserRoleMapper.insert(model);
    }

    @Override
    public SysRole findUserRoleByAppType(Long userId) {
        // 查询用户绑定的角色
        List<SysUserRole> userRoles = sysUserRoleMapper.findListBySQL(" and user_id = " + userId);
        if (CollectionUtils.isNotEmpty(userRoles)) {
            List<Long> roleIds = userRoles.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
            StringBuilder sql = new StringBuilder();
            sql.append(" and app_type = ").append(UserContext.getAppType().getCode());
            sql.append(" and id in (").append(StringUtils.join(roleIds, ",")).append(")");
            List<SysRole> roles = sysRoleMapper.findListBySQL(sql.toString());
            if (CollectionUtils.isNotEmpty(roles)) {
                return roles.get(0);
            }
        }
        return null;
    }

    @Transactional
    @Override
    public int add(SysUser sysUser) {
        return sysUserMapper.insert(sysUser);
    }

    @Transactional
    @Override
    public int edit(SysUser sysUser) {
        return sysUserMapper.update(sysUser);
    }

    @Override
    public SysUser findById(Long modelId) {
        return sysUserMapper.findById(modelId);
    }

    @Transactional
    @Override
    public int deleteBySm(SimpleModel simpleModel) {
        return sysUserMapper.deleteBySm(simpleModel);
    }

    @Override
    public List<SysUser> findList(Map<String, Object> map) {
        return sysUserMapper.findList(map);
    }

    @Override
    public List<SysUser> findListBySQL(String condition) {
        return sysUserMapper.findListBySQL(condition);
    }

    @Override
    public PageView<SysUser> findPage(Map<String, Object> map) {
        int count = sysUserMapper.count(map);
        PageView<SysUser> pageModel = new PageView<>();
        if (count > 0) {
            List<SysUser> list = sysUserMapper.findPage(map);
            pageModel.setData(list);
            pageModel.setTotal(count);
        } else {
            List<SysUser> list = new ArrayList<>();
            pageModel.setData(list);
            pageModel.setTotal(0);
        }
        return pageModel;
    }

    @Override
    public List<SysUser> findListHasPwd(String sql) {
        return sysUserMapper.findListHasPwd(sql);
    }

//    @Override
//    public UserInfo setUserCache(List<Long> userIds, int type) {
//        UserInfo loginUser = null;
//        if (type == 0) {
//            // 直接set
//            for (Long userId : userIds) {
//                loginUser = this.userCache(userId);
//            }
//        } else {
//            // 当角色信息修改时，刷新用户信息缓存，只刷新已有缓存中绑定该角色的用户信息
//            for (Long userId : userIds) {
//                UserInfo info = cacheManage.getSysUser4Id(userId.toString());
//                if (info != null) {
//                    loginUser = this.userCache(userId);
//                }
//            }
//        }
//        return loginUser;
//    }

    /**
     * 刷新用户缓存
     */
    @Override
    public UserInfo setUserCache(Long userId) {
        // 1. 查询用户基本信息
        SysUser user = sysUserMapper.findById(userId);
        if (Objects.isNull(user)) {
            this.delUserCache(userId);
            throw new ServiceException(ResEnum.ACCOUNT_UNREGISTER);
        }
        int userStatus = user.getUserStatus();
        if (userStatus == 1) {
            // 用户已锁定
            this.delUserCache(userId);
            throw new ServiceException((ResEnum.ACCOUNT_LOCKED));
        } else if (userStatus == 2) {
            // 用户已停用
            this.delUserCache(userId);
            throw new ServiceException((ResEnum.ACCOUNT_DISABLED));
        }
        UserInfo userInfo = BeanUtils.convertTo(user, UserInfo::new);

        // 2. 查询用户绑定角色信息
        SysRole role = findUserRoleByAppType(userId);
        if (role == null) {
            // 用户未绑定角色
            this.delUserCache(userId);
            throw new ServiceException(ResEnum.ACCOUNT_ROLE_ERROR);
        }
        userInfo.setRoleId(role.getId());
        Long superManager = 6067634233359835136L;
        if (superManager.equals(role.getId())) {
            userInfo.setSuperManager(true);
        }

        // 3. 查询用户数据权限 TODO 处理数据权限

        // 4. 保存到缓存
        cacheManage.setSysUser(userInfo);
        return userInfo;
    }

    @Override
    public void delUserCache(Long userId) {
        // 当角色信息修改时，刷新用户信息缓存，只刷新已有缓存中绑定该角色的用户信息
        String tempUserId = BusinessUtils.getTempUserId(userId, true);
        String tokenKey = String.format(CacheConstant.CACHE_STR_KEY_TOKEN_KEY, TokenEnum.WEB.getCode(), tempUserId);
        redisCache.deleteKeyValueFuzzy(tokenKey);
        String userInfoKey = String.format(CacheConstant.CACHE_KEY_USER_INFO, TokenEnum.WEB.getCode(), tempUserId);
        redisCache.deleteKeyValueFuzzy(userInfoKey);
    }
}
