package com.ba.dto;

import lombok.Data;

/**
 * @Author: barryw
 * @Description:
 * @Date: 2024/7/10 13:52
 */
@Data
public class UserRoleDTO {
    private Long userId;
    private Long roleId;
    private Integer appType;
}
