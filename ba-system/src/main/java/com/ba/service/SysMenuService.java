package com.ba.service;

import com.ba.base.BaseService;
import com.ba.model.system.SysMenu;
import com.ba.vo.SysMenuVO;

import java.util.List;
import java.util.Map;

public interface SysMenuService extends BaseService<SysMenu> {

    Map<String, Object> findTree(Map<String, Object> queryMap);
}
