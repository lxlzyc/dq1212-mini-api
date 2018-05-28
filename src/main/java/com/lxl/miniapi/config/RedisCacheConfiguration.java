package com.lxl.miniapi.config;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.session.data.redis.config.ConfigureRedisAction;
import redis.clients.jedis.JedisPoolConfig;


@Configuration
public class RedisCacheConfiguration {
    //redis链接1
    @Value("${core.spring.redis.host}")
    String host_core;
    @Value("${core.spring.redis.port}")
    int port_core;
    @Value("${core.spring.redis.password}")
    String password_core;

    @Value("${core.spring.redis.database}")
    int database_core;

//    @Value("${rcmd.spring.redis.host}")
//    private String host_rcmd;
//    @Value("${rcmd.spring.redis.port}")
//    private int port_rcmd;
//    @Value("${rcmd.spring.redis.password}")
//    private String password_rcmd;
//    @Value("${rcmd.spring.redis.database}")
//    int database_rcmd;


    @Value("${spring.redis.timeout}")
    private int timeout;
    @Value("${spring.redis.pool.max-idle}")
    private int maxIdle;
    @Value("${spring.redis.pool.max-wait}")
    private long maxWaitMillis;

    @Bean
    public ConfigureRedisAction configureRedisAction() {
        return ConfigureRedisAction.NO_OP;
    }

//    @Bean
//    public KeyGenerator redisGenerator() {
//        return new KeyGenerator() {
//            @Override
//            public Object generate(Object target, Method method, Object... params) {
//                StringBuilder sb = new StringBuilder();
//                sb.append(target.getClass().getName());
//                sb.append(method.getName());
//                for (Object obj : params) {
//                    sb.append(obj.toString());
//                }
//                return sb.toString();
//            }
//        };
//    }
//
//    @Bean
//    public CacheManager cacheManager(RedisTemplate redisTemplate) {
//        RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);
//        // Number of seconds before expiration. Defaults to unlimited (0)
//        cacheManager.setDefaultExpiration(10); //设置key-value超时时间
//        return cacheManager;
//    }

    @Bean
    @Primary
    public JedisConnectionFactory redisConnectionFactory_API(
    ) {
        return getJedisConnectionFactory(host_core, port_core, password_core, database_core, timeout, maxIdle, maxWaitMillis);
    }

//    @Bean
//    public JedisConnectionFactory redisConnectionFactory_RCMD(
//    ) {
//        return getJedisConnectionFactory(host_rcmd, port_rcmd, password_rcmd, database_rcmd, timeout, maxIdle, maxWaitMillis);
//    }

    public JedisConnectionFactory getJedisConnectionFactory(
            String host, int port, String password, int database, int timeout, int maxIdle, long maxWaitMillis) {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
//        poolConfig.setMaxTotal();
        poolConfig.setMaxIdle(maxIdle);
        poolConfig.setMaxWaitMillis(maxWaitMillis);
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnCreate(true);
        poolConfig.setTestWhileIdle(true);

        JedisConnectionFactory factory = new JedisConnectionFactory(poolConfig);
        factory.setHostName(host);
        factory.setPassword(password);
        factory.setPort(port);
        factory.setDatabase(database);

        //其他配置，可再次扩展
        return factory;
    }
    //配置多个redis链接
    @Bean(name = "redisTemplate_API")
    @Primary
    public RedisTemplate<String, Object> redisTemplate_API() throws Exception {
        RedisTemplate<String, Object> redisTemplateObject = new RedisTemplate<String, Object>();
        redisTemplateObject.setConnectionFactory(redisConnectionFactory_API());
        setSerializer(redisTemplateObject);
        redisTemplateObject.afterPropertiesSet();
        return redisTemplateObject;
    }

//    @Bean(name = "redisTemplate_RCMD")
//    public RedisTemplate<String, Object> redisTemplate_RCMD() throws Exception {
//        RedisTemplate<String, Object> redisTemplateObject = new RedisTemplate<String, Object>();
//        redisTemplateObject.setConnectionFactory(redisConnectionFactory_RCMD());
//        setSerializer(redisTemplateObject);
//        redisTemplateObject.afterPropertiesSet();
//        return redisTemplateObject;
//    }


    private void setSerializer(RedisTemplate<String, Object> template) {
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);

        RedisSerializer<String> stringSerializer = new StringRedisSerializer();
        template.setKeySerializer(stringSerializer);
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.setHashKeySerializer(stringSerializer);
        template.setHashValueSerializer(jackson2JsonRedisSerializer);

        //在使用String的数据结构的时候使用这个来更改序列化方式
        /*RedisSerializer<String> stringSerializer = new StringRedisSerializer();
        template.setKeySerializer(stringSerializer );
        template.setValueSerializer(stringSerializer );
        template.setHashKeySerializer(stringSerializer );
        template.setHashValueSerializer(stringSerializer );*/

    }


}
