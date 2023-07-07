package com.ba.vo;

import com.ba.model.system.SysMenu;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class SysMenuVO extends SysMenu {

    private String treeId;

    private List<SysMenuVO> children = new ArrayList<>();

}
