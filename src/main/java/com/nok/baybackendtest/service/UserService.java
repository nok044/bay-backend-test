package com.nok.baybackendtest.service;

import com.nok.baybackendtest.component.CustomUserDetails;
import com.nok.baybackendtest.entity.MemberTypeEntity;
import com.nok.baybackendtest.entity.UserEntity;
import com.nok.baybackendtest.exception.AuthenticationPrincipalNotFoundException;
import com.nok.baybackendtest.exception.DuplicateUsernameException;
import com.nok.baybackendtest.exception.InvalidPhoneNumberFormatException;
import com.nok.baybackendtest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private ValidatorFactory validatorFactory;
    private Validator validator;
    private DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

    @PostConstruct
    public void init(){
        this.validatorFactory = Validation.buildDefaultValidatorFactory();
        this.validator = this.validatorFactory.getValidator();
    }

    public UserEntity create(String username, String password, String address, String phone, Long salary, MemberTypeEntity memberType){
        Optional<UserEntity> findByUsernameResult = this.findByUsername(username);
        if(findByUsernameResult.isPresent())
            throw new DuplicateUsernameException();

        if(phone.startsWith("+"))
            phone = phone.substring(1);
        if(phone.startsWith("0"))
            phone = "66"+phone.substring(1);
        phone = phone.replaceAll("-","");
        try {
            Long.parseLong(phone);
        }catch (NumberFormatException e){
            throw new InvalidPhoneNumberFormatException();
        }

        String refCode = this.dateFormat.format(new Date())+phone.substring(phone.length()-4);

        password = this.passwordEncoder.encode(password);

        UserEntity entity = new UserEntity();
        entity.setUsername(username.toLowerCase());
        entity.setPassword(password);
        entity.setAddress(address);
        entity.setPhone(phone);
        entity.setRefCode(refCode);
        entity.setSalary(salary);
        entity.setMemberTypeId(memberType.getPkId());

        validator.validate(entity);
        return this.userRepository.save(entity);
    }

    public Optional<UserEntity> findByUsername(String username) {
        return this.userRepository.findByUsername(username.toLowerCase());
    }

    public UserEntity currentUser() {
        Object object = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CustomUserDetails principal = null;
        if(object instanceof CustomUserDetails)
            principal = (CustomUserDetails)object;
        else
            throw new AuthenticationPrincipalNotFoundException();

        Long userId = principal.getUserId();
        return this.userRepository.getOne(userId);
    }
}
