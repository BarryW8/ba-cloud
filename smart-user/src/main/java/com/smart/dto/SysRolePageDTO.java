package com.smart.dto;

import com.smart.base.BasePageDTO;
import lombok.Data;

@Data
public class SysRolePageDTO extends BasePageDTO {

    /**
     * 关键字
     */
    private String keyword;

    /**
     * 角色状态 0启用 1停用
     */
    private Integer status;

}
