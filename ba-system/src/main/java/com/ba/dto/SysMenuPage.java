package com.ba.dto;

import com.ba.base.BasePage;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SysMenuPage extends BasePage {

    private Integer isHide;

}
