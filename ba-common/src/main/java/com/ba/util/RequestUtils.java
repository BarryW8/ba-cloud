package com.ba.util;

import com.alibaba.nacos.shaded.com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * 请求工具类
 */
@Slf4j
public class RequestUtils {

    private static final String UNKNOWN = "unknown";

    /**
     * TOKEN_COOKIE
     */
    public static final String TOKEN_COOKIE = "token";

    /**
     * TOKEN_HEADER
     */
    public static final String TOKEN_HEADER = "Authorization";

    /**
     * DEVICE_ID
     */
    public static final String DEVICE_ID = "x-http-device";

    /**
     * APP_TYPE
     */
    public static final String APP_TYPE = "x-http-app";

    /**
     * APP_TYPE
     */
    public static final String PLATFORM = "x-http-os";

    /**
     * USER_ID  请求里面MD5加密
     */
    public static final String USER_ID = "userId";

    /**
     * mac地址
     */
    public static final String TOKEN_MAC_ADDRESS = "macAddress";

    /**
     * 基础版本
     */
    public static final String TOKEN_BASE_VERSION = "baseVersion";

    /**
     * wgt版本
     */
    public static final String TOKEN_WGT_VERSION = "wgtVersion";

    public static String getUserAgent(HttpServletRequest request) {
        return request.getHeader("User-Agent");
    }

    public static String getToken() {
        // token先从参数中获取，取不到就从hearder-Authorization取
        HttpServletRequest req = getRequest();
        String token = req.getParameter(TOKEN_COOKIE);
        // or header
        if (StringUtils.isEmpty(token)) {
            token = req.getHeader(TOKEN_HEADER);
            if (StringUtils.isEmpty(token)) {
                token = req.getHeader(TOKEN_HEADER.toLowerCase());
                if (StringUtils.isEmpty(token)) {
                    token = req.getHeader(TOKEN_COOKIE);
                    if (StringUtils.isEmpty(token)) {
                        token = req.getHeader(TOKEN_COOKIE.toLowerCase());
                    }
                }
            }
        }
        return Strings.emptyToNull(token);
    }

    public static Optional<String> getToken(HttpServletRequest request) {
        String token = request.getParameter(TOKEN_COOKIE);
        if (StringUtils.isEmpty(token)) {
            token = request.getHeader(TOKEN_HEADER);
        }
        return Optional.ofNullable(Strings.emptyToNull(token));
    }

    /**
     * 获取用户真实IP地址，不使用request.getRemoteAddr();的原因是有可能用户使用了代理软件方式避免真实IP地址,
     * <p>
     * 可是，如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP值，究竟哪个才是真正的用户端的真实IP呢？
     * 答案是取X-Forwarded-For中第一个非unknown的有效IP字符串。
     * <p>
     * 如：X-Forwarded-For：192.168.124.110, 192.168.124.120, 192.168.124.130,
     * 192.168.124.100
     * <p>
     * 用户真实IP为： 192.168.124.110
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (StringUtils.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }

        if (StringUtils.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (StringUtils.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (StringUtils.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 将请求转换为curl 形式，客户端可以模拟请求
     */
    public static StringBuilder getCurlUrl(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        if (Objects.isNull(request)) {
            return sb;
        }
        Map<String, String[]> params = request.getParameterMap();
        int keyMaxLen = 10;
        for (String key : params.keySet()) {
            keyMaxLen = Math.max(keyMaxLen, key.length());
        }
        String fmt = "%" + keyMaxLen + "s  %s\n";

        sb.append("REQ: ");
        sb.append(getIpAddress(request))
                .append(":")
                .append(request.getRemotePort())
                .append(" -> ")
                .append(request.getLocalAddr())
                .append("\n");
        sb.append("curl -X ")
                .append(request.getMethod());
        String pt = request.getProtocol();

        sb.append(" \\\n");
        sb.append("-v '")
                .append(StringUtils.escapeQuotedString(getUrl(request)))
                .append("' \\\n");
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headName = headerNames.nextElement();
            sb.append("-H '")
                    .append(StringUtils.escapeQuotedString(headName))
                    .append(":")
                    .append(StringUtils.escapeQuotedString(request.getHeader(headName)))
                    .append("' \\\n");
        }

        String content = getRequestContent(request);
        if (StringUtils.isEmpty(content)) {
            sb.append("-d ''");
        } else {
            sb.append("-d '")
                    .append(content)
                    .append("'");
        }
        sb.append(" \n\n");

        if (!params.isEmpty()) {
            sb.append("PARAM: \n");
            params.forEach((key, strings) -> {
                for (String s : strings) {
                    sb.append(String.format(fmt, key, s));
                }
            });
        }
        return sb;
    }

    /**
     * 凭借Http 请求URL，含参数； 如http://www.xxx.com?a=1
     */
    public static String getUrl(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder(request.getRequestURL());
        if (StringUtils.isNotEmpty(request.getQueryString())) {
            sb.append("?")
                    .append(request.getQueryString());
        }
        return sb.toString();
    }

    /**
     * 获取请求内容中的字符串
     */
    private static String getRequestContent(HttpServletRequest request) {
        try (BufferedReader br = request.getReader()) {
            StringBuilder sb = new StringBuilder();
            String tmp;
            while ((tmp = br.readLine()) != null) {
                sb.append(tmp);
            }
            return sb.toString();
        } catch (Exception e) {
            return "";
        }
    }

    /***
     * 获取requset
     * @return
     */
    public static HttpServletRequest getRequest() {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        if (null == ra) {
            return null;
        }
        HttpServletRequest request = ((ServletRequestAttributes) ra).getRequest();
        return request;
    }

    /***
     * 获取requset
     * @return
     */
    public static HttpServletResponse getResponse() {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = ((ServletRequestAttributes) ra).getResponse();
        return response;
    }

    /***
     * 获取requset中属性值
     * @return
     */
    public static String getRequestAtributeValue(String key) {
        HttpServletRequest request = getRequest();
        return (String) request.getAttribute(key);
    }


    /***
     * 获取requset中属性值
     * @return
     */
    public static void setRequestAtributeValue(String key, String val) {
        HttpServletRequest request = getRequest();
        request.setAttribute(key, val);
    }

    /***
     * 获取requset中属性值
     * @return
     */
    public static Map getRequestHeader() {
        HttpServletRequest request = getRequest();
        Map<String, String> map = new HashMap<String, String>();
        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }
        return map;
    }

    public static String getHeader(String headerName, HttpServletRequest request) {
        // token先从参数中获取，取不到就从hearder-Authorization取
        String headerValue = request.getHeader(headerName);
        // or header
        if (StringUtils.isEmpty(headerValue)) {
            headerValue = request.getHeader(headerName.toLowerCase());
        }
        return headerValue;
    }
}
