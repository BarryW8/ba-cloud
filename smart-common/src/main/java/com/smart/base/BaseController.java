package com.smart.base;

import com.smart.interceptor.LoginInterceptor;
import com.smart.model.LoginUser;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 通用 controller，后续有通用方法可在此添加
 */
public class BaseController {

    /**
     * 获取当前用户信息
     */
    protected LoginUser getCurrentUser() {
        return LoginInterceptor.threadLocal.get();
    }

    /**
     * 获取当前时间格式
     */
    protected String getCurrentDate(String format) {
        if (StringUtils.isEmpty(format)) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        LocalDateTime now = LocalDateTime.now();
        return now.format(DateTimeFormatter.ofPattern(format));
    }

}
