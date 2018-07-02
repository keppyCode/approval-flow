//package com.jerryl.security;
//
//import com.jerryl.security.service.CustomSuccessHandler;
//import com.jerryl.security.service.CustomUserDetailsService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.builders.WebSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.web.DefaultSecurityFilterChain;
//import org.springframework.security.web.FilterChainProxy;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
//
//import javax.annotation.Resource;
//import javax.servlet.ServletException;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * NO1：创建一个springSecurityFilterChain 的Servlet的过滤器
// * springSecurityFilterChain负责所有安全（例如 保护应用程序的URL，
// * 验证提交的用户名和密码，重定向到登陆的表单等等）
// * @author liuqiuping
// * @date 2018.7.1
// */
//@Configuration
//@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
//public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
//    @Autowired
//    private CustomUserDetailsService customUserDetailsService;
//
//    @Resource
//    private CustomSuccessHandler customSuccessHandler;
//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        //指定密码加密所使用的加密器为passwordEncoder()
//        //需要将密码加密后写入数据库
//        auth.userDetailsService(userDetailsService());
//        auth.eraseCredentials(false);
//
//    }
//
////@Autowired
//    //LightSwordUserDetailService lightSwordUserDetailService;
//
//    @Override
//    @Bean
//    public UserDetailsService userDetailsService() { //覆盖写userDetailsService方法 (1)
//        return customUserDetailsService;
//
//    }
//    public void init(WebSecurity web) {
//        //web.ignoring().antMatchers("/");
//    }
//    /**
//     * 默认配置:
//     *1.确保我们应用中的所有请求都需要用户被认证
//     *2.允许用户进行基于表单的认证
//     *3.允许用户使用HTTP基于验证进行认证
//     * @param http
//     * @throws Exception
//     */
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests()
//                .antMatchers("/**").permitAll()
//                //.antMatchers("/static/**").permitAll()  //  允许所有用户对根路径以及匹配"/static/"开头的路径的访问
//                //.antMatchers("/", "/index").hasRole("TEACHER")
//                .anyRequest().authenticated()   //  任何尚未匹配的的URL地址只需要对用户进行权限验证
//                .and()
//                .formLogin()
//                .successHandler(customSuccessHandler)
//                .failureUrl("/login?error=true")
//                //.defaultSuccessUrl("/home")
//                //.defaultSuccessUrl("/swagger-ui.html")  //  登陆成功后默认默认跳转到swagger页
//                .permitAll()
//                .and()
//                .rememberMe().tokenValiditySeconds(604800);
//        http.logout().logoutSuccessUrl("/"); // 退出默认跳转页面 (5)
//        http.csrf().disable();
//
//    }
//
//
//    @Bean
//    public BCryptPasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder(4);
//    }
//
//}
