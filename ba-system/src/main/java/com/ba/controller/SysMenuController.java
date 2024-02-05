package com.ba.controller;

import com.ba.base.BaseCommonController;
import com.ba.base.BaseController;
import com.ba.annotation.Permission;
import com.ba.base.SimpleModel;
import com.ba.base.UserContext;
import com.ba.base.UserInfo;
import com.ba.cache.CacheManage;
import com.ba.dto.SysMenuPage;
import com.ba.enums.OperationEnum;
import com.ba.model.system.SysMenu;
import com.ba.model.system.SysRoleMenu;
import com.ba.response.ResData;
import com.ba.service.DictionaryService;
import com.ba.service.SysMenuService;
import com.ba.service.SysRoleService;
import com.ba.uid.impl.CachedUidGenerator;
import com.ba.util.BeanUtils;
import com.ba.vo.OptionVO;
import com.ba.vo.SysMenuVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/sysMenu")
@Slf4j
public class SysMenuController extends BaseController implements BaseCommonController<SysMenu, SysMenuPage> {

    private static final String MENU_CODE = "system:menu";

    @Resource
    private SysMenuService sysMenuService;

    @Resource
    private CacheManage cacheManage;

    @Resource
    private CachedUidGenerator uidGenerator;

    @Resource
    private SysRoleService sysRoleService;

    @Resource
    private DictionaryService dictionaryService;

    @Permission(menuFlag = MENU_CODE, perms = OperationEnum.VIEW)
    @GetMapping("findById")
    @Override
    public ResData findById(@RequestParam Long modelId) {
        SysMenu sysMenu = sysMenuService.findById(modelId);
        if (sysMenu == null) {
            return ResData.error("菜单不存在！");
        }
        return ResData.success(sysMenu);
    }

    @Permission(menuFlag = MENU_CODE, perms = OperationEnum.ADD)
    @PostMapping("add")
    @Override
    public ResData add(@RequestBody SysMenu model) {
        // 封装数据
        if (model.getParentId() == null) {
            model.setParentId(-1L);
        }
        model.setId(uidGenerator.getUID());
        int result = sysMenuService.add(model);
        if (result > 0) {
            // 刷新缓存
            Map<String, Object> menuMap = sysMenuService.findTree(null);
            cacheManage.setSysMenu(menuMap);
            return ResData.success();
        }
        return ResData.error("保存失败!");
    }

    @Permission(menuFlag = MENU_CODE, perms = OperationEnum.EDIT)
    @PostMapping("edit")
    @Override
    public ResData edit(@RequestBody SysMenu model) {
        // 封装数据
        if (model.getParentId() == null) {
            model.setParentId(-1L);
        }
        int result = sysMenuService.edit(model);
        if (result > 0) {
            // 刷新缓存
            Map<String, Object> menuMap = sysMenuService.findTree(null);
            cacheManage.setSysMenu(menuMap);
            return ResData.success();
        }
        return ResData.error("保存失败!");
    }

    @Permission(menuFlag = MENU_CODE, perms = OperationEnum.VIEW)
    @PostMapping("findPage")
    @Override
    public ResData findPage(@RequestBody SysMenuPage dto) {
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
        int result = sysMenuService.deleteBySm(simpleModel);
        if (result > 0) {
            // 刷新缓存
            Map<String, Object> menuMap = sysMenuService.findTree(null);
            cacheManage.setSysMenu(menuMap);
            return ResData.success();
        }
        return ResData.error("删除失败!");
    }

    /**
     * 查询树
     */
    @Permission(menuFlag = MENU_CODE, perms = OperationEnum.VIEW)
    @PostMapping("findTree")
    public ResData findTree(@RequestBody SysMenuPage dto) {
        Map<String, Object> resultMap = sysMenuService.findTree(this.queryCondition(dto));
        return ResData.success(resultMap);
    }

    private Map<String, Object> queryCondition(SysMenuPage dto) {
        Map<String, Object> queryMap = dto.toPageMap();
        return queryMap;
//        String keyword = dto.getKeyword();
//        Integer isHide = dto.getIsHide();
//        Integer linkType = dto.getLinkType();
//
//        StringBuilder sqlBf = new StringBuilder();
//        if (isHide != null) {
//            sqlBf.append(" and is_hide = ").append(isHide);
//        }
//        if (linkType != null) {
//            sqlBf.append(" and link_type = ").append(linkType);
//        }
//        if (StringUtils.isNotEmpty(keyword)) {
//            sqlBf.append(" and (code like '%").append(keyword).append("%'")
//                .append(" or name like '%").append(keyword).append("%'").append(")");
//        }
//        return sqlBf.toString();
    }

    /**
     * 构建树型结构
     */
    private List<SysMenuVO> builder(List<SysMenuVO> nodes) {
        List<SysMenuVO> treeNodes = new ArrayList<>();
        for (SysMenuVO n1 : nodes) {
            // 判断是否为根节点
            if (n1.getParentId() == -1L) {
                treeNodes.add(n1);
            }
            for (SysMenuVO n2 : nodes) {
                if (n1.getId().equals(n2.getParentId())) {
                    n1.getChildren().add(n2);
                }
            }
        }
        return treeNodes;
    }

