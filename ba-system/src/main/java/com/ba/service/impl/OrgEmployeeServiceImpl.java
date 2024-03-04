package com.ba.service.impl;

import com.ba.base.PageView;
import com.ba.base.SimpleModel;
import com.ba.mapper.OrgEmployeeMapper;
import com.ba.model.system.OrgEmployee;
import com.ba.service.OrgEmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class OrgEmployeeServiceImpl implements OrgEmployeeService {

    @Resource
    private OrgEmployeeMapper orgEmployeeMapper;

//    @Resource
//    private SysUserRoleMapper sysUserRoleMapper;

//    @Resource
//    private CacheManage cacheManage;
//
//    @Resource
//    private RedisCache redisCache;
//
//    @Resource
//    private CachedUidGenerator uidGenerator;

//    @Transactional
//    @Override
//    public int saveUserRole(SysUserRole model) {
//        Long currentUserId = UserContext.getUserId();
//        String currentDateStr = CommonUtils.getCurrentDate();
//        Long roleId = model.getRoleId();
//        Long userId = model.getUserId();
//        // 1. 删除旧数据
//        sysUserRoleMapper.deleteByUserId(userId);
//        // 判断是否需要新增，同时防止空删除保存失败
//        if (roleId == null) {
//            return 1;
//        }
//        // 2. 保存新数据
//        model.setId(uidGenerator.getUID());
//        model.setCreateBy(currentUserId);
//        model.setCreateTime(currentDateStr);
//        return sysUserRoleMapper.insert(model);
//    }

//    @Override
//    public SysUserRole findUserRole(Long userId) {
//        List<SysUserRole> list = sysUserRoleMapper.findListBySQL(" and user_id = " + userId);
//        if (CollectionUtils.isEmpty(list)) return null;
//        return list.get(0);
//    }

    @Transactional
    @Override
    public int add(OrgEmployee orgEmployee) {
        return orgEmployeeMapper.insert(orgEmployee);
    }

    @Transactional
    @Override
    public int edit(OrgEmployee orgEmployee) {
        return orgEmployeeMapper.update(orgEmployee);
    }

    @Override
    public OrgEmployee findById(Long modelId) {
        return orgEmployeeMapper.findById(modelId);
    }

    @Transactional
    @Override
    public int deleteBySm(SimpleModel simpleModel) {
        return orgEmployeeMapper.deleteBySm(simpleModel);
    }

    @Override
    public List<OrgEmployee> findList(Map<String, Object> map) {
        return orgEmployeeMapper.findList(map);
    }

    @Override
    public List<OrgEmployee> findListBySQL(String condition) {
        return orgEmployeeMapper.findListBySQL(condition);
    }

    @Override
    public PageView<OrgEmployee> findPage(Map<String, Object> map) {
        int count = orgEmployeeMapper.count(map);
        PageView<OrgEmployee> pageModel = new PageView<>();
        if (count > 0) {
            List<OrgEmployee> list = orgEmployeeMapper.findPage(map);
            pageModel.setData(list);
            pageModel.setTotal(count);
        } else {
            List<OrgEmployee> list = new ArrayList<>();
            pageModel.setData(list);
            pageModel.setTotal(0);
        }
        return pageModel;
    }

//    @Override
//    public List<OrgEmployee> findListHasPwd(String sql) {
//        return orgEmployeeMapper.findListHasPwd(sql);
//    }

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

//    /**
//     * 刷新用户缓存
//     */
//    @Override
//    public UserInfo setUserCache(Long userId) {
//        // 1. 查询用户基本信息
//        OrgEmployee user = orgEmployeeMapper.findById(userId);
//        if (Objects.isNull(user)) {
//            this.delUserCache(userId);
//            throw new ServiceException(ResEnum.ACCOUNT_UNREGISTER);
//        }
//        int userStatus = user.getUserStatus();
//        if (userStatus == 1) {
//            // 用户已锁定
//            this.delUserCache(userId);
//            throw new ServiceException((ResEnum.ACCOUNT_LOCKED));
//        } else if (userStatus == 2) {
//            // 用户已停用
//            this.delUserCache(userId);
//            throw new ServiceException((ResEnum.ACCOUNT_DISABLED));
//        }
//        UserInfo userInfo = BeanUtils.convertTo(user, UserInfo::new);
//
//        // 2. 查询用户绑定角色信息
//        SysUserRole userRole = findUserRole(userId);
//        if (userRole == null) {
//            // 用户未绑定角色
//            this.delUserCache(userId);
//            throw new ServiceException(ResEnum.ACCOUNT_ROLE_ERROR);
//        }
//        userInfo.setRoleId(userRole.getRoleId());
//        Long superManager = 6067634233359835136L;
//        if (superManager.equals(userRole.getRoleId())) {
//            userInfo.setSuperManager(true);
//        }
//
//        // 3. 查询用户数据权限 TODO 处理数据权限
//
//        // 4. 保存到缓存
//        cacheManage.setSysUser(userInfo);
//        return userInfo;
//    }
//
//    @Override
//    public void delUserCache(Long userId) {
//        // 当角色信息修改时，刷新用户信息缓存，只刷新已有缓存中绑定该角色的用户信息
//        String tempUserId = BusinessUtils.getTempUserId(userId, true);
//        String redisKey = String.format(CacheConstant.CACHE_STR_KEY_TOKEN_KEY, TokenEnum.WEB.getCode(), tempUserId);
//        redisCache.deleteKeyValueFuzzy(redisKey);
//    }
}
