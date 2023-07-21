package com.ba.enums;

public enum OperationEnum {
    OTHER("-1", "其他"),
    VIEW("0", "查询"),
    ADD("1", "新增"),
    EDIT("2", "编辑"),
    DELETE("3", "删除"),
    AUTH("4", "授权"),
    IMPORT("5", "导入"),
    EXPORT("6", "导出"),
    REFRESH("7", "刷新"),
    RESET_PASSWORD("10", "重置密码"),
    MOVE("11", "移动"),
    ;
    String code;
    String name;

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    OperationEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
