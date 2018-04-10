package com.nok.baybackendtest.service;

import com.nok.baybackendtest.component.AuthenticatedPrincipal;
import com.nok.baybackendtest.entity.MemberTypeEntity;
import com.nok.baybackendtest.entity.UserEntity;
import com.nok.baybackendtest.exception.AuthenticationPrincipalNotFoundException;
import com.nok.baybackendtest.exception.DuplicateUsernameException;
import com.nok.baybackendtest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public UserEntity create(String username, String password, String address, String phone, Long salary, MemberTypeEntity memberType){
        Optional<UserEntity> findByUsernameResult = userRepository.findByUsername(username);
        if(findByUsernameResult.isPresent())
            throw new DuplicateUsernameException();

        UserEntity entity = new UserEntity();
        entity.setUsername(username);
        entity.setPassword(password);
        entity.setAddress(address);
        entity.setPhone(phone);
        entity.setSalary(salary);
        entity.setMemberTypeId(memberType.getPkId());

        return userRepository.save(entity);
    }

    public UserEntity currentUser() {
        Object object = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AuthenticatedPrincipal principal = null;
        if(object instanceof AuthenticatedPrincipal)
            principal = (AuthenticatedPrincipal)object;
        else
            throw new AuthenticationPrincipalNotFoundException();

        Long userId = principal.getUserId();
        return userRepository.getOne(userId);
    }
}
