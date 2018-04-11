package com.nok.baybackendtest.controller;

import com.nok.baybackendtest.configuration.Application;
import com.nok.baybackendtest.repository.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.Base64Utils;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.Filter;

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Application.class)
@SpringBootTest
public class UserControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private Filter springSecurityFilterChain;

    @Autowired
    private UserRepository userRepository;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .addFilters(springSecurityFilterChain)
                .build();
    }

    @Test
    public void testGetCurrentUserWithValidCredential() throws Exception {
        String username = "username";
        String password = "password";
        String address = "address";
        String phone = "66848806606";
        Long salary = 50000l;

        mockMvc.perform(MockMvcRequestBuilders.post("/api/register")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username", username)
                .param("password", password)
                .param("address", address)
                .param("phone", phone)
                .param("salary", salary.toString())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/currentUser")
                .header(HttpHeaders.AUTHORIZATION, "Basic " + Base64Utils.encodeToString((username+":"+password).getBytes())))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetCurrentUserWithValidToken() throws Exception {
        String username = "username";
        String password = "password";
        String address = "address";
        String phone = "66848806606";
        Long salary = 50000l;

        mockMvc.perform(MockMvcRequestBuilders.post("/api/register")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username", username)
                .param("password", password)
                .param("address", address)
                .param("phone", phone)
                .param("salary", salary.toString())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/currentUser")
                .header(HttpHeaders.AUTHORIZATION, "Basic " + Base64Utils.encodeToString((username + ":" + password).getBytes())))
                .andExpect(status().isOk())
                .andReturn();

        String header = mvcResult.getResponse().getHeader("X-JWT-TOKEN");
        assertNotNull(header);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/currentUser")
                .header("X-JWT-TOKEN", header))
                .andExpect(status().isOk());
    }

    @After
    public void clear(){
        userRepository.deleteAll();
    }
}
