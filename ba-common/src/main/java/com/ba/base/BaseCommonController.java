package com.ba.base;

import com.ba.response.ResData;

/**
 * 通用 controller
 */
public interface BaseCommonController<T, V extends BasePage> {

    public ResData add(T t);

    public ResData edit(T t);

    public ResData findById(Long modelId);

    public ResData deleteById(Long modelId);

    public ResData findPage(V v);

}
