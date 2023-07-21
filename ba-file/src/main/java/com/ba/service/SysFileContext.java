package com.ba.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Spring会自动地将 @Service的类 注入到 serviceMap 中
 * 在启动后，serviceMap 中就存在两个元素，("MinioSysFileServiceImpl", MinioSysFileServiceImpl)与("LocalSysFileServiceImpl", LocalSysFileServiceImpl )
 */
@Service
public class SysFileContext {

    @Resource
    private Map<String, SysFileService> serviceMap;

    public SysFileService getService(String type) {
        return serviceMap.get(type);
    }

}
