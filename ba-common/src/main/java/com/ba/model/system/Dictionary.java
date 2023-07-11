package com.ba.model.system;

import com.ba.base.BaseModel;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Dictionary extends BaseModel {

    /**
     * 字典编号
     */
    private String code;

    /**
     * 字典名称
     */
    private String name;

    /**
     * 父ID
     */
    private Long parentId;

    /**
     * 父编号
     */
    private String parentCode;

    /**
     * 状态：0-正常，1-禁用
     */
    private int status;

}
