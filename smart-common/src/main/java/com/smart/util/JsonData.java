package com.smart.util;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 返回结果封装
 **/

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JsonData implements Serializable {

    /**
     * 状态码 0 表示成功
     */

    private Integer code;
    /**
     * 数据
     */
    private Object data;
    /**
     * 描述
     */
    private String msg;

    /**
     * 成功，不传入数据
     */
    public static JsonData buildSuccess() {
        return new JsonData(0, null, null);
    }

    /**
     * 成功，传入数据
     */
    public static JsonData buildSuccess(Object data) {
        return new JsonData(0, data, null);
    }

    /**
     * 失败，传入描述信息
     */
    public static JsonData buildError(String msg) {
        return new JsonData(-1, null, msg);
    }

    /**
     * 自定义状态码和错误信息
     */
    public static JsonData buildCodeAndMsg(int code, String msg) {
        return new JsonData(code, null, msg);
    }


    /**
     * 获取远程调用数据[对象]
     */
    public <T> T getData(Class<T> clazz) {
        return JSON.parseObject(JSON.toJSONString(data), clazz);
    }

    /**
     * 获取远程调用数据[集合]
     */
    public <T> List<T> getDataList(Class<T> clazz) {
        return JSON.parseArray(JSON.toJSONString(data), clazz);
    }
}
