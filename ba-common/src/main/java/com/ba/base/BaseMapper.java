package com.ba.base;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 通用mapper接口
 * T -> 对应model类型
 */
public interface BaseMapper<T> {

	public int insert(T t);

	public int update(T t);

	public List<T> findList(String sql);

	public List<T> findPage(Map<String, Object> map);

	public int count(Map<String, Object> map);

	public int deleteBySm(SimpleModel del);

	public int deleteById(T t);

	public T findById(@Param("id") Long id);

}
