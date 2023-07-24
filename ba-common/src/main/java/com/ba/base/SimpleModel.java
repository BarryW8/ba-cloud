package com.ba.base;

import com.ba.annotation.TableField;
import com.ba.enums.FieldFill;
import com.ba.enums.FieldType;
import lombok.Data;

/**
 * 逻辑删除通用model
 */
@Data
public class SimpleModel {
	/**
	 * 通常为主键ID
	 */
	private Long modelId;

	/**
	 * 删除用户
	 */
	@TableField(type = FieldType.USER, fill = FieldFill.INSERT)
	private Long delUser;

	/**
	 * 删除时间
	 */
	@TableField(type = FieldType.TIME, fill = FieldFill.INSERT)
	private String delDate;

	/**
	 * 删除信息
	 */
	private String msg;
}
