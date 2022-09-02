package com.smart.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.smart.base.Page;
import com.smart.base.PageView;
import com.smart.base.SimpleModel;
import com.smart.cache.CacheManage;
import com.smart.dto.SysUserRoleDTO;
import com.smart.enums.BizCodeEnum;
import com.smart.mapper.SysUserMapper;
import com.smart.mapper.SysUserRoleMapper;
import com.smart.model.LoginUser;
import com.smart.model.user.SysUser;
import com.smart.model.user.SysUserRole;
import com.smart.service.SysUserService;
import com.smart.uid.impl.CachedUidGenerator;
import com.smart.vo.UserInfoVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class SysUserServiceImpl implements SysUserService {

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private SysUserRoleMapper sysUserRoleMapper;

    @Resource
    private CacheManage cacheManage;

    @Resource
    private CachedUidGenerator uidGenerator;

    @Transactional
    @Override
    public int save(SysUser sysUser) {
        return sysUserMapper.save(sysUser);
    }

    @Transactional
    @Override
    public int update(SysUser sysUser) {
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
    public List<SysUser> findList(String condition) {
        return sysUserMapper.findList(condition);
    }

    @Override
    public PageView<SysUser> findPage(int page, int pageSize, String sql, String params) {
        Page pg = new Page();
        pg.setPage((page - 1) * pageSize);
        pg.setPageSize(pageSize);
        pg.setSql(sql);
        int count = sysUserMapper.count(sql);
        PageView<SysUser> pageModel = new PageView<>();
        if (count > 0) {
            List<SysUser> list = sysUserMapper.findPage(pg);
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

    @Override
    public BizCodeEnum setUserCache(List<Long> userIds, int type) {
        BizCodeEnum bizCodeEnum = null;
        if (type == 0) {
            // 直接set
            for (Long userId : userIds) {
                bizCodeEnum = this.userCache(userId);
            }
        } else {
            // 当角色信息修改时，刷新用户信息缓存，只刷新已有缓存中绑定该角色的用户信息
            for (Long userId : userIds) {
                LoginUser info = cacheManage.getSysUser4Id(userId.toString());
                if (info != null) {
                    bizCodeEnum = this.userCache(userId);
                }
            }
        }
        return bizCodeEnum;
    }

    @Override
    public int saveUserRole(SysUserRoleDTO dto) {
        LoginUser currentUser = dto.getCurrentUser();
        String currentDate = dto.getCurrentDate();
        Long roleId = dto.getRoleId();
        Long userId = dto.getUserId();
        // 1. 删除旧数据
        sysUserRoleMapper.deleteByUserId(userId);
        if (roleId == null) {
            return 1;
        }
        // 2. 保存新数据
        SysUserRole userRole = new SysUserRole();
        userRole.setRoleId(roleId);
        userRole.setUserId(userId);
        userRole.setId(uidGenerator.getUID());
        userRole.setCreateUserId(currentUser.getUserId());
        userRole.setCreateUserName(currentUser.getRealName());
        userRole.setCreateTime(currentDate);
        return sysUserRoleMapper.save(userRole);
    }

    @Override
    public List<SysUserRole> findUserRole(Long userId) {
        StringBuilder sqlBd = new StringBuilder();
        sqlBd.append(" and user_id = ").append(userId);
        return sysUserRoleMapper.findList(sqlBd.toString());
    }

    /**
     * 用户缓存存入用户、角色、菜单信息，之后每次刷新页面只需查缓存即可
     */
    private BizCodeEnum userCache(Long userId) {
        LoginUser loginUser = new LoginUser();
        //-------------a、查询用户基本信息
        SysUser sysUser = sysUserMapper.findById(userId);
        BeanUtil.copyProperties(sysUser, loginUser);
        loginUser.setUserId(userId);
        //-------------b、查询用户-角色关联信息
        List<SysUserRole> userRoles = sysUserRoleMapper.findByLogin(userId);
        if (CollectionUtils.isEmpty(userRoles)) {
            // 用户未绑定角色，删除缓存
            cacheManage.delSysUser(userId.toString());
            return BizCodeEnum.ACCOUNT_ROLE_ERROR;
        }
        loginUser.setRoleId(userRoles.get(0).getRoleId());
        //-------------c、保存到缓存
        cacheManage.setSysUser(loginUser);
        return null;
    }
}
