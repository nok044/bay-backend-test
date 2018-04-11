package com.nok.baybackendtest.configuration;

import com.nok.baybackendtest.component.CustomUserDetails;
import com.nok.baybackendtest.entity.UserEntity;
import com.nok.baybackendtest.service.UserService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

@Configuration
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserService userService;

    public UserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> result = this.userService.findByUsername(username);
        if (!result.isPresent())
            throw new UsernameNotFoundException(username);

        UserEntity entity = result.get();

        CustomUserDetails userDetails = new CustomUserDetails();
        userDetails.setUserId(entity.getPkId());
        userDetails.setUsername(entity.getUsername());
        userDetails.setPassword(entity.getPassword());
        userDetails.setAccountNonExpired(true);
        userDetails.setAccountNonLocked(true);
        userDetails.setCredentialsNonExpired(true);
        userDetails.setEnabled(true);

        return userDetails;
    }
}
