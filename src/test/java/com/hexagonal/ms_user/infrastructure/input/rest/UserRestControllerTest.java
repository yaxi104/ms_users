package com.hexagonal.ms_user.infrastructure.input.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hexagonal.ms_user.application.dto.request.AuthRequest;
import com.hexagonal.ms_user.application.dto.request.UserCustomerRequest;
import com.hexagonal.ms_user.application.dto.request.UserEmployeeRequest;
import com.hexagonal.ms_user.application.dto.request.UserOwnerRequest;
import com.hexagonal.ms_user.application.dto.response.AuthResponse;
import com.hexagonal.ms_user.application.dto.response.UserAuthResponse;
import com.hexagonal.ms_user.application.dto.response.UserResponse;
import com.hexagonal.ms_user.application.handler.IUserHandler;
import com.hexagonal.ms_user.domain.exception.UserAlreadyExistsException;
import com.hexagonal.ms_user.domain.exception.UserNotFoundException;
import com.hexagonal.ms_user.infrastructure.exception.UserForbiddenException;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserRestControllerTest {

    private MockMvc mockMvc;
    private IUserHandler userHandler;
    private JacksonTester<UserOwnerRequest> jsonUserOwnerRequest;
    private JacksonTester<UserEmployeeRequest> jsonUserEmployeeRequest;
    private JacksonTester<UserCustomerRequest> jsonUserCustomerRequest;

    @BeforeEach
    void setUp() {
        userHandler = mock(IUserHandler.class);
        UserRestController controller = new UserRestController(userHandler);

        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        JacksonTester.initFields(this, objectMapper);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new ControllerAdvisor())
                .build();

        var auth = new TestingAuthenticationToken("admin", "password", "ROLE_ADMIN");
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    void saveUserSuccessTest() throws Exception {
        UserOwnerRequest userOwnerRequest = TestDataFactory.mockOwnerRequest();

        mockMvc.perform(post("/api/v1/owner")
                        .with(authentication(SecurityContextHolder.getContext().getAuthentication()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUserOwnerRequest.write(userOwnerRequest).getJson()))
                .andExpect(status().isCreated());

        verify(userHandler).saveOwner(any(UserOwnerRequest.class));
    }

    @Test
    void saveUserAlreadyExistsTest() throws Exception {
        UserOwnerRequest userOwnerRequest = TestDataFactory.mockOwnerRequest();

        doThrow(new UserAlreadyExistsException())
                .when(userHandler).saveOwner(any(UserOwnerRequest.class));

        mockMvc.perform(post("/api/v1/owner")
                        .with(authentication(SecurityContextHolder.getContext().getAuthentication()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUserOwnerRequest.write(userOwnerRequest).getJson()))
                .andExpect(status().isConflict());
    }

    @Test
    void loginSuccessTest() throws Exception {
        AuthRequest authRequest = TestDataFactory.mockAuthequest();
        AuthResponse mockResponse = new AuthResponse("mock-jwt-token");

        when(userHandler.authUser(any(AuthRequest.class))).thenReturn(mockResponse);

        mockMvc.perform(post("/api/v1/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(authRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mock-jwt-token"));
    }

    @Test
    void loginUnauthorizedTest() throws Exception {
        AuthRequest authRequest = TestDataFactory.mockAuthequest();

        when(userHandler.authUser(any(AuthRequest.class)))
                .thenThrow(new UserForbiddenException());

        mockMvc.perform(post("/api/v1/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(authRequest)))
                .andExpect(status().isForbidden());
    }

    @Test
    void getUserByEmailSuccessTest() throws Exception {
        String email = "test@example.com";
        UserResponse mockResponse = TestDataFactory.mockUserResponse();

        when(userHandler.getUserByEmail(email)).thenReturn(mockResponse);

        mockMvc.perform(get("/api/v1/user")
                        .with(authentication(SecurityContextHolder.getContext().getAuthentication()))
                        .param("email", email))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value(email));
    }

    @Test
    void getUserByEmailNotFoundTest() throws Exception {
        String email = "notfound@example.com";

        when(userHandler.getUserByEmail(email)).thenThrow(new UserNotFoundException());

        mockMvc.perform(get("/user")
                        .with(authentication(SecurityContextHolder.getContext().getAuthentication()))
                        .param("email", email))
                .andExpect(status().isNotFound());
    }

    @Test
    void getUserByIdSuccessTest() throws Exception {
        Long userId = 1L;
        UserResponse mockResponse = TestDataFactory.mockUserResponse();

        when(userHandler.getUserById(userId)).thenReturn(mockResponse);

        mockMvc.perform(get("/api/v1/" + userId)
                        .with(authentication(SecurityContextHolder.getContext().getAuthentication())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    void getUserByIdNotFoundTest() throws Exception {
        Long userId = 999L;

        when(userHandler.getUserById(userId)).thenThrow(new UserNotFoundException());

        mockMvc.perform(get("/" + userId)
                        .with(authentication(SecurityContextHolder.getContext().getAuthentication())))
                .andExpect(status().isNotFound());
    }

    @Test
    void getUserByIdAuthSuccessTest() throws Exception {
        Long userId = 1L;
        UserAuthResponse mockResponse = new UserAuthResponse();
        mockResponse.setEmail("test@example.com");

        when(userHandler.getUserByIdAuth(userId)).thenReturn(mockResponse);

        mockMvc.perform(get("/api/v1/user/auth/" + userId)
                        .with(authentication(SecurityContextHolder.getContext().getAuthentication())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    void saveEmployeeSuccessTest() throws Exception {
        UserEmployeeRequest userEmployeeRequest = new UserEmployeeRequest();

        mockMvc.perform(post("/api/v1/employee")
                        .with(authentication(SecurityContextHolder.getContext().getAuthentication()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUserEmployeeRequest.write(userEmployeeRequest).getJson()))
                .andExpect(status().isCreated());

        verify(userHandler).saveEmployee(any(UserEmployeeRequest.class));
    }

    @Test
    void saveCustomerSuccessTest() throws Exception {
        UserCustomerRequest userCustomerRequest = new UserCustomerRequest();

        mockMvc.perform(post("/api/v1/customer")
                        .with(authentication(SecurityContextHolder.getContext().getAuthentication()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUserCustomerRequest.write(userCustomerRequest).getJson()))
                .andExpect(status().isCreated());

        verify(userHandler).saveCustomer(any(UserCustomerRequest.class));
    }
}
