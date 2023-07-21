package com.ba.util;

import cn.hutool.crypto.SecureUtil;
import org.apache.commons.lang3.StringUtils;

public class FileUtil extends cn.hutool.core.io.FileUtil {

    /**
     * 生成文件名，以uuid命名
     *
     * @param userId    用户id
     * @param fileType  文件类型
     * @param suffix    后缀
     * @param fixedName 固定名称，如果有固定名称，使用这个名称，如果没有，生成uuid的名称
     * @param needDate  是否需要日期做为目录，在断点续传的时候，目录不能加日期，因为可能会导致临界值问题
     * @param prefix    前缀
     * @return 新的文件名 文件类型/日期/userId/prefix+UUID.suffix
     */
    public static String getFileName(Long userId, Integer fileType, String suffix, String fixedName, boolean needDate, String prefix) {
        StringBuilder fileName = new StringBuilder();
        fileName.append(fileType).append("/");
        if (needDate) {
            fileName.append(CommonUtils.getFirstSecondOfDayToSecond(System.currentTimeMillis())).append("/");
        }
        if (userId != null) {
            fileName.append(SecureUtil.md5(userId + "")).append("/");
        }
        if (org.apache.commons.lang3.StringUtils.isNotBlank(prefix)) {
            fileName.append(prefix);
        }
        if (org.apache.commons.lang3.StringUtils.isNotBlank(fixedName)) {
            fileName.append(fixedName);
        } else {
            fileName.append(CommonUtils.getUUID());
        }
        if (StringUtils.isNotBlank(suffix)) {
            fileName.append(".").append(suffix);
        }
        return fileName.toString();
    }

}
