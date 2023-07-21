package com.ba.service.impl;

import com.alibaba.nacos.common.utils.IoUtils;
import com.ba.base.UserContext;
import com.ba.config.MinioConfig;
import com.ba.enums.UploadTypeEnum;
import com.ba.model.SysFile;
import com.ba.service.SysFileService;
import com.ba.util.FileUtil;
import com.ba.util.MinioUtils;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.apache.tomcat.jni.FileInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;

/**
 * Minio 文件存储
 */
@Service("Minio")
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

    /**
     * 上传文件
     *
     * @param fileType      文件类型
     * @param type          数据类型：0-base64, 1-multipartFile
     * @param baseData      base64 的数据
     * @param multipartFile multipartFile
     * @return 返回文件信息
     */
    @Override
    public SysFile uploadFile(Integer fileType, Integer type, String baseData, MultipartFile multipartFile, String prefix) {
//        Integer uploadType = nacosConfig.getUploadType();
        Integer uploadType = 6;

        SysFile file = new SysFile();
        String originalFilename = multipartFile.getOriginalFilename();
        String suffix = FileUtil.getSuffix(originalFilename);
        String newFilename = FileUtil.getFileName(UserContext.getUserId(), fileType, suffix, null, true, prefix);

        switch (UploadTypeEnum.get(uploadType)) {
            case OSS:
//                fileInfo = uploadOss(originalFilename, fileInfo, isForever, fileType);
                break;
            case MINIO:
                file = uploadMinio(newFilename, type, baseData, multipartFile);
                break;
            case LOCAL:
                file = uploadLocal();
                break;
            default:
        }

        if (file != null) {
            // 封装返回参数
            file.setName(originalFilename);
            file.setSuffix(suffix);
            file.setNormalUrl(newFilename);
        }
        return file;
    }

    @Override
    public String getBaseUrl() {
//        Integer uploadType = nacosConfig.getUploadType();
        Integer uploadType = 6;

        switch (UploadTypeEnum.get(uploadType)) {
            case OSS:
//                return nacosConfig.getOssBaseUrl();
            case MINIO:
                return minioConfig.getFileHost();
//            case LOCAL:
//                file = uploadLocal();
//                break;
            default:
        }
        return null;
    }

    private SysFile uploadMinio(String newFilename, Integer type, String baseData, MultipartFile multipartFile) {
        SysFile file = new SysFile();
        String fileHost = minioConfig.getFileHost();
        String bucketName = minioConfig.getBucketName();

        try {
            if (type == 0) {
                // 流上传文件
            } else if (type == 1) {
                // MultipartFile 上传文件
                file.setSize(multipartFile.getSize());
                MinioUtils.uploadFile(minioConfig.getBucketName(), multipartFile, newFilename, multipartFile.getContentType());
            } else {
                return null;
            }
    //        IoUtils.closeQuietly(inputStream);

            return file;
        } catch (Exception e) {
//            log.error("MINIO_文件上传失败");
            e.printStackTrace();
        } finally {
//            if (inputStream != null) {
//                try {
//                    inputStream.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
        }
        return null;
    }

    private SysFile uploadLocal() {
        SysFile file = new SysFile();
        return file;
    }

}
