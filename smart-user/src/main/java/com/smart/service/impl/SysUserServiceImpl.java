package com.smart.service.impl;

import com.smart.base.Page;
import com.smart.base.PageView;
import com.smart.base.SimpleModel;
import com.smart.mapper.SysUserMapper;
import com.smart.model.user.SysUser;
import com.smart.service.SysUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class SysUserServiceImpl implements SysUserService {

    @Resource
    private SysUserMapper sysUserMapper;

    @Transactional
    @Override
    public int save(SysUser sysUser) {
        return sysUserMapper.save(sysUser);
    }

    @Transactional
    @Override
    public int update(SysUser sysUser) {
        return sysUserMapper.update(sysUser);
    }

    @Override
    public SysUser findById(Long modelId) {
        return sysUserMapper.findById(modelId);
    }

    @Transactional
    @Override
    public int deleteBySm(SimpleModel simpleModel) {
        return sysUserMapper.deleteBySm(simpleModel);
    }

    @Override
    public List<SysUser> findList(String condition) {
        return sysUserMapper.findList(condition);
    }

    @Override
    public PageView<SysUser> findPage(int page, int pageSize, String sql, String params) {
        Page pg = new Page();
        pg.setPage((page - 1) * pageSize);
        pg.setPageSize(pageSize);
        pg.setSql(sql);
        int count = sysUserMapper.count(sql);
        PageView<SysUser> pageModel = new PageView<>();
        if (count > 0) {
            List<SysUser> list = sysUserMapper.findPage(pg);
            pageModel.setData(list);
            pageModel.setTotal(count);
        } else {
            List<SysUser> list = new ArrayList<>();
            pageModel.setData(list);
            pageModel.setTotal(0);
        }
        return pageModel;
    }

    @Override
    public List<SysUser> findListHasPwd(String sql) {
        return sysUserMapper.findListHasPwd(sql);
    }
}
