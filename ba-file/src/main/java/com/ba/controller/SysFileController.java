package com.ba.controller;

import com.ba.config.MinioConfig;
import com.ba.enums.UploadTypeEnum;
import com.ba.model.SysFile;
import com.ba.response.ResData;
import com.ba.service.SysFileContext;
import com.ba.service.SysFileService;
import com.ba.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.jni.FileInfo;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

/**
 * 文件请求处理
 */
@Slf4j
@RestController
public class SysFileController {

    @Resource
    private SysFileContext sysFileContext;

    @Resource
    private MinioConfig minioConfig;

    /**
     * 获取文件域名 baseUrl
     */
    @GetMapping("getBaseUrl")
    public ResData getBaseUrl() {
        SysFileService sysFileService = sysFileContext.getService("Minio");
        String baseUrl = sysFileService.getBaseUrl();
        if (StringUtils.isNotEmpty(baseUrl)) {
            return ResData.success(baseUrl);
        }
        return ResData.error("获取文件域名失败");
    }

    /**
     * 文件上传请求
     */
//    @PostMapping("upload")
//    public ResData upload(MultipartFile file) {
//        try {
//            // 上传并返回访问地址
//            String url = sysFileService.uploadFile(file);
//            SysFile sysFile = new SysFile();
////            sysFile.setName(FileUtils.getName(url));
//            sysFile.setNormalUrl(url);
//            return ResData.success(sysFile);
//        } catch (Exception e) {
//            log.error("上传文件失败", e);
//            return ResData.error(e.getMessage());
//        }
//    }

    /**
     * 文件上传请求
     */
    @PostMapping(value = "upload", headers = "Content-Type=multipart/form-data")
    public ResData upload(@RequestParam Integer fileType,
                          @RequestParam MultipartFile file) {
        long start = System.currentTimeMillis();
        log.info("########### file start time -----》" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(new Date()));

        try {
            SysFileService sysFileService = sysFileContext.getService("Minio");
            SysFile sysFile = sysFileService.uploadFile(fileType, 1, null, file, null);
            log.info("########### file end time -----》" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(new Date()));
            log.info("########### file total time -----》" + (System.currentTimeMillis() - start));
            if (sysFile == null) {
                return ResData.error("上传失败");
            }
            return ResData.success(sysFile);
        } catch (Exception e) {
            log.error("上传文件失败", e);
            return ResData.error(e.getMessage());
        }
    }

    @GetMapping("/showFile/{fileType}/{fileName:.+}")
    public void showFile(@PathVariable String fileType, @PathVariable String fileName, HttpServletRequest request, HttpServletResponse response) {
        showFileData(fileType,null, null, fileName, request, response);
    }

    /**
     * 获取文件，后续为了同处理文件显示控制，例如一些关键的核心资产
     */
    @GetMapping("/showFile/{fileType}/{timestamp}/{fileName:.+}")
    public void showFile(@PathVariable String fileType, @PathVariable String timestamp, @PathVariable String fileName, HttpServletRequest request, HttpServletResponse response) {
        showFileData(fileType, null,timestamp, fileName, request, response);
    }

    /**
     * 获取文件，后续为了同处理文件显示控制，例如一些关键的核心资产
     */
    @GetMapping("/showFile/{fileType}/{fileDirectory}/{timestamp}/{fileName:.+}")
    public void showFile(@PathVariable String fileType,@PathVariable String fileDirectory, @PathVariable String timestamp, @PathVariable String fileName, HttpServletRequest request, HttpServletResponse response) {
        showFileData(fileType,fileDirectory, timestamp, fileName, request, response);
    }

