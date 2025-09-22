package com.hexagonal.ms_user.infrastructure.input.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hexagonal.ms_user.application.dto.request.RoleRequest;
import com.hexagonal.ms_user.application.dto.response.RoleResponse;
import com.hexagonal.ms_user.application.handler.IRoleHandler;
import com.hexagonal.ms_user.infrastructure.exceptionhandler.ControllerAdvisor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.hexagonal.ms_user.domain.utils.Constanst.PROPIETARIO;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RoleRestControllerTest {

    private MockMvc mockMvc;
    private IRoleHandler roleHandler;
    private JacksonTester<RoleRequest> jacksonRoleTester;

    @BeforeEach
    void setUp() {
        roleHandler = mock(IRoleHandler.class);
        RoleRestController controller = new RoleRestController(roleHandler);

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
    void saveRoleSuccessTest() throws Exception {
        RoleRequest userOwnerRequest = new RoleRequest();

        mockMvc.perform(post("/api/v1/role/foodcourt")
                        .with(authentication(SecurityContextHolder.getContext().getAuthentication()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jacksonRoleTester.write(userOwnerRequest).getJson()))
                .andExpect(status().isCreated());

        verify(roleHandler).saveRole(any(RoleRequest.class));
    }

    @Test
    void getRoleByNameSuccessTest() throws Exception {
        String name = PROPIETARIO;
        RoleResponse mockResponse = new RoleResponse();
        mockResponse.setId(1L);

        when(roleHandler.getRolByName(name)).thenReturn(mockResponse);

        mockMvc.perform(get("/api/v1/role/foodcourt")
                        .with(authentication(SecurityContextHolder.getContext().getAuthentication()))
                        .param("name", name))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void getRoleByIdIdSuccessTest() throws Exception {
        Long userId = 1L;
        RoleResponse mockResponse = new RoleResponse();
        mockResponse.setId(1L);

        when(roleHandler.getRolById(userId)).thenReturn(mockResponse);

        mockMvc.perform(get("/api/v1/role/foodcourt/" + userId)
                        .with(authentication(SecurityContextHolder.getContext().getAuthentication())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId));
    }

}
