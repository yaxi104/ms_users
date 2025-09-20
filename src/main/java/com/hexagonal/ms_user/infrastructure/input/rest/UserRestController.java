package com.hexagonal.ms_user.infrastructure.input.rest;

import com.hexagonal.ms_user.application.dto.request.AuthRequest;
import com.hexagonal.ms_user.application.dto.request.UserOwnerRequest;
import com.hexagonal.ms_user.application.dto.response.AuthResponse;
import com.hexagonal.ms_user.application.dto.response.UserResponse;
import com.hexagonal.ms_user.application.handler.IUserHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Validated
@Tag(name = "User", description = "Operations related to users")
public class UserRestController {

    private final IUserHandler userHandler;

    @Operation(
            summary = "Authenticate user and obtain JWT",
            description = "Authenticates a user using email and password credentials. If successful, returns a JWT access token used for authorized requests.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User valid"),
                    @ApiResponse(responseCode = "400", description = "Invalid request"),
                    @ApiResponse(responseCode = "403", description = "Not access")}
    )
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {
        AuthResponse authResponse = userHandler.authUser(authRequest);
        return ResponseEntity.ok(authResponse);
    }

    @Operation(
            summary = "Create owner user",
            description = "Creates a new user with PROPIETARIO role. Only accessible by ADMIN.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses =

                    {
                            @ApiResponse(responseCode = "201", description = "User created"),
                            @ApiResponse(responseCode = "400", description = "Invalid request"),
                            @ApiResponse(responseCode = "409", description = "User already exists")
                    }
    )
    @PostMapping("/owner")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> saveUser(@RequestBody UserOwnerRequest userOwnerRequest) {
        userHandler.saveOwner(userOwnerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(
            summary = "Get user by ID",
            description = "Retrieves a user by their unique ID. Accessible by users with roles ADMIN, PROPIETARIO or EMPLEADO."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'PROPIETARIO', 'EMPLEADO')")
    @GetMapping("/user")
    public ResponseEntity<UserResponse> getUserByEmail(@RequestParam("email") String email) {
        UserResponse user = userHandler.getUserByEmail(email);
        return ResponseEntity.ok(user);
    }
}
