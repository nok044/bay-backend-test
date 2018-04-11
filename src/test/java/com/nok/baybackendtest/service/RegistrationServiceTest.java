package com.nok.baybackendtest.service;

import com.nok.baybackendtest.configuration.Application;
import com.nok.baybackendtest.entity.MemberTypeEntity;
import com.nok.baybackendtest.entity.UserEntity;
import com.nok.baybackendtest.exception.MininumSalaryException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Application.class)
@SpringBootTest
public class RegistrationServiceTest {

    @Mock
    private UserService userService;

    @Autowired
    @InjectMocks
    private RegistrationService registrationService;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);

        when(userService.create(anyString(),anyString(),anyString(),anyString(),anyLong(), any(MemberTypeEntity.class))).thenAnswer(invocation -> {
            Object[] args = invocation.getArguments();

            UserEntity entity = new UserEntity();
            entity.setUsername((String) args[0]);
            entity.setPassword((String) args[1]);
            entity.setAddress((String) args[2]);
            entity.setPhone((String) args[3]);
            entity.setSalary((Long) args[4]);
            entity.setMemberTypeId(((MemberTypeEntity) args[5]).getPkId());

            return entity;
        });
    }

    @Test
    public void testRegistrationWithVaildData(){
        String username = "username";
        String password = "password";
        String address = "address";
        String phone = "0848806606";
        Long salary = 50000l;
        UserEntity entity = this.registrationService.register(username, password, address, phone, salary);
        assertNotNull(entity);
        assertEquals(username, entity.getUsername());
        assertEquals(password, entity.getPassword());
        assertEquals(address, entity.getAddress());
        assertEquals(phone, entity.getPhone());
        assertEquals(salary, entity.getSalary());
    }

    @Test(expected = MininumSalaryException.class)
    public void testRegistrationWithInvaildSalary(){
        String username = "username";
        String password = "password";
        String address = "address";
        String phone = "0848806606";
        Long salary = 10000l;
        UserEntity entity = this.registrationService.register(username, password, address, phone, salary);
    }
}
