package com.ba.service;

import com.ba.model.system.SysOperLog;
import com.ba.response.ResData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 日志服务
 */
//@FeignClient(value = "ba-system")
//@FeignClient(value = "localhost:8100/system")
@FeignClient(name = "ba-system", url = "localhost:8100/system")
public interface RemoteLogService {
    /**
     * 保存系统日志
     */
    @PostMapping("/sysOperLog/add")
    public ResData add(@RequestBody SysOperLog sysOperLog);

//    /**
//     * 保存访问记录
//     *
//     * @param sysLogininfor 访问实体
//     * @param source 请求来源
//     * @return 结果
//     */
//    @PostMapping("/logininfor")
//    public ResData saveLogininfor(@RequestBody SysLogininfor sysLogininfor, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}
