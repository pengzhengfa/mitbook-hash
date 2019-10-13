package com.mitbook.lock;

import com.mitbook.util.ScriptUtil;
import com.mitbook.util.SpringBeanFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

import java.util.Collections;

@Component
public class RedisLock {

    private static Logger logger = LoggerFactory.getLogger(RedisLock.class);

    private static final String LOCK_MSG = "OK";

    private static final Long UNLOCK_MSG = 1L;

    private static final String SET_IF_NOT_EXIST = "NX";
    private static final String SET_WITH_EXPIRE_TIME = "PX";


    private String lockPrefix;

    private int sleepTime;

    private static final String DEFAULT_LOCK_PREFIX = "lock_";
    /**
     * default sleep time
     */
    private static final int DEFAULT_SLEEP_TIME = 100;

    /**
     * time millisecond
     */
    private static final int TIME = 1000;

    /**
     * lua script
     */
    private String script;

    JedisCluster jedisCluster;

    private RedisLock() {
        this.lockPrefix = DEFAULT_LOCK_PREFIX;
        this.sleepTime = DEFAULT_SLEEP_TIME;
        buildScript();
        jedisCluster = SpringBeanFactory.getBean("JedisCluster",JedisCluster.class);
    }

    /**
     * Non-blocking lock
     *
     * @param key     lock business type
     * @param request value
     * @return true lock success
     * false lock fail
     */
    public boolean tryLock(String key, String request) {

        Object connection =jedisCluster;
        String result ;
        if (connection instanceof Jedis){
            result =  ((Jedis) connection).set(lockPrefix + key, request, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, 10 * TIME);
            ((Jedis) connection).close();
        }else {
            result = ((JedisCluster) connection).set(lockPrefix + key, request, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, 10 * TIME);

        }

        if (LOCK_MSG.equals(result)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * blocking lock
     *
     * @param key
     * @param request
     */
    public void lock(String key, String request) throws InterruptedException {

        Object connection =jedisCluster;
        String result ;
        for (; ;) {
            if (connection instanceof Jedis){
                result = ((Jedis)connection).set(lockPrefix + key, request, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, 10 * TIME);
                if (LOCK_MSG.equals(result)){
                    ((Jedis) connection).close();
                }
            }else {
                result = ((JedisCluster)connection).set(lockPrefix + key, request, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, 10 * TIME);
            }

            if (LOCK_MSG.equals(result)) {
                break;
            }

            Thread.sleep(sleepTime);
        }

    }

    /**
     * blocking lock,custom time
     *
     * @param key
     * @param request
     * @param blockTime custom time
     * @return
     * @throws InterruptedException
     */
    public boolean lock(String key, String request, int blockTime) throws InterruptedException {

        Object connection =jedisCluster;
        String result ;
        while (blockTime >= 0) {
            if (connection instanceof Jedis){
                result = ((Jedis) connection).set(lockPrefix + key, request, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, 10 * TIME) ;
                if (LOCK_MSG.equals(result)){
                    ((Jedis) connection).close();
                }
            }else {
                result = ((JedisCluster) connection).set(lockPrefix + key, request, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, 10 * TIME) ;
            }
            if (LOCK_MSG.equals(result)) {
                return true;
            }
            blockTime -= sleepTime;

            Thread.sleep(sleepTime);
        }
        return false;
    }


    /**
     * Non-blocking lock
     *
     * @param key        lock business type
     * @param request    value
     * @param expireTime custom expireTime
     * @return true lock success
     * false lock fail
     */
    public boolean tryLock(String key, String request, int expireTime) {

        Object connection =jedisCluster;
        String result ;

        if (connection instanceof Jedis){
            result = ((Jedis) connection).set(lockPrefix + key, request, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, expireTime);
            ((Jedis) connection).close();
        }else {
            result = ((JedisCluster) connection).set(lockPrefix + key, request, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, expireTime);
        }

        if (LOCK_MSG.equals(result)) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * unlock
     *
     * @param key
     * @param request request must be the same as lock request
     * @return
     */
    public boolean unlock(String key, String request) {


        Object connection =jedisCluster;
        //lua script

        Object result = null;
        if (connection instanceof Jedis) {
            result = ((Jedis) connection).eval(script, Collections.singletonList(lockPrefix + key), Collections.singletonList(request));
            ((Jedis) connection).close();
        } else if (connection instanceof JedisCluster) {
            result = ((JedisCluster) connection).eval(script, Collections.singletonList(lockPrefix + key), Collections.singletonList(request));

        } else {
            //throw new RuntimeException("instance is error") ;
            return false;
        }

        if (UNLOCK_MSG.equals(result)) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * read lua script
     */
    private void buildScript() {
        script = ScriptUtil.getScript("lock.lua");
    }

}
