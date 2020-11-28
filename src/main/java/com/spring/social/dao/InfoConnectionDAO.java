package com.spring.social.dao;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.spring.social.entity.InfoConnection;
import com.spring.social.entity.AppUser;;
 
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

    public List<Long> getConnectionIdByUserId(Long userId)
    {
        String sql = "Select ur.connectionid from " + InfoConnection.class.getName() + " ur " //
                + "where ur.userid = :userId ";
                
        Query query = this.entityManager.createQuery(sql, Long.class);
        query.setParameter("userId", userId);

        return query.getResultList();
    }

    private static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
    }
    
    public List<AppUser> innerJoin()
    {
        String sql = "select u, ic from " + InfoConnection.class.getName() + " ic " +
            "INNER JOIN App_User u ON u.User_Id = ic.User_Id";

        Query query = this.entityManager.createQuery(sql, String.class);

        return (List<AppUser>)query.getResultList();
    }

    public List<AppUser> innerJoinByUserId(long userId)
    {
        String sql = "select u, ic from " + InfoConnection.class.getName() + " ic " +
            "INNER JOIN App_User u ON u.User_Id = ic.User_Id" +
            "WHERE u.User_Id = :User_Id";

        Query query = this.entityManager.createQuery(sql, String.class);
        query.setParameter("User_Id", userId);

        return (List<AppUser>)query.getResultList();
    }

    public void CreateInfoConnection(long userId)
    {
        InfoConnection ic =  new InfoConnection();
        ic.setUserid(userId);
		
		java.util.Date date = new Date();
		System.out.println("LA DATEUH !!!! " + date);

		ic.setLogin_Date(date);
		System.out.println("LA DATEUH !!!! " + ic.getLogin_Date());
		
		this.entityManager.persist(ic);
    }

    public void AddLogout(long connectionId)
    {
        String sql = "UPDATE " + InfoConnection.class.getName() + 
        "SET ur.Logout_Date = :date" +
        "WHERE ur.connectionid = :id";
                
        Query query = this.entityManager.createQuery(sql, String.class);
        query.setParameter("date", new Date());
        query.setParameter("id", connectionId);
        
        System.out.println("querrry = " + query);

        query.getSingleResult();
    }

}
