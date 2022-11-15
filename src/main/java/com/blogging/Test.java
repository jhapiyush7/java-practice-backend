package com.blogging;

import com.blogging.payload.UserDto;
import com.blogging.services.impl.UserServiceImpl;

import java.sql.SQLException;

public class Test {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        UserDto userDto;
        UserServiceImpl userServiceImplObj = new UserServiceImpl();
        userDto = userServiceImplObj.getUserById(1);
        System.out.println(userDto);
    }
}