    private void showFileData(String fileType, String fileDirectory,String timestamp, String fileName, HttpServletRequest request, HttpServletResponse response) {
        //后续可进行按类型开关文件访问
        //        Integer uploadType = nacosConfig.getUploadType();
        Integer uploadType = 6;

        String baseUrl = "";
        switch (UploadTypeEnum.get(uploadType)) {
            case OSS:
//                return nacosConfig.getOssBaseUrl();
            case MINIO:
                baseUrl = minioConfig.getEndpoint() + "/" + minioConfig.getBucketName();
//            case LOCAL:
//                file = uploadLocal();
//                break;
            default:
        }

        //处理人才房的权限控制
        String fileUrl = baseUrl + "/" + fileType;
        if (StringUtils.isNotEmpty(fileDirectory)) {
            fileUrl = fileUrl + "/" + fileDirectory;
        }
        if (StringUtils.isNotEmpty(timestamp)) {
            fileUrl = fileUrl + "/" + timestamp;
        }
        if (StringUtils.isNotEmpty(fileName)) {
            fileUrl = fileUrl + "/" + fileName;
        }
        //先获取路径
        String url = request.getRequestURL().toString();
        log.info("url={},fileUrl={}", url, fileUrl);
        URL urlfile;
        HttpURLConnection httpUrl;
        BufferedInputStream bis = null;
        ServletOutputStream bos = null;
        try {
            bos = response.getOutputStream();
            urlfile = new URL(fileUrl);
            httpUrl = (HttpURLConnection) urlfile.openConnection();
            // 转发前端请求头到minio
            setRangeRequestHeaders(httpUrl, request);
            httpUrl.connect();
            bis = new BufferedInputStream(httpUrl.getInputStream());

            Map<String, List<String>> headerFields = httpUrl.getHeaderFields();
            headerFields.forEach((k, v) -> {
                if (StringUtils.isNotEmpty(k) && v.size() > 0) {
                    response.setHeader(k, v.get(0));
                }
            });
            int contentLength = httpUrl.getContentLength();
            response.setContentLength(contentLength);
            response.setContentLengthLong(httpUrl.getContentLengthLong());
            response.setContentType(httpUrl.getContentType());
            // 回写minio响应状态，一律返回200有问题，有时候会出现206、304属正常现象
            response.setStatus(httpUrl.getResponseCode());
            int len = 1024;
            byte[] b = new byte[len];
            boolean hasData = false;
            while ((len = bis.read(b)) != -1) {
                bos.write(b, 0, len);
                if (len > 0) {
                    hasData = true;
                }
            }
            // 根据minio状态返回
//                if (!hasData) {
//                    response.setContentType("text/html;charset=UTF-8");
//                    String s = "文件不存在";
//                    bos.write(s.getBytes(StandardCharsets.UTF_8));
//                }
            // 设置请求头应当在write前，此处无效，同时应当判断Range请求头
//                response.setHeader("Content-Range", "bytes 0-" + (contentLength) + "/" + contentLength);
            bos.flush();
            bis.close();
            httpUrl.disconnect();
        } catch (Exception e) {
            try {
                response.setContentType("text/html;charset=UTF-8");
                String s = "文件不存在";
                bos.write(s.getBytes(StandardCharsets.UTF_8));
                bos.flush();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            log.error("url={},fileUrl={}", url, fileUrl);
            log.error("访问文件异常 e.getMessage()={}", e.getMessage());
//                e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    /**
     * 支持Range协议，minio本身支持range协议，此处设置请求头转发。修复视频回放（二次播放）、视频滑动滚动条播放等异常问题
     * Range: 请求头，表示期望的下载范围，值的格式为"bytes=范围或范围列表"。
     * 如："1-2"、"3-"、"-3"、"1-2,3-4"、"1-2,3-"、"1-2,-3"，闭区间、至少须有一个范围、允许指定多个范围、左右边界未成对出现的范围最多只能有一个且只能在末尾
     * http 206 Partial Content 断点下载，同时会服务器会返回
     * 1. Accept-Ranges：响应头，表示返回的数据的单位，通常为"bytes"
     * 2. Content-Range: bytes 32768-365120/365121
     * http 304 Not Modified
     * 根据前端传入的If-None-Match、If-Modified-Since与服务器响应的Etag和Last-Modified判断资源是否变化，不是错误请求
     * http 416 Requested range not satisfiable 范围错误
     *
     * @param httpUrl minio链接
     * @param request 前端http请求
     */
    private void setRangeRequestHeaders(HttpURLConnection httpUrl, HttpServletRequest request) {
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            if (org.apache.commons.lang3.StringUtils.equalsAnyIgnoreCase(headerName,
                    HttpHeaders.RANGE, HttpHeaders.IF_RANGE, HttpHeaders.IF_NONE_MATCH, HttpHeaders.IF_MODIFIED_SINCE
            )) {
                httpUrl.setRequestProperty(headerName, request.getHeader(headerName));
            }
        }
    }
}
