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
import java.util.Map;

@Service
public class SysMenuServiceImpl implements SysMenuService {

    @Resource
    private SysMenuMapper sysMenuMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int insert(SysMenu sysMenu) {
        return sysMenuMapper.insert(sysMenu);
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
    public PageView<SysMenu> findPage(Map<String, Object> map) {
        return null;
    }
}
