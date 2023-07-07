package com.ba.util;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 缓存管理
 * redis 默认的 hash ziplist 的默认条数512
 * 如果使用  hash 类型，需要确保条数不能过多，如果条目过多请使用 string 类型，建议是500条以内使用 hash,超过了500请使用String类型，
 * 防止hash过大，占用过多的内存，key 数量超过 512条 会造成底层结构的改变
 */
@Component
@Slf4j
public class RedisCache {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public List<Object> hashValues(String cacheKey) {
        return redisTemplate.boundHashOps(cacheKey).values();
    }

    public Set<Object> hashKeys(String cacheKey) {
        return redisTemplate.boundHashOps(cacheKey).keys();
    }

    public Boolean hasKey(String cacheKey) {
        if (StringUtils.isEmpty(cacheKey)) {
            log.error("hasKey key 不能为空");
            return false;
        }
        return redisTemplate.hasKey(cacheKey);
    }

    public boolean setKeyValue(String key, String value, Long timeout, TimeUnit timeUnit) {
        try {
            if (StringUtils.isEmpty(key)) {
                log.error("setKeyValue key 不能为空");
                return false;
            }
            log.info("setKeyValue key={},value={},timeout={},timeUnit={}", key, value, timeout, timeUnit);
            if (timeout == null) {
                redisTemplate.opsForValue().set(key, value);
            } else {
                redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
            }
            return true;
        } catch (Exception e) {
            log.error("setKeyValue key={},value={},timeout={},timeUnit={}", key, value, timeout, timeUnit);
            return false;
        }

    }

    public String getKeyValue(String key) {
        try {
            if (StringUtils.isEmpty(key)) {
                log.error("getKeyValue key 不能为空");
                return null;
            }
            return redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            return null;
        }
    }

    public <T> T getKeyValue4Object(String key, Class<T> clazz) {
        try {
            if (StringUtils.isEmpty(key)) {
                log.error("getKeyValue key 不能为空");
                return null;
            }
            String s = redisTemplate.opsForValue().get(key);
            return JSON.parseObject(s, clazz);
        } catch (Exception e) {
            return null;
        }
    }

