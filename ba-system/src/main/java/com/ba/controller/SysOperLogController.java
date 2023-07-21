package com.ba.controller;

import cn.hutool.crypto.SecureUtil;
import com.ba.annotation.Permission;
import com.ba.base.BaseCommonController;
import com.ba.base.BaseController;
import com.ba.base.PageView;
import com.ba.dto.SysUserPage;
import com.ba.enums.OperationEnum;
import com.ba.model.system.SysOperLog;
import com.ba.model.system.SysUserRole;
import com.ba.response.ResData;
import com.ba.service.SysOperLogService;
import com.ba.uid.impl.CachedUidGenerator;
import com.ba.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/sysOperLog")
@Slf4j
public class SysOperLogController extends BaseController implements BaseCommonController<SysOperLog, SysUserPage> {

    private static final String MENU_CODE = "system:log";

    @Resource
    private SysOperLogService sysOperLogService;

    @Resource
    private CachedUidGenerator uidGenerator;

    @Permission(menuFlag = MENU_CODE, perms = {OperationEnum.VIEW})
    @GetMapping("findById")
    @Override
    public ResData findById(@RequestParam Long modelId) {
        return ResData.success(sysOperLogService.findById(modelId));
    }

    @Permission(menuFlag = MENU_CODE, perms = {OperationEnum.ADD, OperationEnum.EDIT})
    @PostMapping("save")
    @Override
    public ResData save(@RequestBody SysOperLog model) {
        int result;
        if (model.getId() == null) {
            model.setId(uidGenerator.getUID());
            result = sysOperLogService.insert(model);
        } else {
            //编辑
            result = sysOperLogService.update(model);
        }
        if (result > 0) {
            return ResData.success();
        }
        return ResData.error("保存失败!");
    }

    @Permission(menuFlag = MENU_CODE, perms = {OperationEnum.VIEW})
    @PostMapping("findPage")
    @Override
    public ResData findPage(@RequestBody SysUserPage dto) {
        Map<String, Object> queryMap = this.queryCondition(dto);
        PageView<SysOperLog> pageList = sysOperLogService.findPage(queryMap);
        return ResData.success(pageList);
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

    @Permission(menuFlag = MENU_CODE, perms = {OperationEnum.DELETE})
    @GetMapping("deleteById")
    @Override
    public ResData deleteById(@RequestParam Long modelId) {
        SysOperLog model = new SysOperLog();
        model.setId(modelId);
        int result = sysOperLogService.deleteById(model);
        if (result > 0) {
            return ResData.success();
        }
        return ResData.error("删除失败!");
    }

}
