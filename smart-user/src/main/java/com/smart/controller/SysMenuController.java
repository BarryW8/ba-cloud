package com.smart.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DatePattern;
import com.smart.base.BaseCommonController;
import com.smart.base.BaseController;
import com.smart.base.BasePageDTO;
import com.smart.base.SimpleModel;
import com.smart.dto.DictionaryPageDTO;
import com.smart.dto.SysMenuPageDTO;
import com.smart.model.LoginUser;
import com.smart.model.user.Dictionary;
import com.smart.model.user.SysMenu;
import com.smart.model.user.SysRoleMenu;
import com.smart.service.DictionaryService;
import com.smart.service.SysMenuService;
import com.smart.service.SysRoleMenuService;
import com.smart.service.SysRoleService;
import com.smart.uid.impl.CachedUidGenerator;
import com.smart.util.JsonData;
import com.smart.vo.DictionaryVO;
import com.smart.vo.OptionVO;
import com.smart.vo.SysMenuVO;
import com.smart.vo.UserInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
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
public class SysMenuController extends BaseController implements BaseCommonController<SysMenu, SysMenuPageDTO> {

    @Resource
    private SysMenuService sysMenuService;

    @Resource
    private CachedUidGenerator uidGenerator;

    @Resource
    private SysRoleService sysRoleService;

    @Resource
    private DictionaryService dictionaryService;

    @GetMapping("findById")
    @Override
    public JsonData findById(@RequestParam Long modelId) {
        SysMenu sysMenu = sysMenuService.findById(modelId);
        if (sysMenu == null) {
            return JsonData.buildError("菜单不存在！");
        }
        return JsonData.buildSuccess(sysMenu);
    }

    @PostMapping("save")
    @Override
    public JsonData save(@RequestBody SysMenu sysMenu) {
        LoginUser loginUser = getCurrentUser();
        // 封装数据
        if (sysMenu.getParentId() == null) {
            sysMenu.setParentId(-1L);
        }
        int result;
        if (sysMenu.getId() == null) {
            sysMenu.setId(uidGenerator.getUID());
            sysMenu.setCreateUserId(loginUser.getUserId());
            sysMenu.setCreateUserName(loginUser.getRealName());
            sysMenu.setCreateTime(getCurrentDate());
            result = sysMenuService.save(sysMenu);
        } else {
            //编辑
            sysMenu.setUpdateUserId(loginUser.getUserId());
            sysMenu.setUpdateUserName(loginUser.getRealName());
            sysMenu.setUpdateTime(getCurrentDate());
            result = sysMenuService.update(sysMenu);
        }
        if (result > 0) {
            return JsonData.buildSuccess();
        }
        return JsonData.buildError("保存失败!");
    }

    @PostMapping("findPage")
    @Override
    public JsonData findPage(@RequestBody SysMenuPageDTO dto) {
        return null;
    }

    @GetMapping("deleteById")
    @Override
    public JsonData deleteById(@RequestParam Long modelId) {
        LoginUser loginUser = getCurrentUser();
        SimpleModel simpleModel = new SimpleModel();
        simpleModel.setModelId(modelId);
        simpleModel.setDelUser(loginUser.getUserId());
        simpleModel.setDelUserName(loginUser.getRealName());
        simpleModel.setDelDate(getCurrentDate());
        int result = sysMenuService.deleteBySm(simpleModel);
        if (result > 0) {
            return JsonData.buildSuccess();
        }
        return JsonData.buildError("删除失败!");
    }

    @GetMapping("findAllList")
    public JsonData findAllList() {
        List<SysMenuVO> menus = sysMenuService.findAllList();
        return JsonData.buildSuccess(builder(menus));
    }

    @PostMapping("getList")
    public JsonData getList(@RequestBody SysMenuPageDTO dto) {
        String condition = this.queryCondition(dto);
        List<SysMenu> list = sysMenuService.findList(condition);
        List<SysMenuVO> voList = new ArrayList<>();
        for (SysMenu menu : list) {
            SysMenuVO vo = new SysMenuVO();
            BeanUtil.copyProperties(menu, vo);
            voList.add(vo);
        }
        return JsonData.buildSuccess(builder(voList));
    }

    private String queryCondition(SysMenuPageDTO dto) {
        String keyword = dto.getKeyword();
        Integer isHide = dto.getIsHide();
        Integer linkType = dto.getLinkType();

        StringBuilder sqlBf = new StringBuilder();
        if (isHide != null) {
            sqlBf.append(" and is_hide = ").append(isHide);
        }
        if (linkType != null) {
            sqlBf.append(" and link_type = ").append(linkType);
        }
        if (StringUtils.isNotEmpty(keyword)) {
            sqlBf.append(" and (code like '%").append(keyword).append("%'")
                .append(" or name like '%").append(keyword).append("%'").append(")");
        }
        return sqlBf.toString();
    }

    private List<SysMenuVO> builder(List<SysMenuVO> nodes) {
        List<SysMenuVO> treeNodes = new ArrayList<>();
        for (SysMenuVO n1 : nodes) {
            n1.setKey(n1.getId());
            // -1 代表根节点(顶级父节点)
            if ("-1".equals(n1.getParentId())) {
                treeNodes.add(n1);
            }
            for (SysMenuVO n2 : nodes) {
                if (n2.getParentId().equals(n1.getId())) {
                    n1.getChildren().add(n2);
                }
            }
            if(CollectionUtils.isEmpty(n1.getChildren())){
                n1.setChildren(null);
            }
        }
        return treeNodes;
    }

