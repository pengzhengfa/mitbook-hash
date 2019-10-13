package com.mitbook.limit;

import com.mitbook.util.ScriptUtil;
import com.mitbook.util.SpringBeanFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

import java.io.IOException;
import java.util.Collections;
/**
 * @author pengzhengfa
 */
@Component
public class RedisLimit {

    private static Logger logger = LoggerFactory.getLogger(RedisLimit.class);

    private int limit = 200;

    private static final int FAIL_CODE = 0;

    /**
     * lua script
     */
    private String script;

    private RedisLimit() {
        this.limit = limit ;
        buildScript();
    }

    /**
     * limit traffic
     * @return if true
     */
    public boolean limit() {

        JedisCluster jedisCluster = SpringBeanFactory.getBean("JedisCluster",JedisCluster.class);

        Object result = limitRequest(jedisCluster);

        if (FAIL_CODE != (Long) result) {
            return true;
        } else {
            return false;
        }
    }

    private Object limitRequest(Object connection) {
        Object result = null;
        String key = String.valueOf(System.currentTimeMillis() / 1000);
        if (connection instanceof Jedis){
            result = ((Jedis)connection).eval(script, Collections.singletonList(key), Collections.singletonList(String.valueOf(limit)));
            ((Jedis) connection).close();
        }else {
            result = ((JedisCluster) connection).eval(script, Collections.singletonList(key), Collections.singletonList(String.valueOf(limit)));
            try {
                ((JedisCluster) connection).close();
            } catch (IOException e) {
                logger.error("IOException",e);
            }
        }
        return result;
    }

    /**
     * read lua script
     */
    private void buildScript() {
        script = ScriptUtil.getScript("limit.lua");
    }

}
