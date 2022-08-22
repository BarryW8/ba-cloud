package com.smart.base;

import java.util.List;

/**
 * 通用service接口
 * T->对应model类型
 */
public interface BaseService<T> {

    public int save(T t);

    public int update(T t);

    public T findById(Long modelId);

    public int deleteBySm(SimpleModel simpleModel);

    public List<T> findList(String condition);

    public PageView<T> findPage(int page, int pageSize, String sql, String params);

    public int nameUnique(Long modelId, String name);
}
