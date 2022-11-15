package com.blogging.services.impl;

import com.blogging.entity.User;
import com.blogging.payload.UserDto;
import com.blogging.repositories.UserRepo;
import com.blogging.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
public class UserServiceImpl implements UserServices {
    Logger log = Logger.getLogger(UserServices.class.getName());
    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDto createUser(UserDto user) throws SQLException, ClassNotFoundException {
        String createUserQuery = "INSERT into user(about,email,name,password) values(?,?,?,?)";
        try (Connection conn = getConnection()) {
            PreparedStatement ps = conn.prepareStatement(createUserQuery);
            fillQuery(ps, user);
            int updateCount = ps.executeUpdate();
            if (updateCount != 0) {
                log.info("Data inserted in DB for user: " + user);
                return user;
            } else {
                log.warning("Failed to insert in DB for user: " + user);
                return null;
            }
        } catch (Exception e) {
            log.severe("Error connecting to DB: " + e);
        }
        return null;
    }

    @Override
    public UserDto updateUser(UserDto user, Integer id) {

        return null;
    }

    @Override
    public UserDto getUserById(Integer id) throws SQLException, ClassNotFoundException {
        String getUserByIdQuery = "SELECT * FROM user WHERE id=?";
        UserDto userDto = new UserDto();
        Connection conn;
        try {
            conn = getConnection();
        } catch (Exception e) {
            log.severe("Error while connecting to DB: " + e);
            return null;
        }
        PreparedStatement ps = conn.prepareStatement(getUserByIdQuery);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            userDto = fillUserDto(rs);
        }
        return userDto;
    }

    @Override
    public List<UserDto> getAllUsers() throws SQLException, ClassNotFoundException {
        String getAllUsersQuery = "SELECT * FROM user";
        try (Connection conn = getConnection()) {
            List<UserDto> userSet = new ArrayList<>();
            PreparedStatement ps = conn.prepareStatement(getAllUsersQuery);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                userSet.add(fillUserDto(rs));
            }
            return userSet;
        } catch (Exception e) {
            log.severe("Error connecting to DB: " + e);
        }
        return null;
    }

    @Override
    public void deleteUserById(Integer id) {

    }

    private User dtoToUser(UserDto userDto) {
        User user = new User();
        user.setId(userDto.getId());
        user.setName(userDto.getName());
        user.setAbout(userDto.getAbout());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        return user;
    }

    private UserDto userToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setAbout(user.getAbout());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setPassword(user.getPassword());
        userDto.setId(user.getId());
        return userDto;
    }

    private Connection getConnection() throws ClassNotFoundException, SQLException {
        try {
            Class<?> className = Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            log.severe("DB Class not found error: " + e);
        }
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/blogging", "root", "root");
        } catch (SQLException e) {
            log.severe("Error while connecting to DB: " + e);
            return null;
        }
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/blogging", "root", "root");
    }

    private UserDto fillUserDto(ResultSet rs) throws SQLException {
        UserDto userDto = new UserDto();
        userDto.setId(rs.getInt(1));
        userDto.setAbout(rs.getString(2));
        userDto.setEmail(rs.getString(3));
        userDto.setName(rs.getString(4));
        userDto.setPassword(rs.getString(5));
        return userDto;
    }

    private void fillQuery(PreparedStatement ps, UserDto userDto) throws SQLException {
        ps.setString(1, userDto.getAbout());
        ps.setString(2, userDto.getEmail());
        ps.setString(3, userDto.getName());
        ps.setString(4, userDto.getPassword());
    }
}
