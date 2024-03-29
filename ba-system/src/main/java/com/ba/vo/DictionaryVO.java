package com.ba.vo;

import com.ba.model.system.Dictionary;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class DictionaryVO extends Dictionary {

    private List<DictionaryVO> children = new ArrayList<>();

}
