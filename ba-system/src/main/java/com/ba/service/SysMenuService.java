package com.ba.service;

import com.ba.base.BaseService;
import com.ba.model.system.SysMenu;
import com.ba.vo.SysMenuVO;

import java.util.List;

public interface SysMenuService extends BaseService<SysMenu> {

    List<SysMenuVO> findAllList(String sql);

}
