package cn.net.communion.helper;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@Configuration
@PropertySource("classpath:redis.properties")
public class RedisHelper {
    @Autowired
    private StringRedisTemplate srt;

    public void putWithYearExpire(String key, String value) {
        srt.opsForValue().set(key, value);
        expire(key);
    }

    private void expire(String key) {
        srt.expire(key, 1, TimeUnit.DAYS);
    }

    public String get(String key) {
        return srt.opsForValue().get(key);
    }

    public void pub(String channel, String message) {
        srt.convertAndSend(channel, message);
    }
}
