package com.ba.service;

import com.ba.model.SysFile;
import org.apache.tomcat.jni.FileInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传接口
 *
 * @author ruoyi
 */
public interface SysFileService {
    /**
     * 文件上传接口
     *
     * @param file 上传的文件
     * @return 访问地址
     * @throws Exception
     */
    public String uploadFile(MultipartFile file) throws Exception;

    /**
     * 上传文件
     *
     * @param fileType 文件类型：0-其它, 1-用户头像，2-动态，3-聊天
     * @param file     文件对象
     * @return 返回文件信息
     */
    SysFile uploadFile(Integer fileType, Integer type, String originalFilename, MultipartFile file, String prefix);

    String getBaseUrl();

}
