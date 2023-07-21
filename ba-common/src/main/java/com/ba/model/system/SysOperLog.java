package com.ba.model.system;

import com.ba.base.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SysOperLog extends BaseModel {

    /**
     * 业务名称
     */
    private String business;

    /**
     * 方法名称
     */
    private String method;

    /**
     * 请求方式
     */
    private String reqType;

    /**
     * 请求参数
     */
    private String reqParam;

    /**
     * 返回参数
     */
    private String resParam;

    /**
     * 操作人ID
     */
    private Long operatorId;

    /**
     * 操作人姓名
     */
    private String operatorName;

    /**
     * 操作人手机号
     */
    private String operatorPhone;

    /**
     * 操作类型 字典（-1其它 0查询 1新增 2修改 3删除）
     */
    private String operType;

    /**
     * 操作来源 字典（0其它 1WEB 2APP）
     */
    private String operResource;

    /**
     * 请求URL
     */
    private String operUrl;

    /**
     * 主机地址
     */
    private String operIp;

    /**
     * 操作地点
     */
    private String operLocation;

    /**
     * 操作状态（0正常 1异常）
     */
    private int status;

    /**
     * 错误消息
     */
    private String errorMsg;

    /**
     * 操作日期
     */
    private String operTime;

    /**
     * 消耗时间
     */
    private Long costTime;

}
