package com.epam.test.service;

import com.epam.test.dao.User;
import com.epam.test.dao.UserDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * User Service implementation.
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LogManager.getLogger();

    @Autowired
    UserDao userDao;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public List<User> getAllUsers() throws DataAccessException {
        return userDao.getAllUsers();
    }

    @Override
    public User getUserById(Integer userId) throws DataAccessException {
        LOGGER.debug("getUserById({}) ", userId);
        Assert.notNull(userId, "User id should not be null.");
        return userDao.getUserById(userId);
    }

    @Override
    public User getUserByLogin(String login) throws DataAccessException {
        LOGGER.debug("getUserByLogin(): user login = {} ", login);
        Assert.hasText(login, "User login should not be null.");
        return userDao.getUserByLogin(login);
    }

    @Override
    public Integer addUser(User user) throws DataAccessException {
        Assert.notNull(user, "User should not be null.");
        LOGGER.debug("addUser(): user login = {} ", user.getLogin());
        Assert.isNull(user.getUserId(), "User Id should be null.");
        Assert.hasText(user.getLogin(), "User login should not be null.");
        Assert.hasText(user.getPassword(), "User password should not be null.");

        try {
            if (userDao.getUserByLogin(user.getLogin()) != null) {
                throw new IllegalArgumentException("");
            }
        } catch (DataAccessException ex) {

        }

        return userDao.addUser(user);
    }

    @Override
    public int updateUser(User user) throws DataAccessException {
        Assert.notNull(user, "User should not be null.");
        LOGGER.debug("updateUser(): user login = {} ", user.getLogin());
        Assert.hasText(user.getLogin(), "User login should not be null.");
        Assert.hasText(user.getPassword(), "User password should not be null.");
        return userDao.updateUser(user);
    }

    @Override
    public int deleteUser(Integer userId) throws DataAccessException {
        Assert.notNull(userId, "User id should not be null.");
        LOGGER.debug("deleteUser(): user id = {} ", userId);
        return userDao.deleteUser(userId);
    }

    @Override
    public Flux<User> getAllUsersFlux() {
        Future<List<User>> asyncAllUsers = CompletableFuture.supplyAsync(userDao::getAllUsers);
        try {
            return Flux.fromIterable(asyncAllUsers.get()).delayElements(Duration.ofSeconds(1L)).log();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return Flux.empty();
        }
    }
}
