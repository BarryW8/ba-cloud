package com.smart.service;

import com.smart.base.BaseService;
import com.smart.dto.SysUserRoleDTO;
import com.smart.enums.BizCodeEnum;
import com.smart.model.user.SysUser;
import com.smart.model.user.SysUserRole;

import java.util.List;

public interface SysUserService extends BaseService<SysUser> {
    List<SysUser> findListHasPwd(String sql);

    /**
     * 刷新用户信息
     *
     * @param userIds 用户ID集合
     * @param type 0代表登陆和重新请求 1代表其它
     */
    BizCodeEnum setUserCache(List<Long> userIds, int type);

    int saveUserRole(SysUserRoleDTO dto);

    List<SysUserRole> findUserRole(Long modelId);
}
