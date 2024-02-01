package com.ba.service.impl;

import com.ba.base.PageView;
import com.ba.base.SimpleModel;
import com.ba.mapper.DictionaryMapper;
import com.ba.model.system.Dictionary;
import com.ba.service.DictionaryService;
import com.ba.util.StringUtils;
import com.ba.vo.OptionVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class DictionaryServiceImpl implements DictionaryService {

    @Resource
    private DictionaryMapper dictionaryMapper;

    @Transactional
    @Override
    public int insert(Dictionary dictionary) {
        return dictionaryMapper.insert(dictionary);
    }

    @Transactional
    @Override
    public int update(Dictionary dictionary) {
        return dictionaryMapper.update(dictionary);
    }

    @Override
    public Dictionary findById(Long modelId) {
        return dictionaryMapper.findById(modelId);
    }

    @Transactional
    @Override
    public int deleteBySm(SimpleModel simpleModel) {
        return dictionaryMapper.deleteBySm(simpleModel);
    }

    @Override
    public List<Dictionary> findList(Map<String, Object> map) {
        return dictionaryMapper.findList(map);
    }

    @Override
    public List<Dictionary> findListBySQL(String condition) {
        return dictionaryMapper.findListBySQL(condition);
    }

    @Override
    public PageView<Dictionary> findPage(Map<String, Object> map) {
        return null;
    }

    @Override
    public List<OptionVO> optionList(String parentCode) {
        StringBuilder sql = new StringBuilder();
        if (StringUtils.isNotEmpty(parentCode)) {
            sql.append(" and parent_code = '").append(parentCode).append("'");
        }
        return dictionaryMapper.optionList(sql.toString());
    }

    @Override
    public List<Dictionary> checkNameSame(Dictionary dictionary) {
        StringBuilder sql = new StringBuilder();
        if (dictionary.getId() != null) {
            sql.append(" and id != ").append(dictionary.getId());
        }
        if (StringUtils.isNotEmpty(dictionary.getName())) {
            sql.append(" and name = '").append(dictionary.getName()).append("'");
        }
        if (dictionary.getParentId() == null) {
            sql.append(" and parent_id = -1");
        } else {
            sql.append(" and parent_id = ").append(dictionary.getParentId());
        }
        return dictionaryMapper.findListBySQL(sql.toString());
    }

    @Override
    public List<Dictionary> checkCodeSame(Dictionary dictionary) {
        StringBuilder sql = new StringBuilder();
        if (dictionary.getId() != null) {
            sql.append(" and id != ").append(dictionary.getId());
        }
        if (StringUtils.isNotEmpty(dictionary.getCode())) {
            sql.append(" and code = '").append(dictionary.getCode()).append("'");
        }
        if (dictionary.getParentId() == null) {
            sql.append(" and parent_id = -1");
        } else {
            sql.append(" and parent_id = ").append(dictionary.getParentId());
        }
        return dictionaryMapper.findListBySQL(sql.toString());
    }

}
