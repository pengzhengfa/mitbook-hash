package com.mitbook.route.algorithm.loop;

import com.mitbook.route.algorithm.RouteHandle;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
/**
 * @author pengzhengfa
 */
public class LoopHandle implements RouteHandle {
    private AtomicLong index = new AtomicLong();

    @Override
    public String routeCacheServer(List<String> values, String key) {
        if(values.size() == 0){
            throw new RuntimeException("缓存服务器列表为空");
        }

        Long position = index.incrementAndGet() % values.size();
        if(position < 0){
            position =0L;
        }
        return values.get(position.intValue());
    }
}
