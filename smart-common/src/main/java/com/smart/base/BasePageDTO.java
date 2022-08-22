package com.smart.base;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
public class BasePageDTO implements Serializable {

	private static final long serialVersionUID = -6762508386808346696L;

	private String keyword;

	private int pageNum = 1;

	private int pageSize = 10;

	/**
	 * 转换成map  用于参数不确定的情况
	 */
	public Map<String, Object> toMap() {
		return JSONObject.parseObject(JSON.toJSONString(this));
	}
}
