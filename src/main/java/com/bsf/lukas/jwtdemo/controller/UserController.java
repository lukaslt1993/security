package com.bsf.lukas.jwtdemo.controller;

import com.bsf.lukas.jwtdemo.model.User;
import com.bsf.lukas.jwtdemo.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(EndpointNames.USER)
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(service.getUsers());
    }

    @GetMapping(path = { "/{name}" })
    public ResponseEntity<?> getUser(@PathVariable String name) {
        return ResponseEntity.ok(service.getUser(name));
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User user) {
        return ResponseEntity.ok(service.createUser(user));
    }

    @PutMapping(path = { "/{id}" })
    public ResponseEntity<?> updateUser(@RequestBody User user, @PathVariable Long id) {
        return ResponseEntity.ok(service.updateUser(id, user));
    }

    @DeleteMapping(path = { "/{id}" })
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        service.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

}
