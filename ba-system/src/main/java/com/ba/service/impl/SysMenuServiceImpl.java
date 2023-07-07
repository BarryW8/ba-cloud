package com.ba.service.impl;

import com.ba.base.PageView;
import com.ba.base.SimpleModel;
import com.ba.mapper.SysMenuMapper;
import com.ba.model.system.SysMenu;
import com.ba.service.SysMenuService;
import com.ba.vo.SysMenuVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SysMenuServiceImpl implements SysMenuService {

    @Resource
    private SysMenuMapper sysMenuMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int save(SysMenu sysMenu) {
        return sysMenuMapper.save(sysMenu);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int update(SysMenu sysMenu) {
        return sysMenuMapper.update(sysMenu);
    }

    @Override
    public SysMenu findById(Long modelId) {
        return sysMenuMapper.findById(modelId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteBySm(SimpleModel simpleModel) {
        return sysMenuMapper.deleteBySm(simpleModel);
    }

    @Override
    public List<SysMenu> findList(String condition) {
        return sysMenuMapper.findList(condition);
    }

    @Override
    public PageView<SysMenu> findPage(int page, int pageSize, String sql, String params) {
        return null;
    }

    @Override
    public List<SysMenuVO> findAllList(String sql) {
        return sysMenuMapper.findAllList(sql);
    }
}
