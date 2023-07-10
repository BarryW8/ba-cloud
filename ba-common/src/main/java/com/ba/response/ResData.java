package com.ba.response;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 响应结果封装
 */
@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResData implements Serializable {

    /**
     * 状态码
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
     * 成功
     */
    public static ResData success() {
        return new ResData(HttpStatus.SC_OK, null, null);
    }
    public static ResData success(Object data) {
        return new ResData(HttpStatus.SC_OK, data, null);
    }

    /**
     * 失败
     */
    public static ResData error(String msg) {
        return new ResData(HttpStatus.SC_INTERNAL_SERVER_ERROR, null, msg);
    }

    /**
     * 自定义状态码
     */
    public static ResData result(int code, String msg) {
        return new ResData(code, null, msg);
    }

    /**
     * 自定义状态码 枚举
     */
    public static ResData result(ResEnum resEnum) {
        return new ResData(resEnum.getCode(), null, resEnum.getMsg());
    }

    /**
     * 自定义状态码 枚举+数据
     */
    public static ResData result(ResEnum resEnum, Object data) {
        return new ResData(resEnum.getCode(), data, resEnum.getMsg());
    }

    /**
     * 获取远程调用数据[JSON对象]
     */
    public <T> T getData(Class<T> clazz) {
        try {
            return data == null ? null : JSON.parseObject(JSON.toJSONString(data), clazz);
        } catch (Exception e) {
            log.error("数据转义失败 JSON对象 data={}", data);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取远程调用数据[JSON集合]
     */
    public <T> List<T> getDataList(Class<T> clazz) {
        try {
            return data == null ? null : JSON.parseArray(JSON.toJSONString(data), clazz);
        } catch (Exception e) {
            log.error("数据转义失败 JSON集合 data={}", data);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取远程缓存的数据[JSON对象]
     */
    public <T> T getCacheData(Class<T> clazz) {
        try {
            return data == null ? null : JSON.parseObject(data.toString(), clazz);
        } catch (Exception e) {
            log.error("数据转义失败 JSON对象 data={}", data);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取远程缓存的数据[JSON集合]
     */
    public <T> List<T> getCacheDataList(Class<T> clazz) {
        try {
            return data == null ? null : JSON.parseArray(data.toString(), clazz);
        } catch (Exception e) {
            log.error("数据转义失败 JSON集合 data={}", data);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取远程缓存的数据[基础对象]
     */
    public <T> T getCacheDataBaseObj(Class<T> clazz) {
        if (data == null) {
            return null;
        }
        if ("Integer".equals(clazz.getName())) {
            Integer result = null;
            try {
                result = Integer.parseInt(data.toString());
            } catch (Exception e) {
                log.error("数据转义失败 Integer data={}", data);
                e.printStackTrace();
            }
            return (T) result;
        } else if ("Long".equals(clazz.getName())) {
            Long result = null;
            try {
                result = Long.parseLong(data.toString());
            } catch (Exception e) {
                log.error("数据转义失败 Long data={}", data);
                e.printStackTrace();
            }
            return (T) result;
        } else if ("Float".equals(clazz.getName())) {
            Float result = null;
            try {
                result = Float.parseFloat(data.toString());
            } catch (Exception e) {
                log.error("数据转义失败 Float data={}", data);
                e.printStackTrace();
            }
            return (T) result;
        } else if ("Short".equals(clazz.getName())) {
            Short result = null;
            try {
                result = Short.parseShort(data.toString());
            } catch (Exception e) {
                log.error("数据转义失败 Short data={}", data);
                e.printStackTrace();
            }
            return (T) result;
        } else if ("Double".equals(clazz.getName())) {
            Double result = null;
            try {
                result = Double.parseDouble(data.toString());
            } catch (Exception e) {
                log.error("数据转义失败 Double data={}", data);
                e.printStackTrace();
            }
            return (T) result;
        } else if ("Boolean".equals(clazz.getName())) {
            Boolean result = null;
            try {
                result = Boolean.parseBoolean(data.toString());
            } catch (Exception e) {
                log.error("数据转义失败 Boolean data={}", data);
                e.printStackTrace();
            }
            return (T) result;
        } else if ("BigDecimal".equals(clazz.getName())) {
            BigDecimal result = null;
            try {
                result = new BigDecimal(data.toString());
            } catch (Exception e) {
                log.error("数据转义失败 BigDecimal data={}", data);
                e.printStackTrace();
            }
            return (T) result;
        } else {
            return (T) data.toString();
        }
    }

}
