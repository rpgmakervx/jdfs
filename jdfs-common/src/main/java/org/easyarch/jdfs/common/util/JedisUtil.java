package org.easyarch.jdfs.common.util;/**
 * Description : 
 * Created by YangZH on 16-6-4
 *  上午10:50
 */

import redis.clients.jedis.Jedis;

import java.util.List;

/**
 * Description :
 * Created by YangZH on 16-6-4
 * 上午10:50
 */

public class JedisUtil {

    public static Jedis jedisClient = new Jedis("localhost");
    static {
        System.out.println("Server is running: "+jedisClient.ping());
    }
    public static String get(String key){
        return jedisClient.get(key);
    }

    public static void set(String key,String value){
        jedisClient.set(key, value);
    }

    public static void del(String key){
        jedisClient.del(key);
    }

    public static boolean exists(String key){
        return jedisClient.hexists("codecat",key);
    }

    public static void setList(String listname,List<String> list){
        for (String item:list){
            jedisClient.lpush(listname,item);
        }
    }

    public static List<String> getList(String listname){
        return jedisClient.lrange(listname,0,jedisClient.strlen(listname));
    }

    public static void hSet(String hashname,String key,String value){
        jedisClient.hset(hashname, key, value);
    }

    public static String hGet(String hashname,String key){
        return jedisClient.hget(hashname,key);
    }

    public static void hDel(String hashname,String key){
        jedisClient.hdel(hashname,key);
    }

    public static boolean hExists(String hashname,String key){
        return jedisClient.hexists(hashname,key);
    }
}
