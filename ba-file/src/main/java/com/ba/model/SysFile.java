package com.ba.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

/**
 * 文件信息
 */
@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class SysFile implements Serializable {

    /**
     * 文件名称
     */
    private String name;

    /**
     * 文件地址
     */
    private String normalUrl;

    /**
     * 缩略图地址，大于1M（可配置）的图片、所有视频，会有此字段
     */
    private String thumbUrl;

    /**
     * 文件后缀
     */
    private String suffix;

    /**
     * 文件大小，单位：字节
     */
    private Long size;

    /**
     * 时长（音频文件时会有此字段，单位：秒）
     */
    private Long duration;

    /**
     * 分片上传完成标志，为true时，表示断点上传的文件全部，才会有文件地址（断点上传时会有此字段）
     */
    private Boolean chunkFinish = true;

    /**
     * 其他扩展信息
     */
    private String ext;

    /**
     * 指定大小缩略图地址
     */
    private String compressUrl;

}
