package com.hexagonal.ms_user.infrastructure.input.rest;

import com.hexagonal.ms_user.application.dto.request.AuthRequest;
import com.hexagonal.ms_user.application.dto.request.UserOwnerRequest;
import com.hexagonal.ms_user.application.dto.response.AuthResponse;
import com.hexagonal.ms_user.application.dto.response.UserAuthResponse;
import com.hexagonal.ms_user.application.dto.response.UserResponse;
import com.hexagonal.ms_user.application.handler.IUserHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
                    @ApiResponse(
                            responseCode = "200", description = "User valid",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "AuthResponseExample",
                                            value = """
                                                    {
                                                        "token": "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiQURNSU4iLCJpZCI6Mywic3ViIjoicHJvcGlldGFyaW9wYXN0aW5pQHBsYXpvbGV0YS5jb20iLCJpYXQiOjE3NTgzODExNDgsImV4cCI6MTc1ODQxNzE0OH0.pBcadqfnztse_DAezgaa0j2A_CJjM5Lqh2zK66pnz5M"
                                                    }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400", description = "Invalid request",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Bad Request Example",
                                            value = """
                                                    {
                                                        "Message": "The request contains invalid data. Please check the submitted fields and try again"
                                                    }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403", description = "Not access",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Fordibben Example",
                                            value = """
                                                    {
                                                        "Message": "You do not have permission to access this resource"
                                                    }
                                                    """
                                    )
                            )
                    )
            }
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
            responses = {
                    @ApiResponse(responseCode = "201", description = "User created"),
                    @ApiResponse(
                            responseCode = "400", description = "Invalid request",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Bad Request Example",
                                            value = """
                                                    {
                                                        "Message": "The request contains invalid data. Please check the submitted fields and try again"
                                                    }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403", description = "Not access",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Fordibben Example",
                                            value = """
                                                    {
                                                        "Message": "You do not have permission to access this resource"
                                                    }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "409", description = "Conflict",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Conflict Example",
                                            value = """
                                                    {
                                                        "Message": "User already exists"
                                                    }
                                                    """
                                    )
                            )
                    )
            }
    )
    @PostMapping("/owner")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> saveUser(@RequestBody UserOwnerRequest userOwnerRequest) {
        userHandler.saveOwner(userOwnerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(
            summary = "Get user by Email",
            description = "Retrieves a user by their unique email. Accessible by users with roles ADMIN, PROPIETARIO or EMPLEADO.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User valid",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "User Response Example",
                                            value = """
                                                    {
                                                        "id": 3,
                                                        "firstName": "Pastini",
                                                        "lastName": "Sabrosino",
                                                        "idNumber": "541616415455",
                                                        "phoneNumber": "+573001354155",
                                                        "dateBirth": "2001-01-17",
                                                        "email": "propietariopastini@plazoleta.com",
                                                        "password": "$2a$10$5uDAphEHKL/MkUXQD6hnYONHwA2gfdBA7iVNctVc1p4rBLPv/HCYu"
                                                    }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400", description = "Invalid request",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Bad Request Example",
                                            value = """
                                                    {
                                                        "Message": "The request contains invalid data. Please check the submitted fields and try again"
                                                    }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403", description = "Not access",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Fordibben Example",
                                            value = """
                                                    {
                                                        "Message": "You do not have permission to access this resource"
                                                    }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404", description = "Not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Not Found Example",
                                            value = """
                                                    {
                                                        "Message": "User not found"
                                                    }
                                                    """
                                    )
                            )
                    )
            }
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'PROPIETARIO', 'EMPLEADO')")
    @GetMapping("/user")
    public ResponseEntity<UserResponse> getUserByEmail(@RequestParam("email") String email) {
        UserResponse user = userHandler.getUserByEmail(email);
        return ResponseEntity.ok(user);
    }

    @Operation(
            summary = "Get user by ID",
            description = "Retrieves a user by their unique ID. Accessible by users with roles ADMIN, PROPIETARIO or EMPLEADO.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User valid",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "User Response Example",
                                            value = """
                                                    {
                                                        "id": 3,
                                                        "firstName": "Pastini",
                                                        "lastName": "Sabrosino",
                                                        "idNumber": "541616415455",
                                                        "phoneNumber": "+573001354155",
                                                        "dateBirth": "2001-01-17",
                                                        "email": "propietariopastini@plazoleta.com",
                                                        "password": "$2a$10$5uDAphEHKL/MkUXQD6hnYONHwA2gfdBA7iVNctVc1p4rBLPv/HCYu"
                                                    }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400", description = "Invalid request",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Bad Request Example",
                                            value = """
                                                    {
                                                        "Message": "The request contains invalid data. Please check the submitted fields and try again"
                                                    }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403", description = "Not access",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Fordibben Example",
                                            value = """
                                                    {
                                                        "Message": "You do not have permission to access this resource"
                                                    }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404", description = "Not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Not Found Example",
                                            value = """
                                                    {
                                                        "Message": "User not found"
                                                    }
                                                    """
                                    )
                            )
                    )
            }
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'PROPIETARIO', 'EMPLEADO')")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable("id") Long id) {
        UserResponse user = userHandler.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @Operation(
            summary = "Get user auth by Email",
            description = "Retrieves a user auth by their unique email. Accessible by users with roles ADMIN, PROPIETARIO or EMPLEADO.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User valid",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "User Response Example",
                                            value = """
                                                    {
                                                        "id": 3,
                                                        "email": "propietariopastini@plazoleta.com",
                                                        "password": "$2a$10$5uDAphEHKL/MkUXQD6hnYONHwA2gfdBA7iVNctVc1p4rBLPv/HCYu",
                                                        "role": "ADMIN"
                                                    }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400", description = "Invalid request",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Bad Request Example",
                                            value = """
                                                    {
                                                        "Message": "The request contains invalid data. Please check the submitted fields and try again"
                                                    }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403", description = "Not access",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Fordibben Example",
                                            value = """
                                                    {
                                                        "Message": "You do not have permission to access this resource"
                                                    }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404", description = "Not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Not Found Example",
                                            value = """
                                                    {
                                                        "Message": "User not found"
                                                    }
                                                    """
                                    )
                            )
                    )
            }
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'PROPIETARIO', 'EMPLEADO')")
    @GetMapping("/user/auth/{id}")
    public ResponseEntity<UserAuthResponse> getUserByIdAuth(@PathVariable("id") Long id) {
        UserAuthResponse user = userHandler.getUserByIdAuth(id);
        return ResponseEntity.ok(user);
    }

}
