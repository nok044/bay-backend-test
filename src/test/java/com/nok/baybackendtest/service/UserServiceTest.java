package com.nok.baybackendtest.service;

import com.nok.baybackendtest.configuration.Application;
import com.nok.baybackendtest.entity.MemberTypeEntity;
import com.nok.baybackendtest.entity.UserEntity;
import com.nok.baybackendtest.exception.DuplicateUsernameException;
import com.nok.baybackendtest.exception.InvalidPhoneNumberFormatException;
import com.nok.baybackendtest.repository.MemberTypeRepository;
import com.nok.baybackendtest.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Application.class)
@SpringBootTest
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Autowired
    @InjectMocks
    private UserService userService;

    @Autowired
    private MemberTypeRepository memberTypeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private List<MemberTypeEntity> memberTypeList;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);

        when(userRepository.save(any(UserEntity.class))).thenAnswer(invocation -> {
            Object[] args = invocation.getArguments();
            return args[0];
        });

        this.memberTypeList = this.memberTypeRepository.findAll(new Sort(Sort.Direction.DESC, "minimumSalary"));
    }

    @Test
    public void testCreateUserWithVaildDataAndCountryCodePhoneNumber01(){
        String username = "username";
        String password = "password";
        String address = "address";
        String phone = "66848806606";
        Long salary = 50000l;
        MemberTypeEntity memberType = memberTypeList.get(0);

        UserEntity entity = this.userService.create(username, password, address, phone, salary, memberType);
        assertNotNull(entity);
        assertEquals(username, entity.getUsername());
        assertTrue(this.passwordEncoder.matches(password, entity.getPassword()));
        assertEquals(address, entity.getAddress());
        assertEquals(phone, entity.getPhone());
        assertEquals(salary, entity.getSalary());
    }

    @Test
    public void testCreateUserWithVaildDataAndCountryCodePhoneNumber02(){
        String username = "username";
        String password = "password";
        String address = "address";
        String phone = "+66848806606";
        Long salary = 50000l;
        MemberTypeEntity memberType = memberTypeList.get(0);

        UserEntity entity = this.userService.create(username, password, address, phone, salary, memberType);
        assertNotNull(entity);
        assertEquals(username, entity.getUsername());
        assertTrue(this.passwordEncoder.matches(password, entity.getPassword()));
        assertEquals(address, entity.getAddress());
        assertEquals("66848806606", entity.getPhone());
        assertEquals(salary, entity.getSalary());
    }

    @Test
    public void testCreateUserWithVaildDataAndLocalPhoneNumber01(){
        String username = "username";
        String password = "password";
        String address = "address";
        String phone = "0848806606";
        Long salary = 50000l;
        MemberTypeEntity memberType = memberTypeList.get(0);

        UserEntity entity = this.userService.create(username, password, address, phone, salary, memberType);
        assertNotNull(entity);
        assertEquals(username, entity.getUsername());
        assertTrue(this.passwordEncoder.matches(password, entity.getPassword()));
        assertEquals(address, entity.getAddress());
        assertEquals("66848806606", entity.getPhone());
        assertEquals(salary, entity.getSalary());
    }

    @Test
    public void testCreateUserWithVaildDataAndLocalPhoneNumber02(){
        String username = "username";
        String password = "password";
        String address = "address";
        String phone = "084-880-6606";
        Long salary = 50000l;
        MemberTypeEntity memberType = memberTypeList.get(0);

        UserEntity entity = this.userService.create(username, password, address, phone, salary, memberType);
        assertNotNull(entity);
        assertEquals(username, entity.getUsername());
        assertTrue(this.passwordEncoder.matches(password, entity.getPassword()));
        assertEquals(address, entity.getAddress());
        assertEquals("66848806606", entity.getPhone());
        assertEquals(salary, entity.getSalary());
    }

    @Test(expected = InvalidPhoneNumberFormatException.class)
    public void testCreateUserWithInvalidPhoneNumber01(){
        String username = "username";
        String password = "password";
        String address = "address";
        String phone = "0848806606A";
        Long salary = 50000l;
        MemberTypeEntity memberType = memberTypeList.get(0);

        UserEntity entity = this.userService.create(username, password, address, phone, salary, memberType);
    }

    @Test(expected = InvalidPhoneNumberFormatException.class)
    public void testCreateUserWithInvalidPhoneNumber02(){
        String username = "username";
        String password = "password";
        String address = "address";
        String phone = "084-880-6606B";
        Long salary = 50000l;
        MemberTypeEntity memberType = memberTypeList.get(0);

        UserEntity entity = this.userService.create(username, password, address, phone, salary, memberType);
    }

    @Test(expected = DuplicateUsernameException.class)
    public void testCreateUserWithDuplicateUsername(){
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(new UserEntity()));

        String username = "username";
        String password = "password";
        String address = "address";
        String phone = "084-880-6606B";
        Long salary = 50000l;
        MemberTypeEntity memberType = memberTypeList.get(0);

        UserEntity entity = this.userService.create(username, password, address, phone, salary, memberType);
    }
}
