package com.smart.model.user;


import com.smart.base.BaseModel;
import lombok.Data;

/**
 * 用户角色信息表
 */
@Data
public class SysUserRole extends BaseModel {

    private static final long serialVersionUID = 1794757718154432574L;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 角色id
     */
    private Long roleId;
}
