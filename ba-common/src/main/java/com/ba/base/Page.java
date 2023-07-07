package com.ba.base;

import lombok.Data;

@Data
public class Page {

	/**
	 * 当前页
	 */
	private int page;

	/**
	 * size
	 */
	private int pageSize;

	/**
	 * 条件
	 */
	private String sql;
}
