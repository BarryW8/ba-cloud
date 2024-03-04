package com.ba.model.system;


import com.ba.base.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author: generator
 * @Description: 组织管理
 * @Date: 2024/2/29 11:14:38
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class OrgManagement  extends BaseModel {
    private static final long serialVersionUID=1L;
        
    /**
     * 组织编码
     */
    private String code;
        
    /**
     * 组织名称
     */
    private String name;
        
    /**
     * 上级组织ID
     */
    private Long parentId;
        
    /**
     * 上级组织名称
     */
    private String parentName;
        
    /**
     * 组织类型：0-店铺，1-部门，2-企业
     */
    private int type;
        
    /**
     * 是否考勤：0-是，1-否
     */
    private int isAttendance;
        
    /**
     * 上班时间
     */
    private String workBegTime;
        
    /**
     * 下班时间
     */
    private String workEndTime;
        
    /**
     * 是否午休：0-是，1-否
     */
    private int isRest;
        
    /**
     * 午休开始时间
     */
    private String restBegTime;
        
    /**
     * 午休结束时间
     */
    private String restEndTime;
        
    /**
     * 中途外出时间（分钟）
     */
    private int outTime;
                                
}
