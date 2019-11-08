package com.stadio.cms.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

/**
 * Created by Andy on 02/16/2018.
 */
@SpringBootApplication
@EnableResourceServer
public class SecurityConfig extends ResourceServerConfigurerAdapter
{

    @Autowired
    CMSRestAuthenticationEntryPoint customAuthEntryPoint;

    @Autowired
    PermissionFilter permissionFilter;

    @Override
    public void configure(HttpSecurity http) throws Exception
    {
        http.addFilterBefore(new SimpleCorsFilter(), ChannelProcessingFilter.class)
                .addFilterBefore(permissionFilter, BasicAuthenticationFilter.class)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .and()
                .csrf().disable()
                .anonymous().and()
                .authorizeRequests()
                .antMatchers("/api/config/features").permitAll()
                .antMatchers("/api/database/updateFeatures").permitAll()
                .antMatchers("/**").authenticated()
        ;
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.authenticationEntryPoint(customAuthEntryPoint);
        super.configure(resources);
    }

}
