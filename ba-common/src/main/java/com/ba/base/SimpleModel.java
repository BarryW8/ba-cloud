package com.ba.base;

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
	private Long delUser;

	/**
	 * 删除时间
	 */
	private String delDate;

	/**
	 * 删除信息
	 */
	private String msg;
}
