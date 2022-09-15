package com.smart.service;

import com.smart.base.BaseService;
import com.smart.model.user.SysMenu;
import com.smart.vo.SysMenuVO;

import java.util.List;

public interface SysMenuService extends BaseService<SysMenu> {

    List<SysMenuVO> findAllList(String sql);

}
