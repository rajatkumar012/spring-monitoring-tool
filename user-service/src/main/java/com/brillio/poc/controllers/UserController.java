package com.brillio.poc.controllers;

import com.brillio.poc.entities.User;
import com.brillio.poc.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/")
    public User saveUser(@RequestBody User user) {
        return userService.save(user);
    }

    @GetMapping("/{emailId}")
    public User getUser(@PathVariable String emailId) {
        return userService.getByEmailId(emailId);
    }

}
