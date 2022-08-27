package com.smart.service.impl;

import com.smart.base.PageView;
import com.smart.base.SimpleModel;
import com.smart.mapper.DictionaryMapper;
import com.smart.model.user.Dictionary;
import com.smart.service.DictionaryService;
import com.smart.vo.DictionaryVO;
import com.smart.vo.OptionVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class DictionaryServiceImpl implements DictionaryService {

    @Resource
    private DictionaryMapper dictionaryMapper;

    @Transactional
    @Override
    public int save(Dictionary dictionary) {
        return dictionaryMapper.save(dictionary);
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
    	//存储所有要删除的id
        List<Long> idList = new ArrayList<>();
        idList.add(simpleModel.getModelId());
        selectChildListById(simpleModel.getModelId(), idList);
        return dictionaryMapper.batchDeleteBySm(simpleModel.getDelUser(),simpleModel.getDelUserName(),simpleModel.getDelDate(),idList);
    }

    /**
     * 查询所有子节点
     * @param id
     * @param idList
     */
    private void selectChildListById(Long id, List<Long> idList) {

    	List<Dictionary> childList =  dictionaryMapper.findList(" and parent_id = " + id);
        //递归查询下一级id,同时将上一次查询结果添加到list集合
        childList.forEach(menu-> {
            idList.add(menu.getId());
            this.selectChildListById(menu.getId(), idList);
        });
    }
    @Override
    public List<Dictionary> findList(String condition) {
        return dictionaryMapper.findList(condition);
    }

    @Override
    public PageView<Dictionary> findPage(int page, int pageSize, String sql, String params) {
        return null;
    }

    @Override
    public List<DictionaryVO> getList() {
        return dictionaryMapper.getList();
    }

    @Override
    public List<OptionVO> optionListByParentCode(String parentCode) {
        return dictionaryMapper.optionListByParentCode(parentCode);
    }

    @Override
    public List<Dictionary> checkNameSame(Dictionary dictionary) {
        StringBuilder sql = new StringBuilder();
        if (StringUtils.isNotEmpty(dictionary.getName())) {
            sql.append(" and name = '").append(dictionary.getName()).append("'");
        }
        if (dictionary.getParentId() == null) {
            sql.append(" and parent_id = 0");
        } else {
            sql.append(" and parent_id = ").append(dictionary.getParentId());
        }
        return dictionaryMapper.findList(sql.toString());
    }

    @Override
    public List<Dictionary> checkCodeSame(Dictionary dictionary) {
        StringBuilder sql = new StringBuilder();
        if (StringUtils.isNotEmpty(dictionary.getCode())) {
            sql.append(" and code = '").append(dictionary.getCode()).append("'");
        }
        if (dictionary.getParentId() == null) {
            sql.append(" and parent_id = 0");
        } else {
            sql.append(" and parent_id = ").append(dictionary.getParentId());
        }
        return dictionaryMapper.findList(sql.toString());
    }

}
