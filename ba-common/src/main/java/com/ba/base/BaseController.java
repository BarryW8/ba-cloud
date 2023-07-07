package com.ba.base;

import cn.hutool.core.date.DatePattern;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 通用 controller，后续有通用方法可在此添加
 */
public class BaseController {

    /**
     * 获取当前用户ID
     */
    protected Long getCurrentUserId() {
        return UserContext.getUserId();
    }

    /**
     * 获取当前时间格式-默认格式 yyyy-MM-dd HH:mm:ss
     */
    protected String getCurrentDateStr() {
        LocalDateTime now = LocalDateTime.now();
        return now.format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN));
    }

    /**
     * 获取当前时间格式-指定格式
     */
    protected String getCurrentDateStr(String format) {
        if (StringUtils.isEmpty(format)) {
            format = DatePattern.NORM_DATETIME_PATTERN;
        }
        LocalDateTime now = LocalDateTime.now();
        return now.format(DateTimeFormatter.ofPattern(format));
    }

}
