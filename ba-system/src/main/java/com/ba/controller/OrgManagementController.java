package com.ba.controller;

import com.ba.annotation.Log;
import com.ba.annotation.Permission;
import com.ba.base.BaseCommonController;
import com.ba.base.BaseController;
import com.ba.base.PageView;
import com.ba.base.SimpleModel;
import com.ba.dto.DictionaryPage;
import com.ba.model.system.Dictionary;
import com.ba.response.ResData;
import com.ba.enums.OperationEnum;
import com.ba.uid.impl.CachedUidGenerator;
import com.ba.util.BeanUtils;
import com.ba.vo.OrgManagementVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import com.ba.dto.OrgManagementPageDTO;
import com.ba.model.system.OrgManagement;
import com.ba.service.OrgManagementService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.springframework.http.codec.ServerSentEvent.builder;

/**
 * @Author: barryw
 * @Description: 组织管理
 * @Date: 2024/2/29 11:14:38
 */
@RestController
@RequestMapping("/orgManagement")
@Slf4j
public class OrgManagementController extends BaseController implements BaseCommonController<OrgManagement, OrgManagementPageDTO> {

    private static final String BUSINESS = "组织管理";
    private static final String MENU_CODE = "org:management";

    @Resource
    private OrgManagementService orgManagementService;

    @Resource
    private CachedUidGenerator uidGenerator;

    /**
     * 查询树
     */
    @Permission(menuFlag = MENU_CODE, perms = OperationEnum.VIEW)
    @PostMapping("findTree")
    public ResData findTree(@RequestBody OrgManagementPageDTO dto) {
        Map<String, Object> queryMap = this.queryCondition(dto);
        List<OrgManagement> orgManagements = orgManagementService.findList(null);
        List<OrgManagementVO> list = BeanUtils.convertListTo(orgManagements, OrgManagementVO::new);
        return ResData.success(builder(list));
    }

    @Permission(menuFlag = MENU_CODE, perms = OperationEnum.VIEW)
    @GetMapping("findById")
    @Override
    public ResData findById(@RequestParam Long modelId) {
        return ResData.success(orgManagementService.findById(modelId));
    }

    @Log(business = BUSINESS, operationType = OperationEnum.ADD)
    @Permission(menuFlag = MENU_CODE, perms = OperationEnum.ADD)
    @PostMapping("add")
    @Override
    public ResData add(@RequestBody OrgManagement model) {
        // 封装数据
        if (model.getParentId() == null) {
            model.setParentId(-1L);
        }
        model.setId(uidGenerator.getUID());
        int result = orgManagementService.add(model);
        if (result > 0) {
            return ResData.success();
        }
        return ResData.error("保存失败!");
    }

    @Log(business = BUSINESS, operationType = OperationEnum.EDIT)
    @Permission(menuFlag = MENU_CODE, perms = OperationEnum.EDIT)
    @PostMapping("edit")
    @Override
    public ResData edit(@RequestBody OrgManagement model) {
        int result = orgManagementService.edit(model);
        if (result > 0) {
            return ResData.success();
        }
        return ResData.error("保存失败!");
    }

    @Permission(menuFlag = MENU_CODE, perms = OperationEnum.VIEW)
    @PostMapping("findPage")
    @Override
    public ResData findPage(@RequestBody OrgManagementPageDTO dto) {
        Map<String, Object> queryMap = this.queryCondition(dto);

        PageView<OrgManagement> pageList = orgManagementService.findPage(queryMap);
        return ResData.success(pageList);
    }

    private Map<String, Object> queryCondition(OrgManagementPageDTO dto) {
        Map<String, Object> queryMap = dto.toPageMap();
        return queryMap;
    }

    @Log(business = BUSINESS, operationType = OperationEnum.DELETE)
    @Permission(menuFlag = MENU_CODE, perms = OperationEnum.DELETE)
    @GetMapping("deleteById")
    @Override
    public ResData deleteById(@RequestParam Long modelId) {
        SimpleModel model = new SimpleModel();
        model.setModelId(modelId);
        int result = orgManagementService.deleteBySm(model);
        if (result > 0) {
            return ResData.success();
        }
        return ResData.error("删除失败！");
    }
    
    /**
     * 构建树型结构
     */
    private List<OrgManagementVO> builder(List<OrgManagementVO> nodes) {
        List<OrgManagementVO> treeNodes = new ArrayList<>();
        for (OrgManagementVO n1 : nodes) {
            // -1 代表根节点(顶级父节点)
            if (n1.getParentId() == -1L) {
                treeNodes.add(n1);
            }
            for (OrgManagementVO n2 : nodes) {
                if (n2.getParentId().equals(n1.getId())) {
                    n1.getChildren().add(n2);
                }
            }
        }
        return treeNodes;
    }
    
}
