package com.blogging.controllers;

import com.blogging.payload.UserDto;
import com.blogging.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserServices userServices;

    @PostMapping("/")
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) throws SQLException, ClassNotFoundException {
        UserDto createdUserDto = this.userServices.createUser(userDto);
        return new ResponseEntity<>(createdUserDto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}/hello")
    public ResponseEntity<UserDto> getUserById(@PathVariable Integer id) throws SQLException, ClassNotFoundException {
        UserDto userDto = this.userServices.getUserById(id);
        return ResponseEntity.ok(userDto);
    }
}