    public <T> List<T> getKeyValue4List(String key, Class<T> clazz) {
        try {
            if (StringUtils.isEmpty(key)) {
                log.error("getKeyValue key 不能为空");
                return null;
            }
            String s = redisTemplate.opsForValue().get(key);
            return JSON.parseArray(s, clazz);
        } catch (Exception e) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getKeyValue4BaseObject(String key, Class<T> clazz) {
        try {
            if (StringUtils.isEmpty(key)) {
                log.error("getKeyValue key 不能为空");
                return null;
            }
            String data = redisTemplate.opsForValue().get(key);
            if (data == null) {
                return null;
            }
            if ("Integer".equals(clazz.getName())) {
                Integer result = null;
                try {
                    result = Integer.parseInt(data);
                } catch (Exception e) {
                    log.error("数据转义失败 Integer data={}", data);
                    e.printStackTrace();
                }
                return (T) result;
            } else if ("Long".equals(clazz.getName())) {
                Long result = null;
                try {
                    result = Long.parseLong(data);
                } catch (Exception e) {
                    log.error("数据转义失败 Long data={}", data);
                    e.printStackTrace();
                }
                return (T) result;
            } else if ("Float".equals(clazz.getName())) {
                Float result = null;
                try {
                    result = Float.parseFloat(data);
                } catch (Exception e) {
                    log.error("数据转义失败 Float data={}", data);
                    e.printStackTrace();
                }
                return (T) result;
            } else if ("Short".equals(clazz.getName())) {
                Short result = null;
                try {
                    result = Short.parseShort(data);
                } catch (Exception e) {
                    log.error("数据转义失败 Short data={}", data);
                    e.printStackTrace();
                }
                return (T) result;
            } else if ("Double".equals(clazz.getName())) {
                Double result = null;
                try {
                    result = Double.parseDouble(data);
                } catch (Exception e) {
                    log.error("数据转义失败 Double data={}", data);
                    e.printStackTrace();
                }
                return (T) result;
            } else if ("Boolean".equals(clazz.getName())) {
                Boolean result = null;
                try {
                    result = Boolean.parseBoolean(data);
                } catch (Exception e) {
                    log.error("数据转义失败 Boolean data={}", data);
                    e.printStackTrace();
                }
                return (T) result;
            } else if ("BigDecimal".equals(clazz.getName())) {
                BigDecimal result = null;
                try {
                    result = new BigDecimal(data);
                } catch (Exception e) {
                    log.error("数据转义失败 BigDecimal data={}", data);
                    e.printStackTrace();
                }
                return (T) result;
            } else {
                return (T) data;
            }
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 缓存自增
     *
     * @param key
     * @return
     */
    public Boolean incrementKeyValue(String key) {
        try {
            if (StringUtils.isEmpty(key)) {
                log.error("getKeyValue key 不能为空");
                return false;
            }
            redisTemplate.opsForValue().increment(key);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 缓存自减
     *
     * @param key
     * @return
     */
    public Boolean decrementKeyValue(String key) {
        try {
            if (StringUtils.isEmpty(key)) {
                log.error("getKeyValue key 不能为空");
                return false;
            }
            redisTemplate.opsForValue().decrement(key);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 模糊分页查询  不提供总数，只提供分页的数据
     *
     * @param key 模糊匹配
     * @return
     */
    public List<String> getKeyValuePage(String key, int pageNum, int pageSize) {
        try {
            if (StringUtils.isEmpty(key)) {
                log.error("getKeyValueList key 不能为空");
                return null;
            }
            List<String> result = new ArrayList<>();
            redisTemplate.execute((RedisCallback<Set<Object>>) redisConnection -> {
                Set<Object> keySet = new HashSet<>();
                try (Cursor<byte[]> scan = redisConnection.scan(ScanOptions.scanOptions().match(key).count(1000).build())) {
                    if (scan == null) {
                        log.error("查询 redis scan 连接数用完 出现空指针");
                        return null;
                    }
                    int tmpIndex = 0;
                    int startIndex = (pageNum - 1) * pageSize;  // 开始节点
                    int end = pageNum * pageSize;   // 去redis中找的次数,结束节点
                    while (scan.hasNext()) {
                        if (tmpIndex >= startIndex && tmpIndex < end) {
                            byte[] bytes = redisConnection.get(scan.next());
                            String v = new String(bytes);
                            keySet.add(v);
                            result.add(v);
                        } else {
                            scan.next();
                        }
                        if (tmpIndex >= end) {
                            break;
                        }
                        tmpIndex++;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return keySet;
            });
            return result;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    /**
     * 模糊查询
     *
     * @param key 模糊匹配
     * @return
     */
    public List<String> getKeyValueList(String key) {
        try {
            if (StringUtils.isEmpty(key)) {
                log.error("getKeyValueList key 不能为空");
                return null;
            }
            List<String> result = new ArrayList<>();
            redisTemplate.execute((RedisCallback<Set<Object>>) redisConnection -> {
                Set<Object> keySet = new HashSet<>();
                try (Cursor<byte[]> scan = redisConnection.scan(ScanOptions.scanOptions().match(key).count(1000).build())) {
                    if (scan == null) {
                        log.error("查询 redis scan 连接数用完 出现空指针");
                        return null;
                    }
                    while (scan.hasNext()) {
                        byte[] bytes = redisConnection.get(scan.next());
                        String v = new String(bytes);
                        keySet.add(v);
                        result.add(v);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return keySet;
            });
            return result;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 模糊查询
     *
     * @param key 模糊匹配
     * @return
     */
    public Map<String, String> getKeyValueMap(String key) {
        try {
            if (StringUtils.isEmpty(key)) {
                log.error("getKeyValueMap key 不能为空");
                return null;
            }
            Map<String, String> result = new HashMap<>();
            redisTemplate.execute((RedisCallback<Set<Object>>) redisConnection -> {
                Set<Object> keySet = new HashSet<>();
                try (Cursor<byte[]> scan = redisConnection.scan(ScanOptions.scanOptions().match(key).count(1000).build())) {
                    if (scan == null) {
                        log.error("查询 redis scan 连接数用完 出现空指针");
                        return null;
                    }
                    while (scan.hasNext()) {
                        byte[] next = scan.next();
                        byte[] bytes = redisConnection.get(next);
                        String v = new String(bytes);
                        keySet.add(v);
                        result.put(new String(next), v);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return keySet;
            });
            return result;
        } catch (Exception e) {
            return null;
        }
    }

    public boolean deleteKeyValue(String key) {
        try {
            if (StringUtils.isEmpty(key)) {
                log.error("deleteKeyValue key 不能为空");
                return false;
            }
            log.info("deleteKeyValue key={}", key);
            Boolean delete = redisTemplate.delete(key);
            return delete;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean expire(String key, Long timeout, TimeUnit timeUnit) {
        try {
            if (StringUtils.isEmpty(key)) {
                log.error("expire key 不能为空");
                return false;
            }
            log.info("expire key={},timeout={},timeUnit={}", key, timeout, timeUnit);
            redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Long getExpire(String key, TimeUnit timeUnit) {
        try {
            if (StringUtils.isEmpty(key)) {
                log.error("getExpire key 不能为空");
                return null;
            }
            log.info("getExpire key={},timeUnit={}", key, timeUnit);
            if (timeUnit == null) {
                return redisTemplate.getExpire(key, TimeUnit.SECONDS);
            } else {
                return redisTemplate.getExpire(key, timeUnit);
            }
        } catch (Exception e) {
            return null;
        }
    }

    public Set<String> getKeys(String key) {
        try {
            if (StringUtils.isEmpty(key)) {
                log.error("getKeys key 不能为空");
                return null;
            }
            log.info("getKeys key={}", key);
            Set<String> resultSet = redisTemplate.execute((RedisCallback<Set<String>>) connection -> {
                Set<String> keysTmp = new HashSet<>();
                try (Cursor<byte[]> cursor = connection.scan(ScanOptions.scanOptions().match(key).count(1000).build())) {
                    if (cursor == null) {
                        log.error("查询 redis scan 连接数用完 出现空指针");
                        return keysTmp;
                    }
                    while (cursor.hasNext()) {
                        keysTmp.add(new String(cursor.next()));
                    }
                    return keysTmp;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return keysTmp;
            });
            return resultSet;
        } catch (Exception e) {
            return null;
        }
    }

    public boolean setHashValue(String key, String hashKey, String value) {
        try {
            if (StringUtils.isEmpty(key)) {
                log.error("setHashValue key 不能为空");
                return false;
            }
            log.info("setKeyValue key={},hashKey={},value={}", key, hashKey, value);
            redisTemplate.boundHashOps(key).put(hashKey, value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Object getHashValue(String key, String hashKey) {
        try {
            if (StringUtils.isEmpty(key)) {
                log.error("getHashValue key 不能为空");
                return false;
            }
            Object object = redisTemplate.boundHashOps(key).get(hashKey);
            return object;
        } catch (Exception e) {
            return null;
        }
    }

    public <T> T getHashValue4Object(String key, String hashKey, Class<T> clazz) {
        try {
            if (StringUtils.isEmpty(key)) {
                log.error("getKeyValue key 不能为空");
                return null;
            }
            String s = (String)redisTemplate.boundHashOps(key).get(hashKey);
            return JSON.parseObject(s, clazz);
        } catch (Exception e) {
            return null;
        }
    }
    public <T> List<T> getHashValue4List(String key, String hashKey, Class<T> clazz) {
        try {
            if (StringUtils.isEmpty(key)) {
                log.error("getKeyValue key 不能为空");
                return null;
            }
            String s = (String)redisTemplate.boundHashOps(key).get(hashKey);
            return JSON.parseArray(s, clazz);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean deleteHashValue(String key, String hashKey) {
        try {
            if (StringUtils.isEmpty(key)) {
                log.error("deleteHashValue key 不能为空");
                return false;
            }
            log.info("setKeyValue key={},hashKey={}", key, hashKey);
            redisTemplate.boundHashOps(key).delete(hashKey);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public List<Object> getAllHashValue(String key) {
        try {
            if (StringUtils.isEmpty(key)) {
                log.error("getAllHashValue key 不能为空");
                return null;
            }
            List<Object> values = redisTemplate.boundHashOps(key).values();
            return values;
        } catch (Exception e) {
            return null;
        }
    }

}
