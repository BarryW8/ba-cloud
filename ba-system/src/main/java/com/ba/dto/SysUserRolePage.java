package com.ba.dto;

import com.ba.base.BasePage;
import lombok.Data;

@Data
public class SysUserRolePage extends BasePage {
    private String beginTime;

    private String endTime;

    private Long userId;
    private Long roleId;
}
