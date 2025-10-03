package com.hikmetcakir.coreapi.controller;

import com.hikmetcakir.coreapi.dto.user.UserQueryRequest;
import com.hikmetcakir.coreapi.dto.user.UserQueryResponse;
import com.hikmetcakir.coreapi.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserQueryResponse query(@RequestBody UserQueryRequest request) {
        return userService.query(request);
    }
}
