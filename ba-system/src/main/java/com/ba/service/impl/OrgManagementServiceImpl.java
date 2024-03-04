package com.ba.service.impl;

import com.ba.base.PageView;
import com.ba.base.SimpleModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ba.mapper.OrgManagementMapper;
import com.ba.model.system.OrgManagement;
import com.ba.service.OrgManagementService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class OrgManagementServiceImpl implements OrgManagementService {

    @Resource
    private OrgManagementMapper orgManagementMapper;

    @Transactional
    @Override
    public int add(OrgManagement model) {
        return orgManagementMapper.insert(model);
    }

    @Transactional
    @Override
    public int edit(OrgManagement model) {
        return orgManagementMapper.update(model);
    }

    @Override
    public OrgManagement findById(Long modelId) {
        return orgManagementMapper.findById(modelId);
    }

    @Transactional
    @Override
    public int deleteBySm(SimpleModel simpleModel) {
        return orgManagementMapper.deleteBySm(simpleModel);
    }

    @Override
    public List<OrgManagement> findList(Map<String, Object> map) {
        return orgManagementMapper.findList(map);
    }

    @Override
    public List<OrgManagement> findListBySQL(String condition) {
        return orgManagementMapper.findListBySQL(condition);
    }

    @Override
    public PageView<OrgManagement> findPage(Map<String, Object> map) {
        int count = orgManagementMapper.count(map);
        PageView<OrgManagement> pageModel = new PageView<>();
        if (count > 0) {
            List<OrgManagement> list = orgManagementMapper.findPage(map);
            pageModel.setData(list);
            pageModel.setTotal(count);
        } else {
            List<OrgManagement> list = new ArrayList<>();
            pageModel.setData(list);
            pageModel.setTotal(0);
        }
        return pageModel;
    }

}
