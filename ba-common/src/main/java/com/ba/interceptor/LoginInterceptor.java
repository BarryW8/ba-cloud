package com.ba.interceptor;

import com.alibaba.fastjson.JSON;
import com.ba.base.UserContext;
import com.ba.base.UserInfo;
import com.ba.enums.ResEnum;
import com.ba.response.ResData;
import com.ba.enums.TokenEnum;
import com.ba.token.TokenManage;
import com.ba.token.TokenModel;
import com.ba.util.CommonUtils;
import com.ba.util.RequestUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录拦截
 */
@Component
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    @Resource
    private TokenManage tokenManage;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        UserContext userContext = UserContext.get();
        // 设备ID
        String deviceId = UserContext.getDeviceId();
        // 应用类型
        String appType = UserContext.getAppType().getCode();
        // 平台类型
        String platform = UserContext.getPlatform();

        // 请求IP地址
        String ip = RequestUtils.getIpAddress(request);
        // 请求路径
        String url = RequestUtils.getUrl(request);
        // token
        String token = RequestUtils.getHeader(RequestUtils.TOKEN_COOKIE, request);
        // 用户ID
        String tempUserId = RequestUtils.getHeader(RequestUtils.USER_ID, request);
        log.info("login 拦截请求 IP={},url={}", ip, url);

        // 1. 校验 header
        if (StringUtils.isEmpty(tempUserId)) {
            //临时登陆用户id
            log.error("login 拦截请求 临时登陆用户id不存在 ->IP={},url={}", ip, url);
            CommonUtils.sendJsonMessage(response, ResData.result(ResEnum.ACCOUNT_EXCEPTION));
            return false;
        }
        if (StringUtils.isEmpty(token)) {
            //未登录
            log.error("login 拦截请求 token 参数不存在->IP={},url={}", ip, url);
            CommonUtils.sendJsonMessage(response, ResData.result(ResEnum.ACCOUNT_EXCEPTION));
            return false;
        }

        // 2. 校验 token
        //查缓存得到token 数据
        String loginUserStr = tokenManage.getToken(appType, tempUserId);
        if (StringUtils.isEmpty(loginUserStr)) {
            log.error("login 拦截请求 缓存中的token失效 ->IP={},url={}", ip, url);
            CommonUtils.sendJsonMessage(response, ResData.result(ResEnum.ACCOUNT_EXPIRED));
            return false;
        }
        UserInfo userInfo = JSON.parseObject(loginUserStr, UserInfo.class);
        if (!token.equals(userInfo.getToken())) {
            //token 非法
            log.error("login 拦截请求 token 非法 ->IP={},url={}", ip, url);
            CommonUtils.sendJsonMessage(response, ResData.result(ResEnum.ACCOUNT_EXCEPTION));
            return false;
        }
        if (userInfo.getId() == null) {
            //userID 非法
            log.error("login 拦截请求 userID 非法 ->IP={},url={}", ip, url);
            CommonUtils.sendJsonMessage(response, ResData.result(ResEnum.ACCOUNT_EXPIRED));
            return false;
        }

        //解析缓存的token
        TokenModel tokenModel = tokenManage.parseToken(userInfo.getToken(), platform, appType, deviceId, tempUserId);
        if (tokenModel == null) {
            //token 非法
            log.error("login 拦截请求 token 解析非法 ->IP={},url={}", ip, url);
            CommonUtils.sendJsonMessage(response, ResData.result(ResEnum.ACCOUNT_EXPIRED));
            return false;
        }

        //token缓存 -- 自动续期
        tokenManage.tokenRenew(platform, appType, tempUserId, TokenEnum.WEB.getTime());

        // 封装用户上下文
        userContext.setUserId(userInfo.getId());
        userContext.setUserInfo(userInfo);
        userContext.setDeviceId(deviceId);
        userContext.setAppType(TokenEnum.getEnum(appType));
        userContext.setPlatform(platform);
//        threadLocal.set(userContext);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }
}
