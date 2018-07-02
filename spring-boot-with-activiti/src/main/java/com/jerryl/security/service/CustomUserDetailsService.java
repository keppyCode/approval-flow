//package com.jerryl.security.service;
//
//import org.activiti.engine.IdentityService;
//import org.activiti.engine.identity.User;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//import java.util.Collection;
//
///**
// * 数据库认证实现类
// */
//@Component
//public class CustomUserDetailsService  implements UserDetailsService {
//    @Resource
//    IdentityService identityService;
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User user = identityService.createUserQuery().userId(username).singleResult();
//        if (user == null){
//            throw new UsernameNotFoundException(username + " not found");
//        }
//
//        return null;
//    }
//}
