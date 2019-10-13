package com.mitbook.controller;

import com.mitbook.route.algorithm.RouteHandle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author pengzhengfa
 */
@Controller
public class ConsistenthashController {

    @Autowired
    private RouteHandle routeHandle;

    @PostMapping(value = "test2")
    @ResponseBody
    public String test(){

        return "sucess";
    }
}
