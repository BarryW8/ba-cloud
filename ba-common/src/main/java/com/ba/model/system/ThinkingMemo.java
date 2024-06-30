package com.ba.model.system;


import com.ba.base.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author: generator
 * @Description: 备忘录管理
 * @Date: 2024/6/27 15:25:25
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ThinkingMemo  extends BaseModel {
    private static final long serialVersionUID=1L;
        
    /**
     * 文件夹ID
     */
    private Long pid;
        
    /**
     * 类型M目录F文件
     */
    private String type;
        
    /**
     * 所属用户ID
     */
    private Long ownerId;
        
    /**
     * 标题（冗余第一行文本）
     */
    private String title;
        
    /**
     * 副标题（冗余第二行）
     */
    private String subTitle;
        
    /**
     * 内容
     */
    private String content;
        
    /**
     * 图标
     */
    private String icon;
        
    /**
     * 最后操作时间
     */
    private String lastTime;

    /**
     * 是否置顶0否1是
     */
    private int isTop;
                                
}
