package com.smart.controller;

import cn.hutool.core.date.DatePattern;
import com.smart.base.BaseCommonController;
import com.smart.base.BaseController;
import com.smart.base.PageView;
import com.smart.base.SimpleModel;
import com.smart.dto.SysRoleMenuPageDTO;
import com.smart.model.LoginUser;
import com.smart.model.user.SysRoleMenu;
import com.smart.service.SysRoleMenuService;
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

/**
 * 角色菜单中间表
 *
 */

@RestController
@RequestMapping("/sysRoleMenu")
@Slf4j
public class SysRoleMenuController extends BaseController implements BaseCommonController<SysRoleMenu, SysRoleMenuPageDTO> {


    @Resource
    private SysRoleMenuService sysRoleMenuService;

    @Resource
    private CachedUidGenerator uidGenerator;


    @GetMapping("findById")
    @Override
    public JsonData findById(@RequestParam Long modelId) {
        return JsonData.buildSuccess(sysRoleMenuService.findById(modelId));
    }


    @PostMapping("save")
    @Override
    public JsonData save(@RequestBody SysRoleMenu sysRoleMenu) {
        LoginUser currentUser = getCurrentUser();
        int result;
        if (sysRoleMenu.getId() == null) {
            sysRoleMenu.setId(uidGenerator.getUID());
            sysRoleMenu.setCreateUserId(currentUser.getUserId());
            sysRoleMenu.setCreateUserName(currentUser.getRealName());
            sysRoleMenu.setCreateTime(getCurrentDate(DatePattern.NORM_DATETIME_PATTERN));
            result = sysRoleMenuService.save(sysRoleMenu);
        } else {
            //编辑
            sysRoleMenu.setUpdateUserId(getCurrentUser().getUserId());
            sysRoleMenu.setUpdateUserName(getCurrentUser().getRealName());
            sysRoleMenu.setUpdateTime(getCurrentDate(DatePattern.NORM_DATETIME_PATTERN));
            result = sysRoleMenuService.update(sysRoleMenu);
        }
        if (result > 0) {
            return JsonData.buildSuccess();
        }
        return JsonData.buildError("保存失败!");
    }

    @PostMapping("findPage")
    @Override
    public JsonData findPage(@RequestBody SysRoleMenuPageDTO dto) {
        String keyword = dto.getKeyword();
        String beginTime = dto.getBeginTime();
        String endTime = dto.getEndTime();
        StringBuilder sqlBf = new StringBuilder();
        if (StringUtils.isNotEmpty(beginTime)) {
            sqlBf.append(" and create_time >= '").append(beginTime).append(" 00:00:00'");
        }
        if (StringUtils.isNotEmpty(endTime)) {
            sqlBf.append(" and create_time <= '").append(endTime).append(" 23:59:59'");
        }
        if (StringUtils.isNotEmpty(keyword)) {
            sqlBf.append(" and name like '%").append(keyword).append("%'");
        }
        PageView<SysRoleMenu> pageList = sysRoleMenuService.findPage(dto.getPageNum(), dto.getPageSize(), sqlBf.toString(), null);
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
        int result = sysRoleMenuService.deleteBySm(simpleModel);
        if (result > 0) {
            return JsonData.buildSuccess();
        }
        return JsonData.buildError("删除失败!");
    }

    @GetMapping("findList")
    @Override
    public JsonData findList(String condition) {
        return JsonData.buildSuccess(sysRoleMenuService.findList(condition));
    }

}
