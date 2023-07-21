package com.ba.interceptor;

import com.ba.base.UserContext;
import com.ba.enums.ResEnum;
import com.ba.response.ResData;
import com.ba.enums.TokenEnum;
import com.ba.util.CommonUtils;
import com.ba.util.RequestUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 通用拦截
 */
@Component
@Slf4j
public class CommonInterceptor implements HandlerInterceptor {

    public static ThreadLocal<UserContext> threadLocal = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 设备ID
        String deviceId = RequestUtils.getHeader(RequestUtils.DEVICE_ID, request);
        // 应用类型
        String appType = RequestUtils.getHeader(RequestUtils.APP_TYPE, request);
        // 平台类型
        String platform = RequestUtils.getHeader(RequestUtils.PLATFORM, request);

        // 1. 校验 header
        if (StringUtils.isEmpty(deviceId)) {
            //未登录
            CommonUtils.sendJsonMessage(response, ResData.result(ResEnum.ACCOUNT_EXCEPTION));
            return false;
        }
        if (StringUtils.isEmpty(appType)) {
            //未登录
            CommonUtils.sendJsonMessage(response, ResData.result(ResEnum.ACCOUNT_EXCEPTION));
            return false;
        }
        if (StringUtils.isEmpty(platform)) {
            //未登录
            CommonUtils.sendJsonMessage(response, ResData.result(ResEnum.ACCOUNT_EXCEPTION));
            return false;
        }

        // 封装用户上下文
        UserContext context = new UserContext();
        context.setDeviceId(deviceId);
        context.setAppType(TokenEnum.getEnum(appType));
        context.setPlatform(platform);
        threadLocal.set(context);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        threadLocal.remove();
    }
}
