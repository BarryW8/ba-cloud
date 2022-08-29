package com.smart.controller;

import cn.hutool.core.date.DatePattern;
import com.smart.base.BaseCommonController;
import com.smart.base.BaseController;
import com.smart.base.PageView;
import com.smart.base.SimpleModel;
import com.smart.dto.SysUserPageDTO;
import com.smart.dto.UserLoginDTO;
import com.smart.enums.BizCodeEnum;
import com.smart.model.LoginUser;
import com.smart.model.user.SysUser;
import com.smart.service.SysMenuService;
import com.smart.service.SysUserService;
import com.smart.uid.impl.CachedUidGenerator;
import com.smart.util.CommonUtils;
import com.smart.util.JWTUtil;
import com.smart.util.JsonData;
import com.smart.vo.MenuVO;
import com.smart.vo.MetaVO;
import com.smart.vo.SysMenuVO;
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
import java.util.List;

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
        //-----------c、生成token
        LoginUser loginUser =  LoginUser.builder().build();
        BeanUtils.copyProperties(user, loginUser);
        loginUser.setUserId(user.getId());
        String accessToken = JWTUtil.geneJsonWebToken(loginUser);
        return JsonData.buildSuccess(accessToken);
    }

    @GetMapping("userInfo")
    public JsonData userInfo() {
        UserInfoVO userInfo = new UserInfoVO();
        LoginUser loginUser = getCurrentUser();
        Long userId = loginUser.getUserId();
        SysUser user = sysUserService.findById(userId);
        if(user == null){
            log.error("获取 userInfo 异常");
            return JsonData.buildError("账号不存在");
        }
        BeanUtils.copyProperties(user, userInfo);
        List<SysMenuVO> menuList = sysMenuService.getList();
        List<MenuVO> menuVOS = new ArrayList<>();
        for (SysMenuVO sysMenuVO:menuList) {
            MenuVO menuVO = new MenuVO();
            menuVO.setId(sysMenuVO.getId());
            menuVO.setParentId(sysMenuVO.getParentId());
            menuVO.setName(sysMenuVO.getLabel());
            menuVO.setPath(sysMenuVO.getRoutePath());
            menuVO.setComponent(sysMenuVO.getPagePath());
            MetaVO metaVO = new MetaVO();
            metaVO.setIcon(sysMenuVO.getIconPath());
            metaVO.setPermission(sysMenuVO.getPerms());
            String target = sysMenuVO.getLinkType() == 1 ? "_blank":"";
            metaVO.setTarget(target);
            metaVO.setTitle(sysMenuVO.getLabel());
            metaVO.setHideChildren(sysMenuVO.getIsHide() == 1);
            menuVO.setMeta(metaVO);
            menuVOS.add(menuVO);
        }
        userInfo.setMenuList(menuVOS);
        return JsonData.buildSuccess(userInfo);
    }

    @GetMapping("logout")
    public JsonData logout() {
        // todo 后续再完善
        return JsonData.buildSuccess("登出成功");
    }

    private String queryCondition(SysUserPageDTO dto) {
        String keyword = dto.getKeyword();
        StringBuilder sqlBf = new StringBuilder();
        if (StringUtils.isNotEmpty(keyword)) {
            sqlBf.append(" and (user_name like '%").append(keyword).append("%'")
                    .append(" or real_name like '%").append(keyword).append("%'").append(")");
        }
        return sqlBf.toString();
    }
}
