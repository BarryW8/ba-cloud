package com.smart.base;

import lombok.Data;

import java.io.Serializable;

@Data
public class BaseModel  implements Serializable {

	private static final long serialVersionUID = 8092408631528470900L;

	private Long id;

	/**
	 * 排序
	 */
	private int orderBy;

	private String note;

	/**
	 * 创建人
	 */
	private Long createUserId;

	/**
	 * 修改人
	 */
	private Long lastUserId;

	private String createUserName;

	private String createTime;

	private String lastUserName;

	private String lastTime;
}
