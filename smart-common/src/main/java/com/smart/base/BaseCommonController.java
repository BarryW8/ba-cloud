package com.smart.base;

import com.smart.util.JsonData;

/**
 * 通用 controller
 */
public interface BaseCommonController<T, V extends BasePageDTO> {

    public JsonData save(T t);

    public JsonData findById(Long modelId);

    public JsonData deleteById(Long modelId);

    public JsonData findList(String condition);

    public JsonData findPage(V v);

    public JsonData nameUnique(Long modelId, String name);
}
