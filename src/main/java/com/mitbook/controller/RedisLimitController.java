package com.mitbook.controller;

import com.mitbook.annotation.CommonLimit;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
/**
 * @author pengzhengfa
 */
@Controller
public class RedisLimitController {

    @PostMapping(value = "test3")
    @ResponseBody
    @CommonLimit
    public String getRedisData(){


        return "success";
    }
}
