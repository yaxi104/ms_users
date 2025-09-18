package com.hexagonal.ms_user.infrastructure.configuration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void whenNoAuthenticationTest() throws Exception {
        mockMvc.perform(get("/user/api/v1/owner"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().json("{\"error\": \"Unauthorized access\"}"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void whenAccessDeniedTest() throws Exception {
        mockMvc.perform(get("/user/api/v1/owner"))
                .andExpect(status().isForbidden())
                .andExpect(content().json("{\"error\": \"Access denied\"}"));
    }
}
