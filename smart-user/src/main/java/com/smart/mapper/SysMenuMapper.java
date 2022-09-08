package com.smart.mapper;

import com.smart.base.BaseMapper;
import com.smart.model.user.SysMenu;
import com.smart.vo.SysMenuVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    List<SysMenuVO> findAllList();

}
