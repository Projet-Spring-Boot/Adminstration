package com.spring.social.repository;


import com.spring.social.entity.UserConnection;
import com.spring.social.model.Flow;

import java.util.List;
import java.util.Map;

public interface FlowRepository {

    void save(Flow flow);
    Map<String,Flow> findAll();
    Flow findById(String id);
    void deleteById(String id);

}
