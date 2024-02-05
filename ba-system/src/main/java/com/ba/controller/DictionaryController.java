package com.ba.controller;

import com.ba.base.BaseCommonController;
import com.ba.base.BaseController;
import com.ba.base.BasePage;
import com.ba.annotation.Permission;
import com.ba.base.SimpleModel;
import com.ba.dto.DictionaryPage;
import com.ba.enums.OperationEnum;
import com.ba.model.system.Dictionary;
import com.ba.response.ResData;
import com.ba.service.DictionaryService;
import com.ba.uid.impl.CachedUidGenerator;
import com.ba.util.BeanUtils;
import com.ba.util.StringUtils;
import com.ba.vo.DictionaryVO;
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
import java.util.Map;

@RestController
@RequestMapping("/dictionary")
@Slf4j
public class DictionaryController extends BaseController implements BaseCommonController<Dictionary, BasePage> {

    private static final String MENU_CODE = "system:dictionary";

    @Resource
    private DictionaryService dictionaryService;

    @Resource
    private CachedUidGenerator uidGenerator;

    /**
     * 下拉列表
     * @param parentCode 父级编码
     */
    @Permission(menuFlag = MENU_CODE, perms = OperationEnum.VIEW)
    @GetMapping("optionList")
    public ResData optionList(@RequestParam String parentCode) {
        return ResData.success(dictionaryService.optionList(parentCode));
    }

    @Permission(menuFlag = MENU_CODE, perms = OperationEnum.VIEW)
    @GetMapping("findById")
    @Override
    public ResData findById(@RequestParam Long modelId) {
        Dictionary dictionary = dictionaryService.findById(modelId);
        if (dictionary == null) {
            return ResData.error("菜单不存在！");
        }
        return ResData.success(dictionary);
    }

    private String checkParams(Dictionary dictionary) {
        List<Dictionary> codeList = dictionaryService.checkCodeSame(dictionary);
        if (!CollectionUtils.isEmpty(codeList)) {
            //字典同级code不可相同
            return "同级字典编号不能相同！";
        }
        List<Dictionary> nameList = dictionaryService.checkNameSame(dictionary);
        if (!CollectionUtils.isEmpty(nameList)) {
            //字典同级name不可相同
            return "同级字典名称不能相同！";
        }
        return "";
    }

    @Permission(menuFlag = MENU_CODE, perms = OperationEnum.ADD)
    @PostMapping("add")
    @Override
    public ResData add(@RequestBody Dictionary dictionary) {
        // 检验参数
        String errorMsg = this.checkParams(dictionary);
        if (StringUtils.isNotEmpty(errorMsg)) {
            return ResData.error(errorMsg);
        }
        // 封装数据
        if (dictionary.getParentId() == null) {
            dictionary.setParentId(-1L);
        }
        dictionary.setId(uidGenerator.getUID());
        int result = dictionaryService.add(dictionary);
        if (result > 0) {
            return ResData.success();
        }
        return ResData.error("保存失败!");
    }

    @Permission(menuFlag = MENU_CODE, perms = OperationEnum.EDIT)
    @PostMapping("edit")
    @Override
    public ResData edit(@RequestBody Dictionary dictionary) {
        // 检验参数
        String errorMsg = this.checkParams(dictionary);
        if (StringUtils.isNotEmpty(errorMsg)) {
            return ResData.error(errorMsg);
        }
        // 封装数据
        if (dictionary.getParentId() == null) {
            dictionary.setParentId(-1L);
        }
        int result = dictionaryService.edit(dictionary);
        if (result > 0) {
            return ResData.success();
        }
        return ResData.error("保存失败!");
    }

    @Permission(menuFlag = MENU_CODE, perms = OperationEnum.VIEW)
    @PostMapping("findPage")
    @Override
    public ResData findPage(@RequestBody BasePage dto) {
        return null;
    }

    @Permission(menuFlag = MENU_CODE, perms = OperationEnum.DELETE)
    @GetMapping("deleteById")
    @Override
    public ResData deleteById(@RequestParam Long modelId) {
        SimpleModel simpleModel = new SimpleModel();
        simpleModel.setModelId(modelId);
        simpleModel.setDelUser(getCurrentUserId());
        simpleModel.setDelDate(getCurrentDateStr());
        int result = dictionaryService.deleteBySm(simpleModel);
        if (result > 0) {
            return ResData.success();
        }
        return ResData.error("删除失败!");
    }

    /**
     * 查询树
     */
    @Permission(menuFlag = MENU_CODE, perms = OperationEnum.VIEW)
    @PostMapping("findTree")
    public ResData findTree(@RequestBody DictionaryPage dto) {
        Map<String, Object> queryMap = this.queryCondition(dto);
        List<Dictionary> dictionaries = dictionaryService.findList(null);
        List<DictionaryVO> list = BeanUtils.convertListTo(dictionaries, DictionaryVO::new);
        return ResData.success(builder(list));
    }

    private Map<String, Object> queryCondition(DictionaryPage dto) {
        Map<String, Object> queryMap = dto.toPageMap();
        return queryMap;
//        if (status != null) {
//            sqlBf.append(" and status = ").append(status);
//        }
//        if (StringUtils.isNotEmpty(keyword)) {
//            sqlBf.append(" and (code like '%").append(keyword).append("%'")
//                    .append(" or name like '%").append(keyword).append("%'").append(")");
//        }
    }

    /**
     * 构建树型结构
     */
    private List<DictionaryVO> builder(List<DictionaryVO> nodes) {
        List<DictionaryVO> treeNodes = new ArrayList<>();
        for (DictionaryVO n1 : nodes) {
            // -1 代表根节点(顶级父节点)
            if (n1.getParentId() == -1L) {
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
