package com.ba.service.impl;

import com.ba.base.PageView;
import com.ba.base.SimpleModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ba.mapper.ThinkingMemoMapper;
import com.ba.model.system.ThinkingMemo;
import com.ba.service.ThinkingMemoService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ThinkingMemoServiceImpl implements ThinkingMemoService {

    @Resource
    private ThinkingMemoMapper thinkingMemoMapper;

    @Transactional
    @Override
    public int add(ThinkingMemo model) {
        return thinkingMemoMapper.insert(model);
    }

    @Transactional
    @Override
    public int edit(ThinkingMemo model) {
        return thinkingMemoMapper.update(model);
    }

    @Override
    public ThinkingMemo findById(Long modelId) {
        return thinkingMemoMapper.findById(modelId);
    }

    @Transactional
    @Override
    public int deleteBySm(SimpleModel simpleModel) {
        return thinkingMemoMapper.deleteBySm(simpleModel);
    }

    @Override
    public List<ThinkingMemo> findList(Map<String, Object> map) {
        return thinkingMemoMapper.findList(map);
    }

    @Override
    public List<ThinkingMemo> findListBySQL(String condition) {
        return thinkingMemoMapper.findListBySQL(condition);
    }

    @Override
    public PageView<ThinkingMemo> findPage(Map<String, Object> map) {
        int count = thinkingMemoMapper.count(map);
        PageView<ThinkingMemo> pageModel = new PageView<>();
        if (count > 0) {
            List<ThinkingMemo> list = thinkingMemoMapper.findPage(map);
            pageModel.setData(list);
            pageModel.setTotal(count);
        } else {
            List<ThinkingMemo> list = new ArrayList<>();
            pageModel.setData(list);
            pageModel.setTotal(0);
        }
        return pageModel;
    }

}
