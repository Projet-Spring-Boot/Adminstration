package com.spring.social.dao;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.time.temporal.ChronoUnit;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.spring.social.entity.InfoConnection;
 
@Repository
@Transactional
public class InfoConnectionDAO {
 
    @Autowired
    private EntityManager entityManager;

    public long getElapsedTime(Long connectionId) 
    {
        String sql = "Select ur.InfoConnection.Login_Date from " + InfoConnection.class.getName() + " ur " //
                + " where ur.InfoConnection.ConnectionId = :connectionId ";
                
        Query query = this.entityManager.createQuery(sql, String.class);
        query.setParameter("userId", connectionId);

        Date login = (Date) query.getSingleResult();

        sql = "Select ur.InfoConnection.Logout_Date from " + InfoConnection.class.getName() + " ur " //
                + " where ur.InfoConnection.ConnectionId = :connectionId ";
                
        query = this.entityManager.createQuery(sql, String.class);
        query.setParameter("userId", connectionId);

        Date logout = (Date) query.getSingleResult();

        return getDateDiff(login, logout, TimeUnit.MINUTES);
    }

    public long getNbConnection(Long userId)
    {
        String sql = "Select count(ur) from " + InfoConnection.class.getName() + " ur " //
                + " where ur.InfoConnection.UserId = :userId ";
                
        Query query = this.entityManager.createQuery(sql, String.class);
        query.setParameter("userId", userId);

        return (long)query.getSingleResult();
    }

    public List<Long> getConnectionIdByUserId(long userId)
    {
        String sql = "Select ur.InfoConnection.ConnectionId from " + InfoConnection.class.getName() + " ur " //
                + " where ur.InfoConnection.UserId = :userId ";
                
        Query query = this.entityManager.createQuery(sql, String.class);
        query.setParameter("userId", userId);

        List<String> res = query.getResultList();
        List<Long> longs = new ArrayList();
        for(String i : res)
        {
            longs.add(Long.parseLong(i));
        }

        return longs;
    }

    private static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
    }

}
