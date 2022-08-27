package com.smart.controller;

import cn.hutool.core.date.DatePattern;
import com.smart.base.BaseCommonController;
import com.smart.base.BaseController;
import com.smart.base.PageView;
import com.smart.base.SimpleModel;
import com.smart.dto.SysUserRoleDTO;
import com.smart.dto.SysUserRolePageDTO;
import com.smart.model.LoginUser;
import com.smart.model.user.SysUserRole;
import com.smart.service.SysUserRoleService;
import com.smart.uid.impl.CachedUidGenerator;
import com.smart.util.JsonData;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 用户角色信息表
 *
 */

@RestController
@RequestMapping("/sysUserRole")
@Slf4j
public class SysUserRoleController extends BaseController implements BaseCommonController<SysUserRole, SysUserRolePageDTO> {


    @Resource
    private SysUserRoleService sysUserRoleService;


    @Resource
    private CachedUidGenerator uidGenerator;


    @GetMapping("findById")
    @Override
    public JsonData findById(@RequestParam Long modelId) {
        return JsonData.buildSuccess(sysUserRoleService.findById(modelId));
    }



    @PostMapping("save")
    @Override
    public JsonData save(@RequestBody SysUserRole sysUserRole) {
        LoginUser currentUser = getCurrentUser();
        int result;
        if (sysUserRole.getId() == null) {
            sysUserRole.setId(uidGenerator.getUID());
            sysUserRole.setCreateUserId(currentUser.getUserId());
            sysUserRole.setCreateUserName(currentUser.getRealName());
            sysUserRole.setCreateTime(getCurrentDate(DatePattern.NORM_DATETIME_PATTERN));
            result = sysUserRoleService.save(sysUserRole);
        } else {
            //编辑
            sysUserRole.setUpdateUserId(currentUser.getUserId());
            sysUserRole.setUpdateUserName(currentUser.getRealName());
            sysUserRole.setUpdateTime(getCurrentDate(DatePattern.NORM_DATETIME_PATTERN));
            result = sysUserRoleService.update(sysUserRole);
        }
        if (result > 0) {
            return JsonData.buildSuccess();
        }
        return JsonData.buildError("保存失败!");
    }

    @PostMapping("findPage")
    @Override
    public JsonData findPage(@RequestBody SysUserRolePageDTO dto) {
        String keyword = dto.getKeyword();
        String beginTime = dto.getBeginTime();
        String endTime = dto.getEndTime();
        StringBuilder sqlBf = new StringBuilder();
        if(dto.getUserId()!=null){
            sqlBf.append(" and user_id >= '").append(dto.getUserId()).append("'");
        }
        if(dto.getRoleId()!=null){
            sqlBf.append(" and role_id >= '").append(dto.getRoleId()).append("'");
        }
        if (StringUtils.isNotEmpty(beginTime)) {
            sqlBf.append(" and create_time >= '").append(beginTime).append(" 00:00:00'");
        }
        if (StringUtils.isNotEmpty(endTime)) {
            sqlBf.append(" and create_time <= '").append(endTime).append(" 23:59:59'");
        }
        if (StringUtils.isNotEmpty(keyword)) {
            sqlBf.append(" and name like '%").append(keyword).append("%'");
        }
        PageView<SysUserRole> pageList = sysUserRoleService.findPage(dto.getPageNum(), dto.getPageSize(), sqlBf.toString(), null);
        return JsonData.buildSuccess(pageList);
    }

    @GetMapping("deleteById")
    @Override
    public JsonData deleteById(@RequestParam Long modelId) {
        LoginUser currentUser = getCurrentUser();
        SimpleModel simpleModel = new SimpleModel();
        simpleModel.setModelId(modelId);
        simpleModel.setDelUser(currentUser.getUserId());
        simpleModel.setDelUserName(currentUser.getRealName());
        simpleModel.setDelDate(getCurrentDate(DatePattern.NORM_DATETIME_PATTERN));
        int result = sysUserRoleService.deleteBySm(simpleModel);
        if (result > 0) {
            return JsonData.buildSuccess();
        }
        return JsonData.buildError("删除失败!");
    }

    @GetMapping("findList")
    @Override
    public JsonData findList(String condition) {
        return JsonData.buildSuccess(sysUserRoleService.findList(condition));
    }

    @PostMapping("authRole")
    public JsonData authRole(@RequestBody @Valid SysUserRoleDTO dto) {
        LoginUser currentUser = getCurrentUser();
        int result=0;
        SysUserRole userRole =null;
        if(dto.getId()!=null){
             userRole = sysUserRoleService.findById(dto.getId());
        }
        if(userRole!=null){
            if(userRole.getUserId().equals(dto.getUserId())){
                //编辑
                userRole.setRoleId(dto.getRoleId());
                userRole.setUpdateUserId(currentUser.getUserId());
                userRole.setUpdateUserName(currentUser.getRealName());
                userRole.setUpdateTime(getCurrentDate(DatePattern.NORM_DATETIME_PATTERN));
                result = sysUserRoleService.update(userRole);
            }else{
                return JsonData.buildError("保存失败!");
            }
        }else{
            userRole= new SysUserRole();
            //新增一个
            userRole.setId(uidGenerator.getUID());
            userRole.setRoleId(dto.getRoleId());
            userRole.setUserId(dto.getUserId());
            userRole.setCreateUserId(currentUser.getUserId());
            userRole.setCreateUserName(currentUser.getRealName());
            userRole.setCreateTime(getCurrentDate(DatePattern.NORM_DATETIME_PATTERN));
            result = sysUserRoleService.save(userRole);
        }
        if (result > 0) {
            return JsonData.buildSuccess();
        }
        return JsonData.buildError("保存失败!");
    }

}
