//package com.jerryl.security.service;
//
//import org.springframework.security.core.Authentication;
//import org.springframework.security.web.DefaultRedirectStrategy;
//import org.springframework.security.web.RedirectStrategy;
//import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
///**
// * 登录成功跳转类
// */
//@Component
//public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
//    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
//
//    @Override
//    protected void handle(HttpServletRequest request, HttpServletResponse response,
//                          Authentication authentication) throws IOException, ServletException {
//        //UserDetailsBean user = (UserDetailsBean) authentication.getPrincipal();
//
//        String targetUrl ="/";
//
////        if (UserType.ADMIN.getValue().equals(user.getUserType())) {
////            targetUrl = "/swagger-ui.html";
////        } else if (UserType.TEACHER.getValue().equals(user.getUserType())) {
////            targetUrl = "/teacher";
////        } else if (UserType.TRAINEE.getValue().equals(user.getUserType())){
////            targetUrl = "/trainee";
////        } else {
////            targetUrl = "/login?error";
////        }
//
//        redirectStrategy.sendRedirect(request, response, targetUrl);
//    }
//}
