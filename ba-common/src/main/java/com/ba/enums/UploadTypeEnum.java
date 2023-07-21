package com.ba.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

/**
 * 文件上传方式
 */
public enum UploadTypeEnum {
    OSS(1),
    OBS(2),
    S3(3),
    LOCAL(4),
    FASTDFS(5),
    MINIO(6),
    ;

    @Getter
    private int code;

    UploadTypeEnum(int code) {
        this.code = code;
    }

    public static UploadTypeEnum get(Integer gCode) {
        if (gCode == null) return null;
        Optional<UploadTypeEnum> resultOps = Arrays.stream(UploadTypeEnum.values()).filter(t -> t.code == gCode).findFirst();
        return resultOps.get();
    }

}
