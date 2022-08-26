package com.smart.dto;

import com.smart.base.BasePageDTO;
import lombok.Data;

@Data
public class SysUserRolePageDTO extends BasePageDTO {
    private String beginTime;

    private String endTime;

    private Long userId;
    private Long roleId;
}
