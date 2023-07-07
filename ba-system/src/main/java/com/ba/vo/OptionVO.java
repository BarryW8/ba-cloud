package com.ba.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OptionVO implements Serializable {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 编号
     */
    private String code;

    /**
     * 名称
     */
    private String name;
}
