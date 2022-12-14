package com.blogging.services.impl;

import com.blogging.entity.User;
import com.blogging.exception.ResourceNotFoundException;
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
    public UserDto createUser(UserDto user) {
        log.info("createUser started for user: " + user);
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
    public UserDto updateUser(UserDto user, Integer id) throws SQLException {
        log.info("updateUser started for id: " + id);
        String updateUserQuery = "UPDATE user SET about=?,email=?,name=?,password=? where id=?";
        Connection conn = null;
        try {
            conn = getConnection();
        } catch (Exception e) {
            log.severe("Error while connecting with DB: " + e);
        }
        PreparedStatement ps = conn.prepareStatement(updateUserQuery);
        fillQuery(ps, user);
        ps.setInt(5, id);
        int updateCount = ps.executeUpdate();
        if (updateCount != 0) {
            log.info("Data inserted in DB for user: " + user);
            return user;
        } else {
            log.warning("Failed to update in DB for userId: " + id);
            throw new ResourceNotFoundException("User", "userId", id);
        }

    }

    @Override
    public UserDto getUserById(Integer id) throws SQLException {
        log.info("getUserById started for id: " + id);
        String getUserByIdQuery = "SELECT * FROM user WHERE id=?";
        UserDto userDto = null;
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
        if (!rs.next()) {
            throw new ResourceNotFoundException("User", "userId", id);
        } else {
            do {
                userDto = fillUserDto(rs);
            } while (rs.next());
        }
        return userDto;
    }

    @Override
    public List<UserDto> getAllUsers() {
        log.info("getAllUsers started");
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
    public boolean deleteUserById(Integer id) throws SQLException {
        log.info("deleteUserById started for id: " + id);
        String deleteUserQuery = "DELETE FROM user WHERE id=?";
        Connection conn = null;
        try {
            conn = getConnection();
        } catch (Exception e) {
            log.severe("Error connecting to DB: " + e);
        }
        PreparedStatement ps = conn.prepareStatement(deleteUserQuery);
        ps.setInt(1, id);
        int deleteCount = ps.executeUpdate();
        if (deleteCount != 0) {
            log.info("Data deleted in DB for userId: " + id);
            return true;
        } else {
            log.warning("Failed to delete in DB for userId: " + id);
            throw new ResourceNotFoundException("User", "userId", id);
        }
    }

    @Override
    public boolean createUserInBatch(List<UserDto> users) {
        log.info("createUserInBatch started for user size: " + users.size());
        String createUserQuery = "INSERT into user(about,email,name,password) values(?,?,?,?)";
        try (Connection conn = getConnection()) {
            PreparedStatement ps = conn.prepareStatement(createUserQuery);
            int updateCount = 0;
            for (UserDto user : users) {
                fillQuery(ps, user);
                updateCount += ps.executeUpdate();
            }
            if (updateCount == users.size()) {
                log.info("Inserted all records in DB successfully");
                return true;
            } else {
                log.severe("All/Some records were not inserted");
                return false;
            }
        } catch (Exception e) {
            log.severe("Error connecting to DB: " + e);
        }
        return false;
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

    private Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            log.severe("DB Class not found error: " + e);
        }
        Connection conn;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/blogging", "root", "root");
        } catch (SQLException e) {
            log.severe("Error while connecting to DB: " + e);
            return null;
        }
        return conn;
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
