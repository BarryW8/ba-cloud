package com.smart.service.impl;

import com.smart.base.PageView;
import com.smart.base.SimpleModel;
import com.smart.mapper.SysMenuMapper;
import com.smart.model.user.SysMenu;
import com.smart.service.SysMenuService;
import com.smart.vo.SysMenuVO;
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
    public int nameUnique(Long modelId, String name) {
        return sysMenuMapper.nameUnique(modelId, name);
    }

    @Override
    public List<SysMenuVO> getList() {
        return sysMenuMapper.getList();
    }

    @Override
    public List<SysMenuVO> findAllList() {
        return sysMenuMapper.findAllList();
    }
}
