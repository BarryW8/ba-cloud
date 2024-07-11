package com.ba.controller;

import com.ba.base.BaseController;
import com.ba.base.UserContext;
import com.ba.base.UserInfo;
import com.ba.cache.CacheManage;
import com.ba.dto.LoginDTO;
import com.ba.enums.ResEnum;
import com.ba.exception.ServiceException;
import com.ba.model.system.SysMenu;
import com.ba.model.system.SysRoleMenu;
import com.ba.model.system.SysUser;
import com.ba.response.ResData;
import com.ba.service.SysMenuService;
import com.ba.service.SysRoleService;
import com.ba.service.SysUserService;
import com.ba.enums.TokenEnum;
import com.ba.token.TokenManage;
import com.ba.util.BeanUtils;
import com.ba.util.BusinessUtils;
import com.ba.util.CommonUtils;
import com.ba.util.RedisCache;
import com.ba.vo.SysMenuVO;
import com.ba.vo.UserInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

@Slf4j
@RestController
public class LoginController extends BaseController {

    @Resource
    private SysUserService sysUserService;

    @Resource
    private SysRoleService sysRoleService;

    @Resource
    private SysMenuService sysMenuService;

    @Resource
    private TokenManage tokenManage;

    @Resource
    private CacheManage cacheManage;

    @Resource
    private RedisCache redisCache;

    // 验证码生成
    private SysUser checkUser(List<SysUser> userList) {
        if (CollectionUtils.isEmpty(userList)) {
            // 未注册
            throw new ServiceException(ResEnum.ACCOUNT_UNREGISTER);
        }
        if (userList.size() != 1) {
            // 账号异常
            throw new ServiceException(ResEnum.ACCOUNT_EXCEPTION);
        }
        // 已注册
        SysUser user = userList.get(0);
        if (user.getUserStatus() != 0) {
            // 账号已停用
            throw new ServiceException(ResEnum.ACCOUNT_DISABLED);
        }
        if (StringUtils.isEmpty(user.getAppType()) || !user.getAppType().contains(UserContext.getAppType().getCode())) {
            // 账号未授权应用
            throw new ServiceException(ResEnum.ACCOUNT_APP_ERROR);
        }
        return user;
    }

    /**
     * 登录
     */
    @PostMapping("login")
    public ResData login(@RequestBody @Valid LoginDTO dto) {
        // todo 1. 校验验证码

        // 2. 查询用户信息
        StringBuilder sql = new StringBuilder();
        sql.append(" and user_name = '").append(dto.getUsername()).append("'");
        List<SysUser> list = sysUserService.findListHasPwd(sql.toString());
        // 检查账号信息
        SysUser user = checkUser(list);
        // 匹配密码
        if (!dto.getPassword().equals(user.getPassword())) {
            // 账号或者密码错误
            return ResData.result(ResEnum.ACCOUNT_PWD_ERROR);
        }

        // 3. 刷新用户缓存
        Long userId = user.getId();
        UserInfo userInfo = sysUserService.setUserCache(userId);
        userInfo.setType(UserContext.getAppType().getCode());

        // 4. 生成token todo 将token生成和缓存方法解耦
        // 随机生成临时用户id
        String tempUserId = BusinessUtils.getTempUserId(userId, false);
        String password10L = user.getPassword().substring(0, 10);
        String token = tokenManage.createToken(
                userInfo, UserContext.getPlatform(), UserContext.getAppType().getCode(),
                UserContext.getDeviceId(), user.getId().toString(), tempUserId,
                userInfo.getRoleId().toString(), password10L, UserContext.getAppType().getTime());
        if (StringUtils.isEmpty(token)) {
            log.error("缓存服务器异常 生成token失败 accessToken={}", token);
            return ResData.error("登录异常");
        }

        // 4、将完整的用户信息存入redis
//        String key = String.format(CacheConstant.CACHE_STR_KEY_TOKEN_KEY, TokenEnum.WEB.getCode(), tempUserId);
//        redisCache.setKeyValue(key, JSONObject.toJSONString(userInfo), TokenEnum.WEB.getTime(), TimeUnit.MILLISECONDS);

        // 5. 更新用户登录信息
        user.setLoginNum(user.getLoginNum()+1);
        user.setLoginTime(getCurrentDateStr());
        sysUserService.edit(user);

        // 封装结果集
        HashMap<String, Object> result = new HashMap<>(2);
        result.put("userId", tempUserId);
        result.put("token", token);
        return ResData.success(result);
    }

    /**
     * 获取用户信息
     */
    @GetMapping("getUserInfo")
    public ResData getUserInfo() {
        UserInfo currentUser = UserContext.getUserInfo();
        UserInfoVO vo = BeanUtils.convertTo(currentUser, UserInfoVO::new);

        // 获取全部菜单（查缓存）
        Map<String, Object> menuMap = cacheManage.getSysMenu();
        if (MapUtils.isEmpty(menuMap)) {
            // 如果缓存中没有，则查库，并刷新缓存
            menuMap = sysMenuService.findTree(null);
            cacheManage.setSysMenu(menuMap);
        }
        if (currentUser.isSuperManager()) {
            // 超管，直接返回全部菜单
            vo.setMenus(menuMap);
        } else {
            // 非超管，查询角色绑定菜单信息
            vo.setMenus(getRoleMenu(currentUser.getRoleId()));
        }
        return ResData.success(vo);
    }

    private Map<String, Object> getRoleMenu(Long roleId) {
        Map<String, Object> menuMap = new HashMap<>();
        // 查询用户所属角色的菜单权限
        List<SysRoleMenu> roleMenus = sysRoleService.findRoleMenu(roleId);
        if (CollectionUtils.isNotEmpty(roleMenus)) {
            //处理菜单树结构，得到最终结果
            Map<String, Object> queryMap = new HashMap<>();
            queryMap.put("roleMenus", roleMenus);
            menuMap = sysMenuService.findTree(queryMap);
        }
        return menuMap;
    }

    /**
     * 注册
     */
    @PostMapping("register")
    public ResData register(@RequestBody SysUser user) {
        return ResData.success();
    }

    /**
     * 退出登录
     */
    @GetMapping("logout")
    public ResData logout() {
        // 删除用户缓存
        sysUserService.delUserCache(UserContext.getUserId());
        return ResData.success();
    }

}
