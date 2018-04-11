package com.nok.baybackendtest.configuration;

import com.nok.baybackendtest.component.CustomUserDetails;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

public class JWTAuthenticationFilter extends BasicAuthenticationFilter {

    private AuthenticationManager authenticationManager;
    private String tokenHeader;
    private String tokenPrefix;
    private String tokenSecret;
    private Long tokenExpire;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, String tokenHeader, String tokenPrefix, String tokenSecret, Long tokenExpire) {
        super(authenticationManager);
        this.authenticationManager = authenticationManager;
        this.tokenHeader = tokenHeader;
        this.tokenPrefix = tokenPrefix;
        this.tokenSecret = tokenSecret;
        this.tokenExpire = tokenExpire;
    }

    @Override
    protected void onSuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String token = Jwts.builder()
                .setSubject(((CustomUserDetails) authentication.getPrincipal()).getUserId().toString())
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpire))
                .signWith(SignatureAlgorithm.HS512, tokenSecret.getBytes())
                .compact();

        response.addHeader(tokenHeader, tokenPrefix + token);
    }
}