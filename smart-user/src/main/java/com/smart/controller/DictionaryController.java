package com.smart.controller;

import cn.hutool.core.date.DatePattern;
import com.smart.base.BaseCommonController;
import com.smart.base.BaseController;
import com.smart.base.BasePageDTO;
import com.smart.base.SimpleModel;
import com.smart.model.user.Dictionary;
import com.smart.service.DictionaryService;
import com.smart.uid.impl.CachedUidGenerator;
import com.smart.util.JsonData;
import com.smart.vo.DictionaryVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/dictionary")
@Slf4j
public class DictionaryController extends BaseController implements BaseCommonController<Dictionary, BasePageDTO> {

    @Resource
    private DictionaryService dictionaryService;

    @Resource
    private CachedUidGenerator uidGenerator;

    /**
     * 根据父编号获取字典列表
     */
    @GetMapping("optionListByParentCode")
    public JsonData optionListByParentCode(@RequestParam String parentCode) {
        return JsonData.buildSuccess(dictionaryService.optionListByParentCode(parentCode));
    }

    @GetMapping("findById")
    @Override
    public JsonData findById(@RequestParam Long modelId) {
        Dictionary dictionary = dictionaryService.findById(modelId);
        if (dictionary == null) {
            return JsonData.buildError("菜单不存在！");
        }
        return JsonData.buildSuccess(dictionary);
    }


    @PostMapping("save")
    @Override
    public JsonData save(@RequestBody Dictionary dictionary) {
        int count;
        List<Dictionary> nameList = dictionaryService.checkNameSame(dictionary);
        if (!CollectionUtils.isEmpty(nameList)) {
            //字典同级name不可相同
            return JsonData.buildError("同级字典名称不能相同！");
        }
        List<Dictionary> codeList = dictionaryService.checkCodeSame(dictionary);
        if (!CollectionUtils.isEmpty(codeList)) {
            //字典同级code不可相同
            return JsonData.buildError("同级字典code不能相同！");
        }
        // 封装数据
        if (dictionary.getParentId() == null) {
            dictionary.setParentId(0L);
        }
        if (dictionary.getId() == null) {
            // 新增
            dictionary.setId(uidGenerator.getUID());
            dictionary.setCreateUserId(1000L);
            dictionary.setCreateUserName("ceshi");
            dictionary.setCreateTime(getCurrentDate(DatePattern.NORM_DATETIME_PATTERN));
            count = dictionaryService.save(dictionary);
        } else {
            //编辑
            dictionary.setUpdateUserId(1000L);
            dictionary.setUpdateUserName("ceshi");
            dictionary.setUpdateTime(getCurrentDate(DatePattern.NORM_DATETIME_PATTERN));
            count = dictionaryService.update(dictionary);
        }
        if (count > 0) {
            return JsonData.buildSuccess();
        }
        return JsonData.buildError("保存失败!");
    }

    @PostMapping("findPage")
    @Override
    public JsonData findPage(@RequestBody BasePageDTO dto) {
        return null;
    }

    @GetMapping("deleteById")
    @Override
    public JsonData deleteById(@RequestParam Long modelId) {
        SimpleModel simpleModel = new SimpleModel();
        simpleModel.setModelId(modelId);
        simpleModel.setDelUser(1000L);
        simpleModel.setDelUserName("ceshi");
        simpleModel.setDelDate(getCurrentDate(DatePattern.NORM_DATETIME_PATTERN));
        int result = dictionaryService.deleteBySm(simpleModel);
        if (result > 0) {
            return JsonData.buildSuccess();
        }
        return JsonData.buildError("删除失败!");
    }

    @GetMapping("getList")
    public JsonData getList() {
        List<DictionaryVO> list = dictionaryService.getList();
        return JsonData.buildSuccess(builder(list));
    }

    private List<DictionaryVO> builder(List<DictionaryVO> nodes) {
        List<DictionaryVO> treeNodes = new ArrayList<>();
        for (DictionaryVO n1 : nodes) {
            // 0 代表根节点(顶级父节点)
            if (n1.getParentId() == 0) {
                treeNodes.add(n1);
            }
            for (DictionaryVO n2 : nodes) {
                if (n2.getParentId().equals(n1.getId())) {
                    n1.getChildren().add(n2);
                }
            }
        }
        return treeNodes;
    }

}
