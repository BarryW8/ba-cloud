package com.ba.controller;

import com.ba.base.BaseCommonController;
import com.ba.base.BaseController;
import com.ba.base.PageView;
import com.ba.base.SimpleModel;
import com.ba.dto.SysRoleDTO;
import com.ba.dto.SysRolePage;
import com.ba.model.system.SysRole;
import com.ba.model.system.SysRoleMenu;
import com.ba.service.SysRoleService;
import com.ba.service.SysUserService;
import com.ba.response.ResData;
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
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sysRole")
@Slf4j
public class SysRoleController extends BaseController implements BaseCommonController<SysRole, SysRolePage> {

    @Resource
    private SysRoleService sysRoleService;

    @Resource
    private SysUserService sysUserService;

//    @PostMapping("saveRoleUser")
//    public ResData saveRoleUser(@RequestBody SysUserRoleDTO dto) {
//        Long roleId = dto.getRoleId();
//        if (roleId == null) {
//            return ResData.error("角色不能为空");
//        }
//        dto.setCurrentUser(getCurrentUser());
//        dto.setCurrentDate(getCurrentDate());
//        // 1. 查询用户-角色关联信息，用于刷新缓存（前置用于删除缓存）
//        List<SysUserRoleVO> oldUserRoles = sysRoleService.findRoleUser(roleId);
//        // 2. 保存用户-角色关联信息（不用判断是否成功，支持保存空值）
//        sysRoleService.saveRoleUser(dto);
//        if (!CollectionUtils.isEmpty(oldUserRoles)) {
//            // 3. 刷新系统用户缓存
//            List<Long> userIds = oldUserRoles.stream().map(SysUserRoleVO::getUserId).distinct().collect(Collectors.toList());
//            sysUserService.setUserCache(userIds, 1);
//        }
//        return ResData.success();
//    }
//
//    /**
//     * 查询用户授权角色信息
//     */
//    @GetMapping("findRoleUser")
//    public ResData findRoleUser(@RequestParam Long modelId) {
//        List<SysUserRoleVO> list = sysRoleService.findRoleUser(modelId);
//        return ResData.success(list);
//    }

    @GetMapping("findById")
    @Override
    public ResData findById(@RequestParam Long modelId) {
        // 1. 获取角色详情
        SysRole sysRole = sysRoleService.findById(modelId);
        if (sysRole == null) {
            return ResData.error("角色不存在!");
        }
        // 2. 获取角色菜单权限
        List<String> permList = new ArrayList<>();
        List<SysRoleMenu> list = sysRoleService.findRoleMenu(modelId);
        if (!CollectionUtils.isEmpty(list)) {
            for (SysRoleMenu roleMenu : list) {
                String perm = roleMenu.getPermission();
                if (StringUtils.isNotEmpty(perm)){
                    if (perm.contains(",")) {
                        String[] split = perm.split(",");
                        for (String s : split) {
                            permList.add(roleMenu.getMenuId() + "-" + s);
                        }
                    } else {
                        permList.add(roleMenu.getMenuId() + "-" + perm);
                    }
                } else {
                    permList.add(roleMenu.getMenuId().toString());
                }
            }
        }
        // 3. 封装返回参数
        Map<String, Object> map = new HashMap<>();
        map.put("roleInfo", sysRole);
        map.put("permList", permList);
        return ResData.success(map);
    }

    @Override
    public ResData save(SysRole sysRole) {
        return null;
    }

    @PostMapping("save")
    public ResData save(@RequestBody @Valid SysRoleDTO dto) {
        int result = sysRoleService.saveDTO(dto);
        if (result > 0) {
            // TODO 刷新缓存
            return ResData.success();
        }
        return ResData.error("保存失败!");
    }

    @PostMapping("findPage")
    @Override
    public ResData findPage(@RequestBody SysRolePage dto) {
        Map<String, Object> queryMap = this.queryCondition(dto);
        PageView<SysRole> pageList = sysRoleService.findPage(queryMap);
        return ResData.success(pageList);
    }

    private Map<String, Object> queryCondition(SysRolePage dto) {
        Map<String, Object> queryMap = dto.toPageMap();
//        if (dto.getStatus() != null) {
//            sqlBf.append(" and status =").append(dto.getStatus());
//        }
//        if (StringUtils.isNotEmpty(keyword)) {
//            sqlBf.append(" and ( role_name like '%").append(keyword).append("%'").append(")");
//        }
        return queryMap;
    }

    @GetMapping("deleteById")
    @Override
    public ResData deleteById(@RequestParam Long modelId) {
        SimpleModel simpleModel = new SimpleModel();
        simpleModel.setModelId(modelId);
        simpleModel.setDelUser(getCurrentUserId());
        simpleModel.setDelDate(getCurrentDateStr());
        int result = sysRoleService.deleteBySm(simpleModel);
        if (result > 0) {
            return ResData.success();
        }
        return ResData.error("删除失败!");
    }
}
