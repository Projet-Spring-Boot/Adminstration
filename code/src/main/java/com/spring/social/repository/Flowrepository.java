package com.spring.social.repository;


import com.spring.social.model.flow;

import java.util.Map;

public interface Flowrepository {

    void save(flow flow);
    Map<String, flow> findAll();
    flow findById(String id);
    void deleteById(String id);
}
