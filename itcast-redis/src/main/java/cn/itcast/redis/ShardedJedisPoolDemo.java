package cn.itcast.redis;

import java.util.ArrayList;
import java.util.List;

import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

/**
 * 集群式的连接池
 * 
 */
public class ShardedJedisPoolDemo {

    public static void main(String[] args) {
        // 构建连接池配置信息
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        // 设置最大连接数
        poolConfig.setMaxTotal(50);

        // 定义集群信息
        List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
        shards.add(new JedisShardInfo("127.0.0.1", 6379));
        shards.add(new JedisShardInfo("192.168.0.5", 6379));

        // 定义集群连接池
        ShardedJedisPool shardedJedisPool = new ShardedJedisPool(poolConfig, shards);
        ShardedJedis shardedJedis = null;
        try {
            // 从连接池中获取到jedis分片对象
            shardedJedis = shardedJedisPool.getResource();
            
//                shardedJedis.set("key_" + i, "value_" + i);
//            for (int i = 0; i < 50; i++) {
//            }

            // 从redis中获取数据
           // String value = shardedJedis.get("mytest");
            //System.out.println(value);
            
            System.out.println(shardedJedis.get("key_21"));
            System.out.println(shardedJedis.get("key_23"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != shardedJedis) {
                // 关闭，检测连接是否有效，有效则放回到连接池中，无效则重置状态
                shardedJedis.close();
            }
        }

        // 关闭连接池
        shardedJedisPool.close();

    }
}
