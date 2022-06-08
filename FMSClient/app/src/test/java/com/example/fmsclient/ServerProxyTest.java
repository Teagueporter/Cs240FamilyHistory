package com.example.fmsclient;

import static org.junit.jupiter.api.Assertions.*;

import androidx.annotation.VisibleForTesting;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import request.LoginRequest;
import request.RegRequest;
import results.EveryEventResult;
import results.EveryPersonResult;
import results.LoginResults;
import results.RegisterResults;

class ServerProxyTest {
    private ServerProxy proxy;
    RegisterResults registerResults;
    LoginResults loginResults;
    EveryEventResult everyEventResult;
    EveryPersonResult everyPersonResult;
    RegRequest regRequest = new RegRequest();

    @BeforeEach
    void setUp() {
        proxy = new ServerProxy("localhost", "8080");
        proxy.clear();
        regRequest.setGender("f"); regRequest.setEmail("email"); regRequest.setLastName("Porter"); regRequest.setFirstName("Teague"); regRequest.setUsername("twigs"); regRequest.setPassword("password");
        registerResults = proxy.register(regRequest);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("login pass")
    void login() {
        LoginRequest request = new LoginRequest();
        request.setPassword("password"); request.setUsername("twigs");
        loginResults = proxy.login(request);
        assertNotNull(loginResults.getAuthtoken());

    }

    @Test
    @DisplayName("login fail")
    void loginFail(){
        LoginRequest request = new LoginRequest();
        request.setPassword("password"); request.setUsername("Teague");
        loginResults = proxy.login(request);
        assertNull(loginResults.getAuthtoken());
    }

    @Test
    @DisplayName("register pass")
    void register() {
        regRequest.setGender("f"); regRequest.setEmail("email"); regRequest.setLastName("Clegg"); regRequest.setFirstName("Tanna"); regRequest.setUsername("Tannabum"); regRequest.setPassword("password");
        registerResults = proxy.register(regRequest);
        assertTrue(registerResults.isSuccess());
    }

    @Test
    @DisplayName("register fail")
    void registerFail(){
        registerResults = proxy.register(regRequest);
        assertFalse(registerResults.isSuccess());
    }

    @Test
    @DisplayName("getpeople success")
    void getPeople() {
        LoginRequest request = new LoginRequest();
        request.setPassword("password"); request.setUsername("twigs");
        loginResults = proxy.login(request);

        everyPersonResult = proxy.getPeople(loginResults.getAuthtoken());
        assertNotNull(everyPersonResult.getData());
    }

    @Test
    @DisplayName("getpeople fail")
    void getPeopleFail() {
        everyPersonResult = proxy.getPeople("fail");
        assertNull(everyPersonResult);
    }

    @Test
    @DisplayName("getevents pass")
    void getEvents() {
        LoginRequest request = new LoginRequest();
        request.setPassword("password"); request.setUsername("twigs");
        loginResults = proxy.login(request);

        everyEventResult = proxy.getEvents((loginResults.getAuthtoken()));
        assertNotNull(everyEventResult.getData());
    }

    @Test
    @DisplayName("getevents fail")
    void getEventsFail() {
        everyEventResult = proxy.getEvents("fail");
        assertNull(everyEventResult);
    }
}