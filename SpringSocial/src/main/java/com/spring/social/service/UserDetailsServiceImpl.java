package com.spring.social.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.social.dao.AppRoleDAO;
import com.spring.social.dao.AppUserDAO;
import com.spring.social.entity.AppUser;
import com.spring.social.social.SocialUserDetailsImpl;
  
@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {
  
    @Autowired
    private AppUserDAO appUserDAO;
  
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
  
        System.out.println("UserDetailsServiceImpl.loadUserByUsername=" + userName);
  
        AppUser appUser = this.appUserDAO.findAppUserByUserName(userName);
  
        if (appUser == null) {
            System.out.println("User not found! " + userName);
            throw new UsernameNotFoundException("User " + userName + " was not found in the database");
        }
  
        System.out.println("Found User: " + appUser);
  
/*        // [ROLE_USER, ROLE_ADMIN,..]
        String roleNames = this.appUserDAO.getRoleNames(appUser.getUserId());
  
        List<GrantedAuthority> grantList = new ArrayList<GrantedAuthority>();
        if (roleNames != null) {
            for (String role : roleNames) {
                // ROLE_USER, ROLE_ADMIN,..
                GrantedAuthority authority = new SimpleGrantedAuthority(role);
                grantList.add(authority);
            }
        }*/

        // [ROLE_USER, ROLE_ADMIN,..]
        String roleName = this.appUserDAO.getRoleNames(appUser.getUserId());

        List<GrantedAuthority> grantList = new ArrayList<GrantedAuthority>();
        List<String> roleList = new ArrayList<String>();

        if (roleName != null) {
            if(roleName == "ROLE_ADMIN")
            {
                GrantedAuthority authority1 = new SimpleGrantedAuthority("ROLE_ADMIN");
                grantList.add(authority1);
                roleList.add("ROLE_ADMIN");
                GrantedAuthority authority2 = new SimpleGrantedAuthority("ROLE_USER");
                grantList.add(authority2);
                roleList.add("ROLE_USER");

            }
            else
            {
                GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");
                grantList.add(authority);
                roleList.add("ROLE_USER");
            }
        }
  
        SocialUserDetailsImpl userDetails = new SocialUserDetailsImpl(appUser, roleList);
  
        return userDetails;
    }
  
}
