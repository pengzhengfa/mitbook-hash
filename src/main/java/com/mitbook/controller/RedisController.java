package com.mitbook.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.JedisCluster;

@Controller
public class RedisController {

    @Autowired
    JedisCluster jedisCluster;

    @PostMapping(value = "test")
    @ResponseBody
    public String getRedisData(){

        return "success";
    }
}
