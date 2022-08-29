package com.smart.service;

import com.smart.base.BaseService;
import com.smart.model.user.SysUser;

import java.util.List;

public interface SysUserService extends BaseService<SysUser> {
    List<SysUser> findListHasPwd(String sql);
}
