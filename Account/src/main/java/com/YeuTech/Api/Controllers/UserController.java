package com.YeuTech.Api.Controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.YeuTech.Application.Services.IUserService;
import com.YeuTech.Dtos.Request.RegisterRequestDto;
import com.YeuTech.Dtos.Response.UserResponseDto;
import org.springframework.security.core.Authentication;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/v1/api/admin")
@Tag(name = "Users", description = "User management APIs")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final IUserService _userService;

    public UserController(IUserService userService) {
        this._userService = userService;
    }

    @GetMapping("/users")
    @Operation(summary = "List users", description = "Get all registered users")
    @ApiResponse(responseCode = "200", description = "Users retrieved successfully")
    public List<UserResponseDto> getUsers() {
        return _userService.getAllUsers();
    }

    @GetMapping("/myUser")
    @Operation(summary = "Get user by ID", description = "Retrieve a user by their unique identifier")
    @ApiResponse(responseCode = "200", description = "User retrieved successfully")
    @ApiResponse(responseCode = "404", description = "User not found")
    public UserResponseDto getUserById(Authentication authentication) {
        String email = authentication.getName();
        UserResponseDto user = _userService.getUserByEmail(email);
        return user;
    }

    @PutMapping("/myUser")
    @Operation(summary = "Update user profile", description = "Update the current authenticated user's profile")
    @ApiResponse(responseCode = "200", description = "Profile updated successfully")
    @ApiResponse(responseCode = "404", description = "User not found")
    public UserResponseDto updateMyUser(Authentication authentication, @RequestBody RegisterRequestDto user) {
        String email = authentication.getName();
        return _userService.updateUserProfile(email, user);
    }

    @PostMapping("/addUser")
    @Operation(summary = "Create user", description = "Create a new user account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User added successfully", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "User added successfully"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public String addUser(@RequestBody RegisterRequestDto user) {
        _userService.addUser(user);
        return "User added successfully";
    }
}
