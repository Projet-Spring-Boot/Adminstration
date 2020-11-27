package com.spring.social.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.PreDestroy;

/**
 * Created by OmiD.HaghighatgoO on 8/19/2019.
 */

@Configuration
@EnableCaching
@EnableRedisRepositories
public class RedisConfig {


    @Value("localhost")
    String hostName;

    @Value("6379")
    Integer port;

    @Autowired
    RedisConnectionFactory factory;

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {

        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(hostName, port);
        return new LettuceConnectionFactory(redisStandaloneConfiguration);

    }

    @Bean
    public RedisTemplate<?, ?> redisTemplate() {

        RedisTemplate<String, Object> template = new RedisTemplate<>();
        RedisSerializer<String> stringSerializer = new StringRedisSerializer();
        JdkSerializationRedisSerializer jdkSerializationRedisSerializer = new JdkSerializationRedisSerializer();

        template.setConnectionFactory(redisConnectionFactory());

        template.setKeySerializer(stringSerializer);
        template.setHashKeySerializer(stringSerializer);

        template.setValueSerializer(jdkSerializationRedisSerializer);
        template.setHashValueSerializer(jdkSerializationRedisSerializer);

        template.setEnableTransactionSupport(true);
        template.afterPropertiesSet();


        return template;

    }

    @PreDestroy
    public void cleanRedis() {
        factory.getConnection()
                .flushDb();
    }
}

