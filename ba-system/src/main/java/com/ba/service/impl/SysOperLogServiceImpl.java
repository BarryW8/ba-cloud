package com.ba.service.impl;

import com.ba.base.PageView;
import com.ba.base.SimpleModel;
import com.ba.base.UserContext;
import com.ba.base.UserInfo;
import com.ba.cache.CacheConstant;
import com.ba.cache.CacheManage;
import com.ba.enums.ResEnum;
import com.ba.enums.TokenEnum;
import com.ba.exception.ServiceException;
import com.ba.mapper.SysOperLogMapper;
import com.ba.mapper.SysUserMapper;
import com.ba.mapper.SysUserRoleMapper;
import com.ba.model.system.SysOperLog;
import com.ba.model.system.SysUserRole;
import com.ba.service.SysOperLogService;
import com.ba.service.SysUserService;
import com.ba.uid.impl.CachedUidGenerator;
import com.ba.util.BeanUtils;
import com.ba.util.CommonUtils;
import com.ba.util.RedisCache;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Service
public class SysOperLogServiceImpl implements SysOperLogService {

    @Resource
    private SysOperLogMapper sysOperLogMapper;

    @Transactional
    @Override
    public int insert(SysOperLog model) {
        return sysOperLogMapper.insert(model);
    }

    @Transactional
    @Override
    public int update(SysOperLog model) {
        return sysOperLogMapper.update(model);
    }

    @Override
    public SysOperLog findById(Long modelId) {
        return sysOperLogMapper.findById(modelId);
    }

    @Transactional
    @Override
    public int deleteBySm(SimpleModel simpleModel) {
        return sysOperLogMapper.deleteBySm(simpleModel);
    }

    @Override
    public List<SysOperLog> findList(String condition) {
        return sysOperLogMapper.findList(condition);
    }

    @Override
    public PageView<SysOperLog> findPage(Map<String, Object> map) {
        int count = sysOperLogMapper.count(map);
        PageView<SysOperLog> pageModel = new PageView<>();
        if (count > 0) {
            List<SysOperLog> list = sysOperLogMapper.findPage(map);
            pageModel.setData(list);
            pageModel.setTotal(count);
        } else {
            List<SysOperLog> list = new ArrayList<>();
            pageModel.setData(list);
            pageModel.setTotal(0);
        }
        return pageModel;
    }

    @Override
    public int deleteById(SysOperLog model) {
        return 0;
    }
}
