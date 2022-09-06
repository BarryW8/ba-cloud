package com.smart.vo;

import com.smart.model.user.Dictionary;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class DictionaryVO extends Dictionary {

    private List<DictionaryVO> children = new ArrayList<>();

}
