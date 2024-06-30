package com.ba.controller;

import cn.hutool.core.collection.CollUtil;
import com.ba.annotation.Log;
import com.ba.annotation.Permission;
import com.ba.base.*;
import com.ba.model.system.SysMenu;
import com.ba.response.ResData;
import com.ba.enums.OperationEnum;
import com.ba.uid.impl.CachedUidGenerator;
import com.ba.util.BeanUtils;
import com.ba.vo.OptionVO;
import com.ba.vo.SysMenuVO;
import com.ba.vo.ThinkingMemoVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import com.ba.dto.ThinkingMemoPageDTO;
import com.ba.model.system.ThinkingMemo;
import com.ba.service.ThinkingMemoService;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: generator
 * @Description: 备忘录管理
 * @Date: 2024/6/27 15:25:25
 */
@RestController
@RequestMapping("/thinkingMemo")
@Slf4j
public class ThinkingMemoController extends BaseController implements BaseCommonController<ThinkingMemo, ThinkingMemoPageDTO> {

    private static final String BUSINESS = "备忘录管理";
    private static final String MENU_CODE = "ThinkingMemo";

    @Resource
    private ThinkingMemoService thinkingMemoService;

    @Resource
    private CachedUidGenerator uidGenerator;

    @Permission(menuFlag = MENU_CODE, perms = OperationEnum.VIEW)
    @GetMapping("findById")
    @Override
    public ResData findById(@RequestParam Long modelId) {
        return ResData.success(thinkingMemoService.findById(modelId));
    }

    @Log(business = BUSINESS, operationType = OperationEnum.ADD)
    @Permission(menuFlag = MENU_CODE, perms = OperationEnum.ADD)
    @PostMapping("add")
    @Override
    public ResData add(@RequestBody ThinkingMemo model) {
        // 封装数据
        if (model.getPid() == null) {
            model.setPid(-1L);
        }
        model.setId(uidGenerator.getUID());
        model.setOwnerId(getCurrentUserId());
        model.setLastTime(getCurrentDateStr());
        int result = thinkingMemoService.add(model);
        if (result > 0) {
            return ResData.success();
        }
        return ResData.error("保存失败!");
    }

    @Log(business = BUSINESS, operationType = OperationEnum.EDIT)
    @Permission(menuFlag = MENU_CODE, perms = OperationEnum.EDIT)
    @PostMapping("edit")
    @Override
    public ResData edit(@RequestBody ThinkingMemo model) {
        model.setLastTime(getCurrentDateStr());
        int result = thinkingMemoService.edit(model);
        if (result > 0) {
            return ResData.success();
        }
        return ResData.error("保存失败!");
    }

    @Permission(menuFlag = MENU_CODE, perms = OperationEnum.VIEW)
    @PostMapping("findPage")
    @Override
    public ResData findPage(@RequestBody ThinkingMemoPageDTO dto) {
        Map<String, Object> queryMap = this.queryCondition(dto);

        PageView<ThinkingMemo> pageList = thinkingMemoService.findPage(queryMap);
        return ResData.success(pageList);
    }

    @Permission(menuFlag = MENU_CODE, perms = OperationEnum.VIEW)
    @GetMapping("findTree")
    public ResData findTree() {
        UserInfo currentUser = UserContext.getUserInfo();
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("ownerId", currentUser.getId());
        List<ThinkingMemo> memos = thinkingMemoService.findList(queryMap);
        List<ThinkingMemoVO> memoVOS = BeanUtils.convertListTo(memos, ThinkingMemoVO::new);
        if (CollectionUtils.isEmpty(memoVOS)) {
            return ResData.success();
        }
        // 构建树形结构
        return ResData.success(builderTree(memoVOS));
    }

    private Map<String, Object> queryCondition(ThinkingMemoPageDTO dto) {
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
        int result = thinkingMemoService.deleteBySm(model);
        if (result > 0) {
            return ResData.success();
        }
        return ResData.error("删除失败！");
    }

    /**
     * 过滤出根节点
     */
    private List<ThinkingMemoVO> builderTree(List<ThinkingMemoVO> nodes) {
        return nodes.stream()
                .filter(n -> n.getPid() == -1L)
                .peek(pn -> pn.setChildren(builderChildren(pn, nodes)))
                .collect(Collectors.toList());
    }

    /**
     * 构建子节点
     */
    private List<ThinkingMemoVO> builderChildren(ThinkingMemoVO parentNode, List<ThinkingMemoVO> nodes) {
        return nodes.stream()
                .filter(n -> parentNode.getId().equals(n.getPid()))
                .peek(pn -> pn.setChildren(builderChildren(pn, nodes)))
                .collect(Collectors.toList());
    }

//    /**
//     * 构建树形结构
//     */
//    private List<ThinkingMemoVO> builderTree(List<ThinkingMemoVO> nodes) {
//        List<ThinkingMemoVO> treeNodes = new ArrayList<>();
//        for (ThinkingMemoVO n1 : nodes) {
//            n1.setTreeId(n1.getId().toString());
//            // -1 代表根节点(顶级父节点)
//            if (n1.getPid() == -1L) {
//                treeNodes.add(n1);
//            }
//            for (ThinkingMemoVO n2 : nodes) {
//                n2.setTreeId(n2.getId().toString());
//                if (n2.getPid().equals(n1.getId())) {
//                    n1.getChildren().add(n2);
//                }
//            }
//        }
//        return treeNodes;
//    }

}
