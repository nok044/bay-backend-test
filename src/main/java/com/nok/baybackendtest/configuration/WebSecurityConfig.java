package com.nok.baybackendtest.configuration;

import com.nok.baybackendtest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${application.context-path}")
    private String contextPath;
    @Value("${application.token-header}")
    private String tokenHeader;
    @Value("${application.token-prefix}")
    private String tokenPrefix;
    @Value("${application.token-secret}")
    private String tokenSecret;
    @Value("${application.token-expire}")
    private Long tokenExpire;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;



    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/api/register").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilter(new JWTAuthenticationFilter(authenticationManager(), tokenHeader, tokenPrefix, tokenSecret, tokenExpire))
                .addFilter(new JWTAuthorizationFilter(authenticationManager(), tokenHeader, tokenPrefix, tokenSecret, tokenExpire));
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        UserDetailsServiceImpl userDetailsService = new UserDetailsServiceImpl(userService);

        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
    }


}
