package com.epam.test.service;

import com.epam.test.dao.User;
import org.springframework.dao.DataAccessException;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * Created by mentor on 20.2.17.
 */
public interface UserService {

    /**
     * Get all users list.
     *
     * @return all users list
     */
    List<User> getAllUsers() throws DataAccessException;

    /**
     * Get user by Id.
     *
     * @param userId user identifier.
     * @return user.
     */
    User getUserById(Integer userId) throws DataAccessException;

    /**
     * Get user by login.
     *
     * @param login user login.
     * @return user
     * @throws DataAccessException
     */
    User getUserByLogin(String login) throws DataAccessException;

    /**
     * Create new user.
     *
     * @param user user.
     * @return new user Id.
     */
    Integer addUser(User user) throws DataAccessException;

    /**
     * Update user.
     *
     * @param user user.
     * @return new user Id.
     */
    int updateUser(User user) throws DataAccessException;

    int deleteUser(Integer userId) throws DataAccessException;

    /**
     * Used to demonstrated non-blocking capabilities
     * of Spring 5 web-reactive
     * @return reactive stream of all users
     */
    Flux<User> getAllUsersFlux();
}
