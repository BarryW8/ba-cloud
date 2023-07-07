package com.ba.model.system;

import com.ba.base.BaseModel;
import lombok.Data;

@Data
public class Dictionary extends BaseModel {

    /**
     * 父ID
     */
    private Long parentId;

    /**
     * 父code
     */
    private String parentCode;

    /**
     * 字典编号
     */
    private String code;

    /**
     * 字典名称
     */
    private String name;

    /**
     * 状态：0-正常，1-禁用
     */
    private int status;

    /**
     * 相关配置，以标准json格式存储
     */
    private String configValue;

}
