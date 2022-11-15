package com.blogging.controllers;

import com.blogging.payload.UserDto;
import com.blogging.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

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

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Integer id) throws SQLException, ClassNotFoundException {
        UserDto userDto = this.userServices.getUserById(id);
        return ResponseEntity.ok(userDto);
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserDto>> getAllUsers() throws SQLException, ClassNotFoundException {
        List<UserDto> userSet = this.userServices.getAllUsers();
        return ResponseEntity.ok(userSet);
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto user, @PathVariable int id) throws SQLException, ClassNotFoundException {
        UserDto userDto = this.userServices.updateUser(user, id);
        return ResponseEntity.ok(userDto);
    }
}