    /**
     * 查询树-构建按钮权限
     */
    @Permission(menuFlag = MENU_CODE, perms = OperationEnum.VIEW)
    @GetMapping("findTreePerms")
    public ResData findTreePerms() {
//        List<SysMenu> menus = cacheManage.getSysMenu();
        List<SysMenu> menus = sysMenuService.findList(null);
        List<SysMenuVO> allMenus = BeanUtils.convertListTo(menus, SysMenuVO::new);
        if (CollectionUtils.isEmpty(allMenus)) {
            return ResData.success();
        }
        UserInfo currentUser = UserContext.getUserInfo();
        // 如果不是超管
        if (!currentUser.isSuperManager()) {
            // 查询角色绑定菜单信息
            List<SysRoleMenu> roleMenus = sysRoleService.findRoleMenu(currentUser.getRoleId());
            if (CollectionUtils.isNotEmpty(roleMenus)) {
                //处理菜单树结构，得到最终结果
                List<Long> menuIds = roleMenus.stream().map(SysRoleMenu::getMenuId).collect(Collectors.toList());
                Map<Long, String> menuPerms = roleMenus.stream().collect(Collectors.toMap(SysRoleMenu::getMenuId, SysRoleMenu::getPermission));
                Map<Long, SysMenuVO> menuAllMap = new HashMap<>(16);
                List<Long> menuIdList = new ArrayList<>();
                List<SysMenuVO> resultList = new ArrayList<>();
                // 将菜单放入map中方便取值
                for (SysMenuVO menu : allMenus) {
                    menuAllMap.put(menu.getId(), menu);
                    menuIdList.add(menu.getId());
                }
                //取交集，防止all菜单被删，角色菜单表中未及时更新，产生脏数据
                menuIds.retainAll(menuIdList);
                for (Long menuId : menuIds) {
                    resultMenus(menuId, menuAllMap, menuPerms, resultList);
                }
                //菜单排序
                allMenus = resultList.stream().sorted(Comparator.comparing(SysMenuVO::getOrderBy)).collect(Collectors.toList());
            }
        }
        // 过滤隐藏状态的菜单
//        for (SysMenuVO vo : allMenus) {
//            if (vo.getIsHide() == 0) {
//                menus.add(vo);
//            }
//        }
        List<OptionVO> authList = dictionaryService.optionList("001");
        return ResData.success(builderMenu(allMenus, authList));
    }

    public void resultMenus(Long id, Map<Long, SysMenuVO> menuMap, Map<Long, String> menuPerms, List<SysMenuVO> list) {
        SysMenuVO vo = menuMap.get(id);
        if (vo == null) {
            return;
        }
        boolean flag = false;
        for (SysMenuVO tree : list) {
            if (tree.getId().equals(vo.getId())) {
                flag = true;
                break;
            }
        }
        if (!flag) {
            String realPerms = vo.getPerms();
            String ownPerms = menuPerms.get(vo.getId());
            vo.setPerms(dealPerms(realPerms, ownPerms));
            list.add(vo);
        }
        if (vo.getParentId() != -1L) {
            resultMenus(vo.getParentId(), menuMap, menuPerms, list);
        }
    }

    private String dealPerms(String realPerms, String ownPerms) {
        if (StringUtils.isEmpty(realPerms) || StringUtils.isEmpty(ownPerms)) {
            return "";
        }
        List<String> realList = Arrays.stream(realPerms.split(",")).collect(Collectors.toList());
        List<String> ownList = Arrays.stream(ownPerms.split(",")).collect(Collectors.toList());
        ownList.retainAll(realList);
        return String.join(",", ownList);
    }

    private List<SysMenuVO> builderMenu(List<SysMenuVO> nodes, List<OptionVO> auths) {
        List<SysMenuVO> treeNodes = new ArrayList<>();
        for (SysMenuVO n1 : nodes) {
            n1.setTreeId(n1.getId().toString());
            // -1 代表根节点(顶级父节点)
            if (n1.getParentId() == -1L) {
                treeNodes.add(n1);
            }
            for (SysMenuVO n2 : nodes) {
                n2.setTreeId(n2.getId().toString());
                if (n2.getParentId().equals(n1.getId())) {
                    n1.getChildren().add(n2);
                }
            }
            // 子节点为空、存在按钮权限才添加
            if (CollectionUtils.isEmpty(n1.getChildren()) && StringUtils.isNotEmpty(n1.getPerms())) {
                String[] split = n1.getPerms().split(",");
                List<String> permList = Arrays.asList(split);
                for (String perm : permList) {
                    for (OptionVO option : auths) {
                        if (perm.equals(option.getCode())) {
                            SysMenuVO vo = new SysMenuVO();
                            vo.setId(n1.getId());
                            vo.setTreeId(n1.getId() + "-" + option.getCode());
                            vo.setMenuCode(n1.getMenuCode());
                            vo.setMenuName(option.getName());
                            n1.getChildren().add(vo);
                        }
                    }
                }
            }
        }
        return treeNodes;
    }

}
