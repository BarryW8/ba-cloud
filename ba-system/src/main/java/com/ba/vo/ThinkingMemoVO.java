package com.ba.vo;

import com.ba.model.system.SysMenu;
import com.ba.model.system.ThinkingMemo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class ThinkingMemoVO extends ThinkingMemo {

    private String treeId;

    private List<ThinkingMemoVO> children = new ArrayList<>();

}
