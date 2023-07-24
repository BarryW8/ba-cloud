package com.ba.base;

import com.ba.annotation.TableField;
import com.ba.enums.FieldFill;
import com.ba.enums.FieldType;
import lombok.Data;

import java.io.Serializable;

@Data
public class BaseModel implements Serializable {

	private static final long serialVersionUID = 8092408631528470900L;

	/**
	 * 主键ID
	 */
	@TableField(type = FieldType.ID, fill = FieldFill.INSERT)
	private Long id;

	/**
	 * 排序
	 */
	private int orderBy;

	/**
	 * 备注
	 */
	private String note;

	/**
	 * 创建人
	 */
	@TableField(type = FieldType.USER, fill = FieldFill.INSERT)
	private Long createBy;

	/**
	 * 创建时间
	 */
	@TableField(type = FieldType.TIME, fill = FieldFill.INSERT)
	private String createTime;

	/**
	 * 修改人
	 */
	@TableField(type = FieldType.USER, fill = FieldFill.UPDATE)
	private Long updateBy;

	/**
	 * 修改时间
	 */
	@TableField(type = FieldType.TIME, fill = FieldFill.UPDATE)
	private String updateTime;

}
