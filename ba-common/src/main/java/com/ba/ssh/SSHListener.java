package com.ba.ssh;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * @Author: barryw
 * @Description: SSH监听器
 * @Date: 2024/2/22 15:03
 */
@WebListener
@Component
public class SSHListener implements ServletContextListener {

    @Resource
    private SSHConnection connection;

    @Value("${customer.ssh.is-use}")
    private String isUse;

    @Override
    public void contextInitialized(ServletContextEvent arg0) {
        // 服务启动时，创建监听器
        System.out.println("Context initialized ... !");
        if ("F".equals(isUse)) {
            return;
        }
        try {
            // 建立连接
            connection.open();
            System.out.println("\n成功建立SSH连接！\n");
        } catch (Throwable e) {
            e.printStackTrace(); // error connecting SSH server
            System.out.println("\nSSH连接失败！\n");
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        // 服务关闭时，销毁监听器
        System.out.println("Context destroyed ... !");
        if ("F".equals(isUse)) {
            return;
        }
        try {
            // 断开连接
            connection.close(); // disconnect
            System.out.println("\n成功断开SSH连接!\n");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("\n断开SSH连接出错！\n");
        }
    }

}
