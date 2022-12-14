package com.blogging.services;

import com.blogging.payload.UserDto;

import java.sql.SQLException;
import java.util.List;

public interface UserServices {
    UserDto createUser(UserDto user) throws SQLException, ClassNotFoundException;

    UserDto updateUser(UserDto user, Integer id) throws SQLException, ClassNotFoundException;

    UserDto getUserById(Integer id) throws SQLException, ClassNotFoundException;

    List<UserDto> getAllUsers() throws SQLException, ClassNotFoundException;

    boolean deleteUserById(Integer id) throws SQLException, ClassNotFoundException;

    boolean createUserInBatch(List<UserDto> users) throws SQLException, ClassNotFoundException;

}