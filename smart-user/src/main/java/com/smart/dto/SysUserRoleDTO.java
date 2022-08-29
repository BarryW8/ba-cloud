package com.smart.dto;

import com.smart.base.BaseDTO;
import com.smart.model.LoginUser;
import com.smart.model.user.SysUserRole;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class SysUserRoleDTO extends BaseDTO {

    /**
     * 系统id
     */
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 角色id
     */
    private Long roleId;

    /**
     * 保存的数据
     */
    private List<SysUserRole> userRoles;

}
