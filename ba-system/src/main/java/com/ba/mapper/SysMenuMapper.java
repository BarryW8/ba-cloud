package com.ba.mapper;

import com.ba.base.BaseMapper;
import com.ba.model.system.SysMenu;
import com.ba.vo.SysMenuVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    List<SysMenuVO> findAllList(String sql);

}
