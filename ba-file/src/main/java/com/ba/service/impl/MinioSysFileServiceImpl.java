package com.ba.service.impl;

import com.alibaba.nacos.common.utils.IoUtils;
import com.ba.config.MinioConfig;
import com.ba.service.SysFileService;
import com.ba.util.MinioUtils;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.InputStream;

/**
 * Minio 文件存储
 */
@Service
public class MinioSysFileServiceImpl implements SysFileService {

    @Resource
    private MinioConfig minioConfig;

    /**
     * Minio文件上传接口
     *
     * @param file 上传的文件
     * @return 访问地址
     * @throws Exception
     */
    @Override
    public String uploadFile(MultipartFile file) throws Exception {
        String fileName = file.getOriginalFilename();
        MinioUtils.uploadFile(minioConfig.getBucketName(), file, fileName, file.getContentType());
//        IoUtils.closeQuietly(inputStream);
        //返回图片链接
        return minioConfig.getFileHost() + "/" + minioConfig.getBucketName() + "/" + fileName;
    }

}
