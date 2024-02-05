package com.ba.base;

import java.util.List;
import java.util.Map;

/**
 * 通用service接口
 * T->对应model类型
 */
public interface BaseService<T> {

    public int add(T t);

    public int edit(T t);

    public T findById(Long modelId);

    public int deleteBySm(SimpleModel simpleModel);

    public List<T> findList(Map<String, Object> map);

    public PageView<T> findPage(Map<String, Object> map);

    /**
     * 不允许从外部传值使用，存在SQL注入问题
     * @param sql sql语句
     * @return 数据列表
     */
    public List<T> findListBySQL(String sql);

}
