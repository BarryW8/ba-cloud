package com.ba.model.system;


import com.ba.base.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户角色信息表
 */
@Data
@EqualsAndHashCode(callSuper = true)
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
