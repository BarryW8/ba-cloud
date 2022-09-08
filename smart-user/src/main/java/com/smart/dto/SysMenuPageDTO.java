package com.smart.dto;

import com.smart.base.BasePageDTO;
import lombok.Data;

@Data
public class SysMenuPageDTO extends BasePageDTO {

    private Integer isHide;

    private Integer linkType;

}
