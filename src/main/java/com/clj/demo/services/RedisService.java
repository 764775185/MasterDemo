package com.clj.demo.services;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service("RedisService")
public class RedisService {

    @Resource
    private RedisTemplate<String ,Object> redisTemplate;

    public void set(String key, Object value){
        ValueOperations<String, Object> vo = redisTemplate.opsForValue();
        vo.set(key, value);
    }

    public void set_timeout(String key, Object value, int minute) {
        ValueOperations<String, Object> vo = redisTemplate.opsForValue();
        vo.set(key, value, minute, TimeUnit.MINUTES);
    }

    public void clearRedisByKey(String key) {
        redisTemplate.delete(key);
    }

    public Object get(String key) {
        ValueOperations<String, Object> vo = redisTemplate.opsForValue();
        return vo.get(key);
    }

    public void clearRedis() {
        Set<String> keys = redisTemplate.keys(""+"*");
        redisTemplate.delete(keys);
    }

    public Map<String, Object> setStatus(String message) {
        Map<String,Object> result = new HashMap<>();
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
        result.put("lastTime",sdf.format(d));
        result.put("lastStatus",message);
        return result;
    }
}