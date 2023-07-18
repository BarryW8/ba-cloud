package com.ba.controller;

import cn.hutool.crypto.SecureUtil;
import com.ba.base.BaseCommonController;
import com.ba.base.BaseController;
import com.ba.base.PageView;
import com.ba.base.SimpleModel;
import com.ba.cache.CacheManage;
import com.ba.dto.SysUserPage;
import com.ba.model.system.SysRole;
import com.ba.model.system.SysUser;
import com.ba.model.system.SysUserRole;
import com.ba.service.SysMenuService;
import com.ba.service.SysRoleService;
import com.ba.service.SysUserService;
import com.ba.uid.impl.CachedUidGenerator;
import com.ba.util.BeanUtils;
import com.ba.util.CommonUtils;
import com.ba.response.ResData;
import com.ba.util.StringUtils;
import com.ba.vo.SysUserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/sysUser")
@Slf4j
public class SysUserController extends BaseController implements BaseCommonController<SysUser, SysUserPage> {

    @Resource
    private SysUserService sysUserService;

    @Resource
    private CacheManage cacheManage;

    @Resource
    private CachedUidGenerator uidGenerator;

    @Resource
    private SysMenuService sysMenuService;

    @Resource
    private SysRoleService sysRoleService;

    @GetMapping("findById")
    @Override
    public ResData findById(@RequestParam Long modelId) {
        return ResData.success(sysUserService.findById(modelId));
    }

    @PostMapping("save")
    @Override
    public ResData save(@RequestBody SysUser model) {
        int result;
        if (model.getId() == null) {
            if (StringUtils.isEmpty(model.getPassword())) {
                // 初始化密码
                String defaultPassword = "123456";
                model.setPassword(SecureUtil.md5((SecureUtil.md5(defaultPassword))));
            }
            model.setId(uidGenerator.getUID());
            result = sysUserService.insert(model);
        } else {
            //编辑
            result = sysUserService.update(model);
        }
        if (result > 0) {
            return ResData.success();
        }
        return ResData.error("保存失败!");
    }

    @PostMapping("findPage")
    @Override
    public ResData findPage(@RequestBody SysUserPage dto) {
        Map<String, Object> queryMap = this.queryCondition(dto);
        PageView<SysUser> pageList = sysUserService.findPage(queryMap);

        // 封装角色名称字段
        List<SysUserVO> voList = BeanUtils.convertListTo(pageList.getData(), SysUserVO::new);
        for (SysUserVO vo : voList) {
            SysUserRole userRole = sysUserService.findUserRole(vo.getId());
            if (Objects.isNull(userRole)) continue;
            SysRole role = sysRoleService.findById(userRole.getRoleId());
            if (Objects.isNull(role)) continue;
            vo.setRoleName(role.getRoleName());
        }

        // 封装结果集
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("data", voList);
        resultMap.put("total", pageList.getTotal());
        return ResData.success(resultMap);
    }

    private Map<String, Object> queryCondition(SysUserPage dto) {
        Map<String, Object> queryMap = dto.toPageMap();
//        if (userStatus != null) {
//            sqlBf.append(" and user_status = ").append(userStatus);
//        }
//        if (StringUtils.isNotEmpty(keyword)) {
//            sqlBf.append(" and (user_name like '%").append(keyword).append("%'")
//                    .append(" or real_name like '%").append(keyword).append("%'").append(")");
//        }
        return queryMap;
    }

    @GetMapping("deleteById")
    @Override
    public ResData deleteById(@RequestParam Long modelId) {
        SysUser model = new SysUser();
        model.setId(modelId);
        int result = sysUserService.deleteById(model);
        if (result > 0) {
            return ResData.success();
        }
        return ResData.error("删除失败!");
    }

    /**
     * 保存用户授权角色信息
     */
//    @Permission(menuFlag = MENU_FLAG, perms = {PermissionBtnEnum.AUTH_ROLE})
    @PostMapping("saveUserRole")
    public ResData saveUserRole(@RequestBody SysUserRole model) {
        int result = sysUserService.saveUserRole(model);
        if (result > 0) {
            // todo 刷新缓存
            return ResData.success();
        }
        return ResData.error("保存失败");
    }

    /**
     * 查询用户授权角色信息
     */
//    @Permission(menuFlag = MENU_FLAG, perms = {PermissionBtnEnum.AUTH_ROLE})
    @GetMapping("findUserRole")
    public ResData findUserRole(@RequestParam Long modelId) {
        return ResData.success(sysUserService.findUserRole(modelId));
    }

}
