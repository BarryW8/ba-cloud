package com.smart.base;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 通用mapper接口
 * T -> 对应model类型
 */
public interface BaseMapper<T> {

	public int save(T t);

	public int update(T t);

	public List<T> findList(@Param("sql") String sql);

	public List<T> findPage(Page page);

	public int count(@Param("tmpSql") String sql);

	public int deleteBySm(SimpleModel del);

	public T findById(@Param("id") Long id);

}
