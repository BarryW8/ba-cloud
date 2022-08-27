package com.smart.interceptor;

import com.smart.model.LoginUser;
import com.smart.util.CommonUtils;
import com.smart.util.JWTUtil;
import com.smart.util.JsonData;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录拦截
 */
@Component
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    public static ThreadLocal<LoginUser> threadLocal = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String accessToken = request.getHeader("token");
        if(accessToken == null) {
            accessToken = request.getParameter("token");
        }

        if(StringUtils.isNotBlank(accessToken)){
            //不为空
            Claims claims = JWTUtil.checkJWT(accessToken);
            if(claims == null){
                //未登录
                CommonUtils.sendJsonMessage(response, JsonData.buildError("用户未登录"));
                return false;
            }
            long userId = Long.parseLong(claims.get("userId").toString());
            String userName = claims.get("userName").toString();
            String realName = claims.get("realName").toString();
            String email = claims.get("email").toString();
            String telephone =claims.get("telephone").toString();
            LoginUser loginUser = LoginUser.builder()
                    .userId(userId)
                    .userName(userName)
                    .realName(realName)
                    .email(email)
                    .telephone(telephone)
                    .build();
            threadLocal.set(loginUser);
            return true;

        }
        CommonUtils.sendJsonMessage(response,JsonData.buildError("账号未登录"));
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        threadLocal.remove();
    }
}
