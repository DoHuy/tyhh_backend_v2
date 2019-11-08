package com.stadio.mediation.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;

/**
 * Created by Andy on 02/16/2018.
 */
@SpringBootApplication
@EnableResourceServer
@Configuration
public class SecurityConfigMediation extends ResourceServerConfigurerAdapter
{

    @Autowired
    MediationRestAuthenticationEntryPoint customAuthEntryPoint;

    @Override
    public void configure(HttpSecurity http) throws Exception
    {
        http
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .and()
                .csrf().disable()
                .anonymous()
                .and()
                .authorizeRequests()
                .antMatchers("/api/media/document/upload").authenticated()
                .antMatchers("/api/media/upload-image").authenticated()
                .antMatchers("/api/media/document/secret/*").authenticated()
                .and().authorizeRequests()
                .antMatchers(HttpMethod.GET,"/api/media/images/{document:.+}").permitAll()
                .antMatchers(HttpMethod.GET,"/api/media/document/*").permitAll()
                .antMatchers("/api/chemistry/periodic/*").permitAll()
                .and()
                .authorizeRequests()
                .anyRequest().authenticated()
        ;
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.authenticationEntryPoint(customAuthEntryPoint);
        super.configure(resources);
    }

}
