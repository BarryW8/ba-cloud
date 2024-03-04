package com.ba.vo;

import com.ba.model.system.OrgManagement;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class OrgManagementVO extends OrgManagement {

    private List<OrgManagementVO> children = new ArrayList<>();

}
