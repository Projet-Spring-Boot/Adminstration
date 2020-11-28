package com.spring.social;

import com.spring.social.model.flow;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootApplication
public class ApplicationSocial {

	@Bean
	JedisConnectionFactory jedisConnectionFactory()
	{
		return new JedisConnectionFactory();
	}

	@Bean
	RedisTemplate<String, flow> redisTemplate (){
		RedisTemplate<String, flow> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(jedisConnectionFactory());
		return redisTemplate;
	}

	public static void main(String[] args) {
		SpringApplication.run(ApplicationSocial.class, args);
	}
}