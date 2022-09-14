package com.smart.controller;

import cn.hutool.core.date.DatePattern;
import com.smart.base.BaseCommonController;
import com.smart.base.BaseController;
import com.smart.base.PageView;
import com.smart.base.SimpleModel;
import com.smart.dto.SysUserPageDTO;
import com.smart.dto.SysUserRoleDTO;
import com.smart.dto.UserLoginDTO;
import com.smart.enums.BizCodeEnum;
import com.smart.model.LoginUser;
import com.smart.model.user.SysRole;
import com.smart.model.user.SysRoleMenu;
import com.smart.model.user.SysUser;
import com.smart.model.user.SysUserRole;
import com.smart.service.SysMenuService;
import com.smart.service.SysRoleService;
import com.smart.service.SysUserService;
import com.smart.uid.impl.CachedUidGenerator;
import com.smart.util.CommonUtils;
import com.smart.util.JWTUtil;
import com.smart.util.JsonData;
import com.smart.vo.MenuVO;
import com.smart.vo.MetaVO;
import com.smart.vo.SysMenuVO;
import com.smart.vo.SysUserRoleVO;
import com.smart.vo.UserInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/sysUser")
@Slf4j
public class SysUserController extends BaseController implements BaseCommonController<SysUser, SysUserPageDTO> {

    @Resource
    private SysUserService sysUserService;

    @Resource
    private CachedUidGenerator uidGenerator;

    @Resource
    private SysMenuService sysMenuService;

    @Resource
    private SysRoleService sysRoleService;

    @PostMapping("saveUserRole")
    public JsonData saveUserRole(@RequestBody SysUserRoleDTO dto) {
        Long userId = dto.getUserId();
        if (userId == null) {
            return JsonData.buildError("用户不能为空");
        }
        dto.setCurrentUser(getCurrentUser());
        dto.setCurrentDate(getCurrentDate());
        // 1. 查询用户-角色关联信息，用于刷新缓存（前置用于删除缓存）
        List<SysUserRoleVO> oldUserRoles = sysUserService.findUserRole(userId);
        // 2. 保存用户-角色关联信息（不用判断是否成功，支持保存空值）
        sysUserService.saveUserRole(dto);
        if (!CollectionUtils.isEmpty(oldUserRoles)) {
            // 3. 刷新系统用户缓存
            List<Long> userIds = oldUserRoles.stream().map(SysUserRoleVO::getUserId).distinct().collect(Collectors.toList());
            sysUserService.setUserCache(userIds, 1);
        }
        return JsonData.buildSuccess();
    }

    /**
     * 查询用户授权角色信息
     */
    @GetMapping("findUserRole")
    public JsonData findUserRole(@RequestParam Long modelId) {
        List<SysUserRoleVO> list = sysUserService.findUserRole(modelId);
        return JsonData.buildSuccess(list);
    }

    @GetMapping("findById")
    @Override
    public JsonData findById(@RequestParam Long modelId) {
        return JsonData.buildSuccess(sysUserService.findById(modelId));
    }

    @PostMapping("save")
    @Override
    public JsonData save(@RequestBody SysUser sysUser) {
        LoginUser loginUser = getCurrentUser();
        int result;
        if (sysUser.getId() == null) {
            // 初始化密码
            String defaultPassword = "123456";
            sysUser.setPassword(CommonUtils.MD5Lower(CommonUtils.MD5Lower(defaultPassword)));
            sysUser.setId(uidGenerator.getUID());
            sysUser.setCreateUserId(loginUser.getUserId());
            sysUser.setCreateUserName(loginUser.getRealName());
            sysUser.setCreateTime(getCurrentDate());
            result = sysUserService.save(sysUser);
        } else {
            //编辑
            sysUser.setUpdateUserId(loginUser.getUserId());
            sysUser.setUpdateUserName(loginUser.getRealName());
            sysUser.setUpdateTime(getCurrentDate());
            result = sysUserService.update(sysUser);
        }
        if (result > 0) {
            return JsonData.buildSuccess();
        }
        return JsonData.buildError("保存失败!");
    }

    @PostMapping("findPage")
    @Override
    public JsonData findPage(@RequestBody SysUserPageDTO dto) {
        String condition = this.queryCondition(dto);
        PageView<SysUser> pageList = sysUserService.findPage(dto.getPageNum(), dto.getPageSize(), condition, null);
        return JsonData.buildSuccess(pageList);
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
        int result = sysUserService.deleteBySm(simpleModel);
        if (result > 0) {
            return JsonData.buildSuccess();
        }
        return JsonData.buildError("删除失败!");
    }

    @PostMapping("login")
    public JsonData login(@RequestBody @Valid UserLoginDTO dto) {
        //-----------a、查询用户信息
        List<SysUser> userList = sysUserService.findListHasPwd(" and user_name = '" + dto.getUsername() + "'");
        if (CollectionUtils.isEmpty(userList)) {
            // 未注册
            return JsonData.buildResult(BizCodeEnum.ACCOUNT_UNREGISTER);
        }
        if (userList.size() != 1) {
            // 账号异常
            return JsonData.buildResult(BizCodeEnum.ACCOUNT_EXCEPTION);
        }
        // 已注册
        SysUser user = userList.get(0);
        if (user.getUserStatus() != 0) {
            // 账号已停用
            return JsonData.buildResult(BizCodeEnum.ACCOUNT_STOP_USING);
        }
        //-----------b、校验密码
        if (!dto.getPassword().equals(user.getPassword())) {
            // 账号或者密码错误
            return JsonData.buildResult(BizCodeEnum.ACCOUNT_PWD_ERROR);
        }
        //-----------c、系统用户缓存刷新
        List<Long> userIds = new ArrayList<>();
        userIds.add(user.getId());
        LoginUser loginUser = sysUserService.setUserCache(userIds, 0);
        //-----------d、生成token
        String accessToken = JWTUtil.geneJsonWebToken(loginUser);
        //-----------e、修改登录次数
        int loginNum = user.getLoginNum() + 1;
        user.setLoginNum(loginNum);
        user.setLoginTime(getCurrentDate());
        sysUserService.update(user);
        return JsonData.buildSuccess(accessToken);
    }