    @GetMapping("findMenuList")
    public JsonData findMenuList() {
        List<SysMenuVO> menus = new ArrayList<>();
//        List<SysMenuVO> allMenus = cacheManage.getSysMenu();
        List<SysMenuVO> allMenus = sysMenuService.findAllList();
        if (CollectionUtils.isEmpty(allMenus)) {
            return JsonData.buildSuccess(menus);
        }
        LoginUser currentUser = getCurrentUser();
        // 如果不是超管
        if (StringUtils.isNotEmpty(currentUser.getUserCode())) {
            // 查询角色绑定菜单信息
            List<SysRoleMenu> roleMenus = sysRoleService.findRoleMenu(currentUser.getRoleId());
            if (!CollectionUtils.isEmpty(roleMenus)) {
                //处理菜单树结构，得到最终结果
                List<Long> menuIds = roleMenus.stream().map(SysRoleMenu::getMenuId).collect(Collectors.toList());
                Map<Long, String> menuPerms = roleMenus.stream().collect(Collectors.toMap(SysRoleMenu::getMenuId, SysRoleMenu::getMenuAction));
                Map<Long, SysMenuVO> menuAllMap = new HashMap<>(16);
                List<Long> menuIdList = new ArrayList<>();
                List<SysMenuVO> resultList = new ArrayList<>();
                // 将菜单放入map中方便取值
                for (SysMenuVO menu : allMenus) {
                    menuAllMap.put(Long.valueOf(menu.getId()), menu);
                    menuIdList.add(Long.valueOf(menu.getId()));
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
        return JsonData.buildSuccess(builderMenu(allMenus, authList));
    }

    @GetMapping("findTreeMenuList")
    public JsonData findTreeMenuList() {
        List<SysMenuVO> menus = new ArrayList<>();
//        List<SysMenuVO> allMenus = cacheManage.getSysMenu();
        List<SysMenuVO> allMenus = sysMenuService.findAllList();
        if (CollectionUtils.isEmpty(allMenus)) {
            return JsonData.buildSuccess(menus);
        }
        LoginUser currentUser = getCurrentUser();

        // 如果不是超管
//        if (currentUser.isManager()) {
//            // 查询角色绑定菜单信息
//            List<SysRoleMenu> roleMenus = sysRoleMenuService.findList(" and role_id='" + currentUser.getRoleId() + "'");
//            if (!CollectionUtils.isEmpty(roleMenus)) {
//                //处理菜单树结构，得到最终结果
//                List<Long> menuIds = roleMenus.stream().map(SysRoleMenu::getMenuId).collect(Collectors.toList());
//                Map<Long, String> menuPerms = roleMenus.stream().collect(Collectors.toMap(SysRoleMenu::getMenuId, SysRoleMenu::getMenuAction));
//                Map<Long, SysMenuVO> menuAllMap = new HashMap<>(16);
//                List<Long> menuIdList = new ArrayList<>();
//                List<SysMenuVO> resultList = new ArrayList<>();
//                // 将菜单放入map中方便取值
//                for (SysMenuVO menu : allMenus) {
//                    menuAllMap.put(Long.valueOf(menu.getId()), menu);
//                    menuIdList.add(Long.valueOf(menu.getId()));
//                }
//                //取交集，防止all菜单被删，角色菜单表中未及时更新，产生脏数据
//                menuIds.retainAll(menuIdList);
//                for (Long menuId : menuIds) {
//                    resultMenus(menuId, menuAllMap, menuPerms, resultList);
//                }
//                //菜单排序
//                allMenus = resultList.stream().sorted(Comparator.comparing(SysMenuVO::getOrderBy)).collect(Collectors.toList());
//            }
//        }
        // 过滤隐藏状态的菜单
//        for (SysMenuVO vo : allMenus) {
//            if (vo.getIsHide() == 0) {
//                menus.add(vo);
//            }
//        }
        List<OptionVO> authList = dictionaryService.optionList("btnPermissions");
        return JsonData.buildSuccess(builderMenu(allMenus, authList));
    }

    private List<SysMenuVO> builderMenu(List<SysMenuVO> nodes, List<OptionVO> auths) {
        List<SysMenuVO> treeNodes = new ArrayList<>();
        for (SysMenuVO n1 : nodes) {
            // -1 代表根节点(顶级父节点)
            if ("-1".equals(n1.getParentId())) {
                treeNodes.add(n1);
            }
            for (SysMenuVO n2 : nodes) {
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
                            vo.setId(n1.getId() + "-" + option.getCode());
                            vo.setLabel(option.getName());
                            n1.getChildren().add(vo);
                        }
                    }
                }
            }
        }
        return treeNodes;
    }


    public void resultMenus(Long id, Map<Long, SysMenuVO> menuMap, Map<Long, String> menuPerms, List<SysMenuVO> list) {
        SysMenuVO dto = menuMap.get(id);
        if (dto == null) {
            return;
        }
        boolean flag = false;
        for (SysMenuVO tree : list) {
            if (tree.getId().equals(dto.getId())) {
                flag = true;
                break;
            }
        }
        if (!flag) {
            String realPerms = dto.getPerms();
            String ownPerms = menuPerms.get(Long.valueOf(dto.getId()));
            dto.setPerms(dealPerms(realPerms, ownPerms));
            list.add(dto);
        }
        if (!"-1".equals(dto.getParentId())) {
            resultMenus(Long.valueOf(dto.getParentId()), menuMap, menuPerms, list);
        }
    }

    private String dealPerms(String realPerms, String ownPerms) {
        if (org.springframework.util.StringUtils.isEmpty(realPerms)
                || org.springframework.util.StringUtils.isEmpty(ownPerms)) {
            return "";
        }
        List<String> realList = Arrays.stream(realPerms.split(",")).collect(Collectors.toList());
        List<String> ownList = Arrays.stream(ownPerms.split(",")).collect(Collectors.toList());
        ownList.retainAll(realList);
        return String.join(",", ownList);
    }
}
