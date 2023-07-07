package com.ba.dto;

import com.ba.base.BasePage;
import lombok.Data;

@Data
public class SysRolePage extends BasePage {

    /**
     * 关键字
     */
    private String keyword;

    /**
     * 角色状态 0启用 1停用
     */
    private Integer status;

}