    /**
     * 登录后，获取系统用户信息
     * （前端每次刷新页面时都会调用该方法，因此方法内最好不要查数据库）
     */
    @GetMapping("userInfo")
    public JsonData userInfo() {
        UserInfoVO userInfo = new UserInfoVO();
        //1. 获取当前登陆的user（拦截器中已查询用户缓存，无需再次查询缓存）
        LoginUser currentUser = getCurrentUser();
        BeanUtils.copyProperties(currentUser, userInfo);
        //2. 查询客户登录logo和登录标题 TODO
        //3. 获取全部菜单（查缓存）
//        List<SysMenuVO> menus = cacheManage.getSysMenu();
//        if (CollectionUtils.isEmpty(menus)) {
//            // 如果缓存中没有，则查库，刷新缓存
//            menus = sysMenuService.findAllList();
//            cacheManage.setSysMenu(menus);
//        }
        List<SysMenuVO> menus = sysMenuService.findAllList();
        if (StringUtils.isEmpty(currentUser.getTelephone())) {
            userInfo.setMenuList(builder(menus));
        } else {
            //5. 查询角色绑定菜单信息
            List<SysRoleMenu> roleMenus = sysRoleService.findRoleMenu(currentUser.getRoleId());
            if (!CollectionUtils.isEmpty(roleMenus)) {
                //处理菜单树结构，得到最终结果
                List<Long> menuIds = roleMenus.stream().map(SysRoleMenu::getMenuId).collect(Collectors.toList());
                Map<Long, String> menuPerms = roleMenus.stream().collect(Collectors.toMap(SysRoleMenu::getMenuId, SysRoleMenu::getMenuAction));
                Map<Long, SysMenuVO> menuAllMap = new HashMap<>(16);
                List<Long> menuIdList = new ArrayList<>();
                List<SysMenuVO> resultList = new ArrayList<>();
                // 将菜单放入map中方便取值
                for (SysMenuVO menu : menus) {
                    menuAllMap.put(Long.valueOf(menu.getId()), menu);
                    menuIdList.add(Long.valueOf(menu.getId()));
                }
                //取交集，防止all菜单被删，角色菜单表中未及时更新，产生脏数据
                menuIds.retainAll(menuIdList);
                for (Long menuId : menuIds) {
                    resultMenus(menuId, menuAllMap, menuPerms, resultList);
                }
                //菜单排序
                List<SysMenuVO> finalMenus = resultList.stream().sorted(Comparator.comparing(SysMenuVO::getOrderBy)).collect(Collectors.toList());
                //菜单树构建
                userInfo.setMenuList(builder(finalMenus));
            }
        }


//        Long userId = loginUser.getUserId();
//        SysUser user = sysUserService.findById(userId);
//        if (user == null) {
//            log.error("获取 userInfo 异常");
//            return JsonData.buildError("账号不存在");
//        }
//        BeanUtils.copyProperties(user, userInfo);
//        List<SysMenuVO> menuList = sysMenuService.findAllList();
//        List<MenuVO> menuVOS = new ArrayList<>();
//        for (SysMenuVO sysMenuVO:menuList) {
//            MenuVO menuVO = new MenuVO();
//            menuVO.setId(sysMenuVO.getId());
//            menuVO.setParentId(sysMenuVO.getParentId());
//            menuVO.setName(sysMenuVO.getLabel());
//            menuVO.setPath(sysMenuVO.getRoutePath());
//            menuVO.setComponent(sysMenuVO.getPagePath());
//            MetaVO metaVO = new MetaVO();
//            metaVO.setIcon(sysMenuVO.getIconPath());
//            metaVO.setPermission(sysMenuVO.getPerms());
//            String target = sysMenuVO.getLinkType() == 1 ? "_blank":"";
//            metaVO.setTarget(target);
//            metaVO.setTitle(sysMenuVO.getLabel());
//            metaVO.setHideChildren(sysMenuVO.getIsHide() == 1);
//            menuVO.setMeta(metaVO);
//            menuVOS.add(menuVO);
//        }
//        userInfo.setMenuList(menuVOS);
        return JsonData.buildSuccess(userInfo);
    }

    @GetMapping("logout")
    public JsonData logout() {
        // todo 后续再完善
        return JsonData.buildSuccess("登出成功");
    }

    private String queryCondition(SysUserPageDTO dto) {
        String keyword = dto.getKeyword();
        Integer userStatus = dto.getUserStatus();

        StringBuilder sqlBf = new StringBuilder();
        if (userStatus != null) {
            sqlBf.append(" and user_status = ").append(userStatus);
        }
        if (StringUtils.isNotEmpty(keyword)) {
            sqlBf.append(" and (user_name like '%").append(keyword).append("%'")
                .append(" or real_name like '%").append(keyword).append("%'").append(")");
        }
        return sqlBf.toString();
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

    private List<SysMenuVO> builder(List<SysMenuVO> nodes) {
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
        }
        return treeNodes;
    }

}
