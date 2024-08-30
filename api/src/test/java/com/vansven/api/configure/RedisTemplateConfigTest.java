package com.vansven.api.configure;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.HashMap;

@SpringBootTest
public class RedisTemplateConfigTest {
    @Resource
    RedisTemplate<String, HashMap<String,Integer>> redisTemplate;

    @Test
    void redisTemplate(){
        redisTemplate.boundHashOps("xxxxiA").put("1",1);
        Integer integer = (Integer) redisTemplate.boundHashOps("xxxxiA").get("1");
        System.out.println(integer);
    }

}