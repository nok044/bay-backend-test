package com.nok.baybackendtest.configuration;

import com.nok.baybackendtest.component.CustomUserDetails;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    private String tokenHeader;
    private String tokenPrefix;
    private String tokenSecret;
    private Long tokenExpire;

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager, String tokenHeader, String tokenPrefix, String tokenSecret, Long tokenExpire) {
        super(authenticationManager);
        this.tokenHeader = tokenHeader;
        this.tokenPrefix = tokenPrefix;
        this.tokenSecret = tokenSecret;
        this.tokenExpire = tokenExpire;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        String header = request.getHeader(tokenHeader);

        if (header == null || !header.startsWith(tokenPrefix)) {
            chain.doFilter(request, response);
        }else {
            UsernamePasswordAuthenticationToken authentication = this.getAuthentication(request);

            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);
        }
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        if (token != null) {
            String userId = Jwts.parser()
                    .setSigningKey(tokenSecret.getBytes())
                    .parseClaimsJws(token.replace(tokenPrefix, ""))
                    .getBody()
                    .getSubject();

            if (userId != null) {
                CustomUserDetails userDetails = new CustomUserDetails();
                userDetails.setUserId(Long.parseLong(userId));

                return new UsernamePasswordAuthenticationToken(userDetails, null, new ArrayList<>());
            }
        }
        return null;
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