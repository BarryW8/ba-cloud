package com.ba.vo;

import com.ba.model.system.Dictionary;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DictionaryVO extends Dictionary {

    private List<DictionaryVO> children = new ArrayList<>();

}
