package com.ba.enums;

public enum PermissionEnum {
    VIEW("0", "查询", "VIEW"),
    ADD("1", "新增", "Add"),
    EDIT("2", "编辑", "Edit"),
    DELETE("3", "删除", "Delete"),
    AUTH("4", "授权", "AUTH"),
    IMPORT("5", "导入", "Import"),
    EXPORT("6", "导出", "Export"),
    REFRESH("7", "刷新", "Refresh"),
    AUTH_ROLE("8", "授权角色", "Authorization Role"),
    AUTH_USER("9", "授权用户", "Authorization User"),
    RESET_PASSWORD("10", "重置密码", "Reset Password"),
    MOVE("11", "移动", "Move"),
    ;
    String code;
    String chName;
    String enName;

    public String getCode() {
        return code;
    }

    public String getChName() {
        return chName;
    }

    public String getEnName() {
        return enName;
    }

    PermissionEnum(String code, String chName, String enName) {
        this.code = code;
        this.chName = chName;
        this.enName = enName;
    }
}
