package com.ba.ssh;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @Author: barryw
 * @Description: SSH连接服务器工具类
 * @Date: 2024/2/22 14:56
 */
@Component
public class SSHConnection {

    @Value("${customer.ssh.username}")
    private String username;

    @Value("${customer.ssh.password}")
    private String password;

    @Value("${customer.ssh.host}")
    private String host;

    @Value("${customer.ssh.port}")
    private int port;

    @Value("${customer.ssh.auth-type}")
    private String authType;

    @Resource
    private Environment environment;

    private Session session = null;

    public Session open() throws JSchException {
        JSch jsch = new JSch();
        if ("PUBLIC_KEY".equals(authType)) {
            jsch.addIdentity(password); //直接用私钥文件名
        }
        session = jsch.getSession(username, host, port);
        session.setConfig("StrictHostKeyChecking", "no");
        //指定验证方式
        switch (authType) {
            case "PASSWORD":
                session.setPassword(password);
                break;
            case "PUBLIC_KEY":
                session.setConfig("PreferredAuthentications", "publickey");
                break;
        }
        session.connect();

        // mysql
        String mysqlHost = environment.getProperty("customer.mysql.host"); // 本地IP
        String mysqlPort = environment.getProperty("customer.mysql.port"); // 本地端口
        assert mysqlPort != null;
        session.setPortForwardingL(Integer.parseInt(mysqlPort), mysqlHost, Integer.parseInt(mysqlPort));

        // redis
        String redisHost = environment.getProperty("customer.redis.host"); // 本地IP
        String redisPort = environment.getProperty("customer.redis.port"); // 本地端口
        assert redisPort != null;
        session.setPortForwardingL(Integer.parseInt(redisPort), redisHost, Integer.parseInt(redisPort));
        return session;
    }

    /**
     *    断开SSH连接
     */
    public void close() throws Exception {
        session.disconnect();
    }
}
