package com.ba.controller;

import com.ba.model.SysFile;
import com.ba.response.ResData;
import com.ba.service.SysFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * 文件请求处理
 */
@Slf4j
@RestController
public class SysFileController {

    @Resource
    private SysFileService sysFileService;

    /**
     * 文件上传请求
     */
    @PostMapping("upload")
    public ResData upload(MultipartFile file) {
        try {
            // 上传并返回访问地址
            String url = sysFileService.uploadFile(file);
            SysFile sysFile = new SysFile();
//            sysFile.setName(FileUtils.getName(url));
            sysFile.setUrl(url);
            return ResData.success(sysFile);
        } catch (Exception e) {
            log.error("上传文件失败", e);
            return ResData.error(e.getMessage());
        }
    }
}
