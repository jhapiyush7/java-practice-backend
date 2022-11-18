package com.blogging.controllers;

import com.blogging.payload.UserDto;
import com.blogging.services.UserServices;
import com.blogging.utils.ApiResponse;
import com.blogging.utils.TestClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

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

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Integer id) throws SQLException, ClassNotFoundException {
        boolean deleted = this.userServices.deleteUserById(id);
        return deleted ? ResponseEntity.ok(new ApiResponse("Deleted user Successfully", true)) :
                ResponseEntity.ok(new ApiResponse("Failed to Delete User", false));
    }

    @PostMapping("/batch")
    public ResponseEntity<ApiResponse> createUserInBatch(@RequestBody List<UserDto> users) throws SQLException, ClassNotFoundException {
        boolean allAdded = this.userServices.createUserInBatch(users);
        return allAdded ? ResponseEntity.ok(new ApiResponse("All users added Successfully", true)) :
                ResponseEntity.ok(new ApiResponse("Failed to add all/some users", false));
    }

//    @GetMapping("/nothing")
//    public ResponseEntity<ApiResponse> doNothing(@RequestBody TestClass obj) {
//        this.userServices.doNothing(obj);
//        return ResponseEntity.ok(new ApiResponse("Nothing Done", true));
//    }
}
