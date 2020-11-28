package com.spring.social.repository;


import com.spring.social.model.flow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class FlowRepositoryImpl implements Flowrepository{

    private HashOperations hashOperations;
    private RedisTemplate<String, flow> redisTemplate;


    @Autowired
    public FlowRepositoryImpl(RedisTemplate<String,flow> redisTemplate)
    {
        this.redisTemplate = redisTemplate;

        hashOperations = redisTemplate.opsForHash();
    }

    @Override
    public void save(flow flow) {

        hashOperations.put("FLOW",flow.getId(), flow);

    }

    @Override
    public Map<String, flow> findAll() {

        return hashOperations.entries("FLOW");
    }

    @Override
    public flow findById(String id) {

        return (flow)hashOperations.get("FLOW", id);
    }

    @Override
    public void deleteById(String id) {
        hashOperations.delete("FLOW",id);

    }
}
