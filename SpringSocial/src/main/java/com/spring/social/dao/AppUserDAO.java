package com.spring.social.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
  
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.UserProfile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.spring.social.entity.AppUser;
import com.spring.social.form.AppUserForm;
import com.spring.social.security.crypto.EncryptedPassword;
  
@Repository
@Transactional
public class AppUserDAO {

    public static final String ROLE_USER = "ROLE_USER";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";

    private RedisTemplate<String, AppUser> redisTemplate;

    private HashOperations hashOperations;


    public AppUserDAO(RedisTemplate<String, AppUser> redisTemplate) {
        this.redisTemplate = redisTemplate;

        hashOperations = redisTemplate.opsForHash();
    }

    @Autowired
    private EntityManager entityManager;

    public AppUser findAppUserByUserId(Long userId) {
        try {
            return (AppUser) hashOperations.get("APP_USER", userId);
        } catch (NoResultException e) {
            return null;
        }
    }

    public AppUser findAppUserByUserName(String userName) {

        try {
            return (AppUser) hashOperations.get("APP_USER", userName);
        } catch (NoResultException e) {
            return null;
        }
    }

    public AppUser findByEmail(String email) {

        try {
            return (AppUser) hashOperations.get("APP_USER", email);
        } catch (NoResultException e) {
            return null;
        }
    }

    private String findAvailableUserName(String userName_prefix) {
        AppUser account = this.findAppUserByUserName(userName_prefix);
        if (account == null) {
            return userName_prefix;
        }
        int i = 0;
        while (true) {
            String userName = userName_prefix + "_" + i++;
            account = this.findAppUserByUserName(userName);
            if (account == null) {
                return userName;
            }
        }
    }

    // Auto create App User Account.
    public AppUser createAppUser(Connection<?> connection) {

        ConnectionKey key = connection.getKey();
        //  (google,123) ...

        System.out.println("key= (" + key.getProviderId() + "," + key.getProviderUserId() + ")");

        UserProfile userProfile = connection.fetchUserProfile();

        String email = userProfile.getEmail();
        AppUser appUser = this.findByEmail(email);
        if (appUser != null) {
            return appUser;
        }
        String userName_prefix = userProfile.getFirstName().trim().toLowerCase()//
                + "_" + userProfile.getLastName().trim().toLowerCase();

        String userName = this.findAvailableUserName(userName_prefix);
        //
        // Random Password! TODO: Need send email to User!
        //
        String randomPassword = UUID.randomUUID().toString().substring(0, 5);
        System.out.println("PASSWORD =" + randomPassword);
        String encrytedPassword = EncryptedPassword.encrytePassword(randomPassword);
        //
        appUser = new AppUser();
        appUser.setEnabled(true);
        appUser.setEncrytedPassword(encrytedPassword);
        appUser.setUserName(userName);
        appUser.setEmail(email);
        appUser.setFirstName(userProfile.getFirstName());
        appUser.setLastName(userProfile.getLastName());

        this.entityManager.persist(appUser);

        // Create default Role
        List<String> roleNames = new ArrayList<String>();
        roleNames.add(ROLE_USER);
        this.createRoleFor(appUser, roleNames);

        return appUser;
    }

    public AppUser registerNewUserAccount(AppUserForm appUserForm, List<String> roleNames) {
        AppUser appUser = new AppUser();
        appUser.setUserName(appUserForm.getUserName());
        appUser.setEmail(appUserForm.getEmail());
        appUser.setFirstName(appUserForm.getFirstName());
        appUser.setLastName(appUserForm.getLastName());
        appUser.setEnabled(true);
        String encrytedPassword = EncryptedPassword.encrytePassword(appUserForm.getPassword());
        appUser.setEncrytedPassword(encrytedPassword);
        this.entityManager.persist(appUser);
        this.entityManager.flush();

        this.createRoleFor(appUser, roleNames);

        return appUser;
    }

    public AppUser editUserAccount(AppUser appUser) {

        System.out.println("Edit user with password : " + appUser.getEncrytedPassword());
        this.entityManager.persist(appUser);
        this.entityManager.flush();


        return appUser;
    }

    public String getRoleNames(Long userId) {
        try {
            AppUser user1 = findAppUserByUserId(userId);
            return user1.getUserRole();

        } catch (NoResultException e) {
            return null;
        }
    }

    public void createRoleFor(AppUser appUser, List<String> roleNames) {
        //
    /*if(ROLE_ADMIN){}
    }*/

    }
}
