package com.smart.service.impl;

import com.smart.base.Page;
import com.smart.base.PageView;
import com.smart.base.SimpleModel;
import com.smart.mapper.SysUserRoleMapper;
import com.smart.model.user.SysUserRole;
import com.smart.service.SysUserRoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


@Service
public class SysUserRoleServiceImpl implements SysUserRoleService {


    @Resource
    private SysUserRoleMapper sysUserRoleMapper;

    @Override
    public int save(SysUserRole sysUserRole) {
        return sysUserRoleMapper.save(sysUserRole);
    }

    @Override
    public int update(SysUserRole sysUserRole) {
        return sysUserRoleMapper.update(sysUserRole);
    }

    @Override
    public SysUserRole findById(Long modelId) {
        return sysUserRoleMapper.findById(modelId);
    }

    @Override
    public int deleteBySm(SimpleModel simpleModel) {
        return sysUserRoleMapper.deleteBySm(simpleModel);
    }

    @Override
    public List<SysUserRole> findList(String condition) {
        return sysUserRoleMapper.findList(condition);
    }

    @Override
    public PageView<SysUserRole> findPage(int page, int pageSize, String sql, String params) {
        Page pg = new Page();
        pg.setPage((page - 1) * pageSize);
        pg.setPageSize(pageSize);
        pg.setSql(sql);
        int count = sysUserRoleMapper.count(sql);
        PageView<SysUserRole> pageModel = new PageView<>();
        if (count > 0) {
            List<SysUserRole> list = sysUserRoleMapper.findPage(pg);
            pageModel.setData(list);
            pageModel.setTotal(count);
        } else {
            List<SysUserRole> list = new ArrayList<>();
            pageModel.setData(list);
            pageModel.setTotal(0);
        }
        return pageModel;
    }

    @Override
    public int nameUnique(Long modelId, String name) {
        return sysUserRoleMapper.nameUnique(modelId, name);
    }


}
