package com.spring.social.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.spring.social.entity.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.spring.social.entity.UserConnection;
 
@Repository
@Transactional
public class UserConnectionDAO {

    private RedisTemplate<String, UserConnection> redisTemplate;

    private HashOperations hashOperations;


    public UserConnectionDAO(RedisTemplate<String, UserConnection> redisTemplate) {
        this.redisTemplate = redisTemplate;

        hashOperations = redisTemplate.opsForHash();
    }
 
    @Autowired
    private EntityManager entityManager;
 
/*    public UserConnection findUserConnectionByUserProviderId(String userProviderId) {
    	
        try {
            String sql = "Select e from " + UserConnection.class.getName() + " e " //
                    + " Where e.userProviderId = :userProviderId ";
 
            Query query = entityManager.createQuery(sql, UserConnection.class);
            query.setParameter("userProviderId", userProviderId);
 
            List<UserConnection> list = query.getResultList();
 
            return list.isEmpty() ? null : list.get(0);
        } catch (NoResultException e) {
            return null;
        }
    }*/
    
/* public UserConnection findUserConnectionByUserName(String userName) {
    	
        try {
            String sql = "Select e from " + UserConnection.class.getName() + " e " //
                    + " Where e.userId = :userId ";
 
            Query query = entityManager.createQuery(sql, UserConnection.class);
            query.setParameter("userId", userName);
 
            List<UserConnection> list = query.getResultList();
 
            return list.isEmpty() ? null : list.get(0);
        } catch (NoResultException e) {
            return null;
        }
    }*/

        public UserConnection findUserConnectionByUserName(String userName) {
        try {
            return (UserConnection) hashOperations.get("USERCONNECTION", userName);
        } catch (NoResultException e) {
            return null;
        }
        }
}