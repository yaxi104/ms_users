package com.hexagonal.ms_user.infrastructure.security;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.access.AccessDeniedException;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

class CustomAccessDeniedTest {

    private CustomAccessDenied customAccessDenied;
    private HttpServletRequest request;
    private AccessDeniedException accessDeniedException;
    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        customAccessDenied = new CustomAccessDenied();
        request = mock(HttpServletRequest.class);
        accessDeniedException = new AccessDeniedException("Access denied");
        mapper = new ObjectMapper();
    }

    @Test
    void handleShouldSetForbiddenStatusAndJsonBody() throws Exception {
        MockHttpServletResponse response = new MockHttpServletResponse();

        customAccessDenied.handle(request, response, accessDeniedException);

        assertEquals(403, response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        String responseBody = response.getContentAsString();
        Map<String, String> result = mapper.readValue(responseBody, new TypeReference<Map<String, String>>() {
        });

        assertEquals("Forbidden", result.get("error"));
        assertEquals("You do not have permission to access this resource", result.get("message"));
    }
}
