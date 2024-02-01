package com.ba.vo;

import com.ba.base.UserInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserInfoVO extends UserInfo {

    private Map<String, Object> menus;

}
