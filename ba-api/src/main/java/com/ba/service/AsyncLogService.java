package com.ba.service;

import com.ba.model.system.SysOperLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 异步调用日志服务
 *
 * @author ruoyi
 */
@Service
public class AsyncLogService {

    @Autowired
    private RemoteLogService remoteLogService;

    /**
     * 保存系统日志记录
     */
    @Async
    public void add(SysOperLog sysOperLog) throws Exception {
        remoteLogService.add(sysOperLog);
    }

}
