package com.ba.base;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
public class BasePage implements Serializable {

	private static final long serialVersionUID = -6762508386808346696L;

	private String keyword;

	private int pageNum = 1;

	private int pageSize = 10;

	/**
	 * 转换成 Map
	 */
	public Map<String, Object> toMap() {
		return JSONObject.parseObject(JSON.toJSONString(this));
	}

	/**
	 * 转换成 带分页的Map
	 */
	public Map<String, Object> toPageMap() {
		if (pageSize<1) {
			pageSize=15;
		}
		if (pageNum<1) {
			pageNum=0;
		} else {
			pageNum=pageSize*--pageNum;
		}
		return toMap();
	}

}
