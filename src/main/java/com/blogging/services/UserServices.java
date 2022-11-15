package com.blogging.services;

import com.blogging.entity.User;
import com.blogging.payload.UserDto;

import java.sql.SQLException;
import java.util.List;

public interface UserServices {
    UserDto createUser(UserDto user) throws SQLException, ClassNotFoundException;

    UserDto updateUser(UserDto user, Integer id);

    UserDto getUserById(Integer id) throws SQLException, ClassNotFoundException;

    List<UserDto> getAllUsers() throws SQLException, ClassNotFoundException;

    void deleteUserById(Integer id);
}