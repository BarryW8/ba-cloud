package com.ba.base;

import lombok.Data;

import java.io.Serializable;

@Data
public class BaseModel implements Serializable {

	private static final long serialVersionUID = 8092408631528470900L;

	/**
	 * 主键ID
	 */
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
	private Long createBy;

	/**
	 * 创建时间
	 */
	private String createTime;

	/**
	 * 修改人
	 */
	private Long updateBy;

	/**
	 * 修改时间
	 */
	private String updateTime;

}
