package com.hexagonal.ms_user.infrastructure.input.rest;

import com.hexagonal.ms_user.application.dto.request.RoleRequest;
import com.hexagonal.ms_user.application.dto.response.RoleResponse;
import com.hexagonal.ms_user.application.handler.IRoleHandler;
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
@RequestMapping("/api/v1/role")
@RequiredArgsConstructor
@Validated
@Tag(name = "Role", description = "Operations related to role")
public class RoleRestController {

    private final IRoleHandler roleHandler;

    @Operation(
            summary = "Create a role",
            description = "Creates a new role.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Role created"),
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
                                                        "Message": "Role already exists"
                                                    }
                                                    """
                                    )
                            )
                    )
            }
    )
    @PostMapping("/foodcourt")
    public ResponseEntity<Void> saveRole(@RequestBody RoleRequest roleRequest) {
        roleHandler.saveRole(roleRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(
            summary = "Get role by Name",
            description = "Retrieves a role by their unique name. Accessible by users with roles ADMIN, PROPIETARIO or EMPLEADO.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Role valid",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Role Response Example",
                                            value = """
                                                    //                                                    {
                                                    //                                                        "id": 3,
                                                    //                                                        "firstName": "Pastini",
                                                    //                                                        "lastName": "Sabrosino",
                                                    //                                                        "idNumber": "541616415455",
                                                    //                                                        "phoneNumber": "+573001354155",
                                                    //                                                        "dateBirth": "2001-01-17",
                                                    //                                                        "email": "propietariopastini@plazoleta.com",
                                                    //                                                        "password": "$2a$10$5uDAphEHKL/MkUXQD6hnYONHwA2gfdBA7iVNctVc1p4rBLPv/HCYu"
                                                    //                                                    }
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
                                                        "Message": "Role not found"
                                                    }
                                                    """
                                    )
                            )
                    )
            }
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'PROPIETARIO', 'EMPLEADO')")
    @GetMapping("/foodcourt")
    public ResponseEntity<RoleResponse> getRoleByName(@RequestParam("name") String name) {
        RoleResponse rol = roleHandler.getRolByName(name);
        return ResponseEntity.ok(rol);
    }

    @Operation(
            summary = "Get role by ID",
            description = "Retrieves a role by their unique ID. Accessible by users with roles ADMIN, PROPIETARIO or EMPLEADO.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Role valid",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Role Response Example",
                                            value = """
                                                    //                                                    {
                                                    //                                                        "id": 3,
                                                    //                                                        "firstName": "Pastini",
                                                    //                                                        "lastName": "Sabrosino",
                                                    //                                                        "idNumber": "541616415455",
                                                    //                                                        "phoneNumber": "+573001354155",
                                                    //                                                        "dateBirth": "2001-01-17",
                                                    //                                                        "email": "propietariopastini@plazoleta.com",
                                                    //                                                        "password": "$2a$10$5uDAphEHKL/MkUXQD6hnYONHwA2gfdBA7iVNctVc1p4rBLPv/HCYu"
                                                    //                                                    }
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
    @GetMapping("/foodcourt/{id}")
    public ResponseEntity<RoleResponse> getRoleById(@PathVariable("id") Long id) {
        RoleResponse role = roleHandler.getRolById(id);
        return ResponseEntity.ok(role);
    }

}
