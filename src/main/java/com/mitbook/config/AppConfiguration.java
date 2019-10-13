package com.mitbook.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
/**
 * @author pengzhengfa
 */
@Component
public class AppConfiguration {


    @Value("${app.route.way}")
    private String routeWay;

    @Value("${app.route.way.consitenthash}")
    private String consistentHashWay;

    public String getRouteWay() {
        return routeWay;
    }

    public void setRouteWay(String routeWay) {
        this.routeWay = routeWay;
    }

    public String getConsistentHashWay() {
        return consistentHashWay;
    }

    public void setConsistentHashWay(String consistentHashWay) {
        this.consistentHashWay = consistentHashWay;
    }
}
