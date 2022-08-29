package com.smart.base;

import com.smart.model.LoginUser;
import lombok.Data;

import java.io.Serializable;

@Data
public class BaseDTO implements Serializable {

	private static final long serialVersionUID = 8092408631528470911L;

	/**
	 * 当前用户
	 */
	private LoginUser currentUser;

	/**
	 * 当前时间
	 */
	private String currentDate;

}
