package com.ba.base;

import java.util.List;
import java.util.Map;

/**
 * 通用service接口
 * T->对应model类型
 */
public interface BaseService<T> {

    public int insert(T t);

    public int update(T t);

    public T findById(Long modelId);

    public int deleteBySm(SimpleModel simpleModel);

    public List<T> findList(String condition);

    public PageView<T> findPage(Map<String, Object> map);

}
