package com.ba.token;

import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSON;
import com.ba.cache.CacheConstant;
import com.ba.base.UserInfo;
import com.ba.enums.TokenEnum;
import com.ba.util.CommonUtils;
import com.ba.util.DesUtils;
import com.ba.util.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class TokenManage {

    //用于md5加密的后端秘钥
    @Value(value = "${customer.encryption-salt:qhSpomParking}")
    public String ENCRYPTION_SALT;

    @Resource
    private RedisCache redisCache;

    /**
     * 创建token
     *
     * @param platform    平台类型 ios andorid win mac linux
     * @param appType     请求客户端类型 web、app、小程序等
     * @param deviceId    设备ID
     * @param userId      用户id
     * @param userId      临时用户id  存缓存的temp,不宜过长是可变的  建议是 userId+三位随机数
     * @param roleId      角色id
     * @param password10L 密码的前十位，用于处理修改密码自动下线
     * @param expire      过期时间
     * @return token
     */
    public String createToken(
            UserInfo userInfo, String platform, String appType,
            String deviceId, String userId, String tempUserId,
            String roleId, String password10L, Long expire) {
        log.info("createToken_生成token: platform={},appType={},deviceId={},userId={},tempUserId={},roleId={},password10L={},expire={}",
                platform, appType, deviceId, userId, tempUserId, roleId, password10L, expire);
        //根据临时tempUserId 控制可变的秘钥
        String secret = SecureUtil.md5(ENCRYPTION_SALT + platform + appType + deviceId + tempUserId);
        String content = platform + "#" + appType + "#" + deviceId + "#" + userId + "#" + roleId + "#" + password10L + "#" + expire + "#" + System.currentTimeMillis();
        String token = DesUtils.encrypt(content, secret);
        userInfo.setToken(token);
        String redisKey = String.format(CacheConstant.CACHE_STR_KEY_TOKEN_KEY, appType, tempUserId);
        //将生成token存入到缓存中
        boolean b = redisCache.setKeyValue(redisKey, JSON.toJSONString(userInfo), expire, TimeUnit.SECONDS);
        if (!b) {
            log.error("缓存服务器异常 b={}", b);
            return null;
        }
        return token;
    }

    /**
     * 获取redis_token,判断是否已过期
     */
    public String getToken(String appType, String tempUserId) {
        log.info("getToken_获取token: appType={},tempUserId={}", appType, tempUserId);
        String redisKey = String.format(CacheConstant.CACHE_STR_KEY_TOKEN_KEY, appType, tempUserId);
        String value = redisCache.getKeyValue4Object(redisKey, String.class);
        if (!StringUtils.hasLength(value)) {
            log.error("token 已过期 r={}", value);
            return null;
        }
        return value;
    }

    /**
     * token  重新生成token 用于APP续时
     */
    public boolean tokenAutoRenew(String platform, String appType, String deviceId, String tempUserId, Long expire, String token) throws IOException {
        log.info("tokenRenew_续时token: platform={},appType={},deviceId={},tempUserId={},token={},expire={}",
                platform, appType, deviceId, tempUserId, token, expire);
        String secret = SecureUtil.md5(ENCRYPTION_SALT + platform + appType + deviceId + tempUserId);

        String decrypt = DesUtils.decode(token, secret);
        if (!StringUtils.hasLength(decrypt) || !decrypt.contains("#")) {
            log.error("tokenRenew_续时token: platform={},appType={},deviceId={},token={},expire={}",
                    platform, appType, deviceId, token, expire);
            log.error("tokenRenew_非法token->续时");
            return false;
        }
        String redisKey = String.format(CacheConstant.CACHE_STR_KEY_TOKEN_KEY, appType, tempUserId);
        //将生成token的秘钥存入到缓存中
        boolean expire1 = redisCache.expire(redisKey, expire, TimeUnit.SECONDS);
        return expire1;
    }

    /**
     * token  redis 通用续时
     */
    public void tokenRenew(String platform, String appType, String tempUserId, long expire) {
        log.info("tokenRenew_通用续时token: platform={},appType={},expire={}",
                platform, appType, expire);
        String redisKey = String.format(CacheConstant.CACHE_STR_KEY_TOKEN_KEY, appType, tempUserId);
        //将生成token的秘钥存入到缓存中
        redisCache.expire(redisKey, expire, TimeUnit.SECONDS);
    }

    /**
     * 解析token
     */
    public TokenModel parseToken(String token, String platform, String appType, String deviceId, String tempUserId) throws IOException {
        log.info("parseToken_解析token: platform={},appType={},deviceId={},tempUserId={},token={}",
                platform, appType, deviceId, tempUserId, token);
        String secret = SecureUtil.md5(ENCRYPTION_SALT + platform + appType + deviceId + tempUserId);
        String decrypt = DesUtils.decode(token, secret);
        if (!StringUtils.hasLength(decrypt) || !decrypt.contains("#")) {
            log.error("parseToken_非法token->解析");
            return null;
        }
        //platform + "#" + appType + "#" + deviceId + "#" + userId + "#"+ roleId + "#" +password10L+ "#" + expire + "#" + System.currentTimeMillis();
        TokenModel model = new TokenModel();
        String[] desArray = decrypt.split("#");
        model.setPlatform(desArray[0]);
        model.setAppType(desArray[1]);
        model.setDeviceId(desArray[2]);
        model.setUserId(desArray[3]);
        model.setRoleId(desArray[4]);
        model.setPassword10L(desArray[5]);
        model.setExpire(desArray[6]);
        model.setTimestamp(desArray[7]);
        return model;
    }

    /**
     * 清除token
     * key = appType:deviceId
     */
    public boolean deleteToken(String appType, String tempUserId) {
        log.info("deleteToken_删除token: appType={}", appType);
        String redisKey = String.format(CacheConstant.CACHE_STR_KEY_TOKEN_KEY, appType, tempUserId);
        return redisCache.deleteKeyValue(redisKey);
    }

    /**
     * web token 更新信息
     */
    public boolean updateWebToken(String appType, String tempUserId, UserInfo userInfo) {
        log.info("updateToken_更新token: appType={},tempUserId={}", appType, tempUserId);
        String redisKey = String.format(CacheConstant.CACHE_STR_KEY_TOKEN_KEY, appType, tempUserId);
        return redisCache.setKeyValue(redisKey, JSON.toJSONString(userInfo), TokenEnum.WEB.getTime(), TimeUnit.SECONDS);
    }

    /**
     * token 更新信息
     */
    public void updateToken(String appType, String tempUserId, UserInfo userInfo) {
        log.info("updateToken_更新token: appType={},tempUserId={}",
                appType, tempUserId);
        String redisKey = String.format(CacheConstant.CACHE_STR_KEY_TOKEN_KEY, appType, tempUserId);
        // 获取所有key
        Set<String> keys = redisCache.getKeys(redisKey);
        for (String k : keys) {
            // 获取token
            String parse = redisCache.getKeyValue(k);
            UserInfo cacheInfo = JSON.parseObject(parse, UserInfo.class);
            userInfo.setToken(cacheInfo.getToken());
            // 获取过期时间
            Long expire = redisCache.getExpire(k, TimeUnit.SECONDS);
            // 更新缓存
            boolean result = redisCache.setKeyValue(k, JSON.toJSONString(userInfo), expire, TimeUnit.SECONDS);
            if (!result) {
                log.error("缓存服务器异常 k={},userInfo={}", k, JSON.toJSONString(userInfo));
                return;
            }
        }
    }
}
