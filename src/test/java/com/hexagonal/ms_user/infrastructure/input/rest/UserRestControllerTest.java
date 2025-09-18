package com.hexagonal.ms_user.infrastructure.input.rest;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hexagonal.ms_user.application.dto.request.UserRequest;
import com.hexagonal.ms_user.application.handler.IUserHandler;
import com.hexagonal.ms_user.infrastructure.exception.UserAlreadyExistsException;
import com.hexagonal.ms_user.infrastructure.exceptionhandler.ControllerAdvisor;
import com.hexagonal.ms_user.util.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserRestControllerTest {

    private MockMvc mockMvc;
    private IUserHandler userHandler;

    private JacksonTester<UserRequest> jsonUserRequest;

    @BeforeEach
    void setUp() {
        userHandler = mock(IUserHandler.class);
        UserRestController controller = new UserRestController(userHandler);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        JacksonTester.initFields(this, objectMapper);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new ControllerAdvisor()).build();

        var auth = new TestingAuthenticationToken("admin", "password", "ROLE_ADMIN");
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    void saveUserSuccessTest() throws Exception {
        UserRequest userRequest = TestDataFactory.mockUserRequest();

        mockMvc.perform(post("/api/v1/owner")
                        .with(authentication(SecurityContextHolder.getContext().getAuthentication()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUserRequest.write(userRequest).getJson()))
                .andExpect(status().isCreated());

        verify(userHandler).saveUser(any(UserRequest.class));
    }

    @Test
    void saveUserAlreadyExistsTest() throws Exception {
        UserRequest userRequest = TestDataFactory.mockUserRequest();

        doThrow(new UserAlreadyExistsException())
                .when(userHandler).saveUser(any(UserRequest.class));

        mockMvc.perform(post("/api/v1/owner")
                        .with(authentication(SecurityContextHolder.getContext().getAuthentication()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUserRequest.write(userRequest).getJson()))
                .andExpect(status().isConflict());
    }

}