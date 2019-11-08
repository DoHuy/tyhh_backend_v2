package com.stadio.mobi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

/**
 * Created by Andy on 02/17/2018.
 */
@EnableResourceServer
@Configuration
public class SecurityConfig extends ResourceServerConfigurerAdapter
{

    @Autowired
    MobileAPIRestAuthenticationEntryPoint customAuthEntryPoint;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .and()
            .authorizeRequests()
                //Các đầu API sẽ check token nếu có token truyền lên
                .antMatchers("/api/user/checkPhoneNumber").permitAll()
                .antMatchers("/api/user/resetPassword").permitAll()
                .antMatchers("/dev/documents").permitAll()
                .antMatchers("/api/periodic/**").permitAll()
                .antMatchers("/api/media/images/**").permitAll()
                .antMatchers("/api/user/register").permitAll()
                .antMatchers("/api/exam/newest").permitAll()
                .antMatchers("/api/exam/details").permitAll()
                .antMatchers("/api/exam/topSubmit").permitAll()
                .antMatchers("/api/config").permitAll()

                .antMatchers("/api/category/details").permitAll()
                .antMatchers("/api/category/examList").permitAll()
                .antMatchers("/api/category/detail").permitAll()
                .antMatchers("/api/category/other").permitAll()

                .antMatchers("/api/chapter/**").permitAll()
                .antMatchers("/api/clazz/**").permitAll()
                .antMatchers("/api/home/**").permitAll()
                .antMatchers("/api/notification/**").permitAll()
                .antMatchers("/api/rank").permitAll()
                .antMatchers("/api/search/**").permitAll()
                .antMatchers("/api/sms/**").permitAll()
                .antMatchers("/api/payment/deplay").permitAll()
                .antMatchers("/api/message/**").permitAll()

                .antMatchers("/api/examOnline/list").permitAll()
                .antMatchers("/api/examOnline/details").permitAll()
                .antMatchers("/api/examOnline/comment/list").permitAll()
                .antMatchers("/api/examOnline/history").permitAll()
                .antMatchers("/api/examOnline/tablePoint").permitAll()

                .antMatchers("/api/theory/details").permitAll()
                .antMatchers("/api/theory/findByChapterId").permitAll()
                .antMatchers("/api/faq/list").permitAll()
                .antMatchers("/api/userPoint/details").permitAll()

                .antMatchers("/api/course/lectures").permitAll()
                .antMatchers("/api/course/lecture/detail").permitAll()
                .antMatchers("/api/course/detail").permitAll()
                .antMatchers("/api/course/otherCourses").permitAll()

                .and().authorizeRequests()
                .anyRequest().authenticated()
        ;
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.authenticationEntryPoint(customAuthEntryPoint);
        super.configure(resources);
    }

}
