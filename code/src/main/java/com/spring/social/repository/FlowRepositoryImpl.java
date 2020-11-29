package com.spring.social.repository;


import com.spring.social.model.Flow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class FlowRepositoryImpl implements FlowRepository {

    private HashOperations hashOperations;
    private RedisTemplate<String, Flow> redisTemplate;

    //@Autowired
    public FlowRepositoryImpl(RedisTemplate<String, Flow> redisTemplate) {
        this.redisTemplate = redisTemplate;

        hashOperations = redisTemplate.opsForHash();
    }

    @Override
    public void save(Flow flow) {

        hashOperations.put("FLOW", flow.getId(), flow);

    }

    @Override
    public Map<String,Flow> findAll() {

        return hashOperations.entries("FLOW");
    }

    @Override
    public Flow findById(String id) {

        return (Flow) hashOperations.get("FLOW", id);
    }

    @Override
    public void deleteById(String id) {
        hashOperations.delete("FLOW", id);

    }

}
