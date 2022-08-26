package com.smart.service.impl;

import com.smart.base.Page;
import com.smart.base.PageView;
import com.smart.base.SimpleModel;
import com.smart.mapper.SysRoleMenuMapper;
import com.smart.model.user.SysRoleMenu;
import com.smart.service.SysRoleMenuService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


@Service
public class SysRoleMenuServiceImpl implements SysRoleMenuService {


    @Resource
    private SysRoleMenuMapper sysRoleMenuMapper;

    @Override
    public int save(SysRoleMenu sysRoleMenu) {
        return sysRoleMenuMapper.save(sysRoleMenu);
    }

    @Override
    public int update(SysRoleMenu sysRoleMenu) {
        return sysRoleMenuMapper.update(sysRoleMenu);
    }

    @Override
    public SysRoleMenu findById(Long modelId) {
        return sysRoleMenuMapper.findById(modelId);
    }

    @Override
    public int deleteBySm(SimpleModel simpleModel) {
        return sysRoleMenuMapper.deleteBySm(simpleModel);
    }

    @Override
    public List<SysRoleMenu> findList(String condition) {
        return sysRoleMenuMapper.findList(condition);
    }

    @Override
    public PageView<SysRoleMenu> findPage(int page, int pageSize, String sql, String params) {
        Page pg = new Page();
        pg.setPage((page - 1) * pageSize);
        pg.setPageSize(pageSize);
        pg.setSql(sql);
        int count = sysRoleMenuMapper.count(sql);
        PageView<SysRoleMenu> pageModel = new PageView<>();
        if (count > 0) {
            List<SysRoleMenu> list = sysRoleMenuMapper.findPage(pg);
            pageModel.setData(list);
            pageModel.setTotal(count);
        } else {
            List<SysRoleMenu> list = new ArrayList<>();
            pageModel.setData(list);
            pageModel.setTotal(0);
        }
        return pageModel;
    }

    @Override
    public int nameUnique(Long modelId, String name) {
        return sysRoleMenuMapper.nameUnique(modelId, name);
    }


}
