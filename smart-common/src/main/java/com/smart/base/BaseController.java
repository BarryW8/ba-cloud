package com.smart.base;

import cn.hutool.core.date.DatePattern;
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
     * 获取当前时间格式-指定格式
     */
    protected String getCurrentDate(String format) {
        if (StringUtils.isEmpty(format)) {
            format = DatePattern.NORM_DATETIME_PATTERN;
        }
        LocalDateTime now = LocalDateTime.now();
        return now.format(DateTimeFormatter.ofPattern(format));
    }

    /**
     * 获取当前时间格式-默认格式 yyyy-MM-dd HH:mm:ss
     */
    protected String getCurrentDate() {
        LocalDateTime now = LocalDateTime.now();
        return now.format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN));
    }

}
