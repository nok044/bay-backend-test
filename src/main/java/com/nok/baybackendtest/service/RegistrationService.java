package com.nok.baybackendtest.service;

import com.nok.baybackendtest.entity.MemberTypeEntity;
import com.nok.baybackendtest.entity.UserEntity;
import com.nok.baybackendtest.exception.MininumSalaryException;
import com.nok.baybackendtest.repository.MemberTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

@Service
public class RegistrationService {

    @Autowired
    private UserService userService;

    @Autowired
    private MemberTypeRepository memberTypeRepository;

    private List<MemberTypeEntity> memberTypeList;

    @PostConstruct
    private void init(){
        this.memberTypeList = this.memberTypeRepository.findAll(new Sort(Sort.Direction.DESC, "minimumSalary"));
    }

    public UserEntity register(String username, String password, String address, String phone, Long salary){
        Optional<MemberTypeEntity> memberTypeResult = this.memberTypeList.stream().filter(memberType -> salary.compareTo(memberType.getMinimumSalary()) > 0).findFirst();
        MemberTypeEntity memberType = null;
        if(memberTypeResult.isPresent())
            memberType = memberTypeResult.get();
        else
            throw new MininumSalaryException();

        return this.userService.create(username, password, address, phone, salary, memberType);
    }
}
