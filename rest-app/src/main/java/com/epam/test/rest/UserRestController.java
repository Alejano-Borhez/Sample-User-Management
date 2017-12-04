package com.epam.test.rest;

import com.epam.test.dao.User;
import com.epam.test.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

@CrossOrigin
@RestController
public class UserRestController {

    private static final Logger LOGGER = LogManager.getLogger();

    private final UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ExceptionHandler({IllegalArgumentException.class})
    public String incorrectDataError() {
        return "{  \"response\" : \"Incorrect Data Error\" }";
    }

    @GetMapping(value = "/users/reactive", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Flux<User> getUsersReactive() {
        LOGGER.debug("Began getting users");
        return userService.getAllUsersFlux();
    }

    //curl -v localhost:8088/users
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public @ResponseBody List<User> getUsers() {
        LOGGER.debug("getUsers()");
        return userService.getAllUsers();
    }

    //curl -H "Content-Type: application/json" -X POST -d '{"login":"xyz","password":"xyz"}' -v localhost:8088/user
    @RequestMapping(value = "/user", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody Integer addUser(@RequestBody User user) {
        LOGGER.debug("addUser: user = {}", user);
        return userService.addUser(user);
    }

    //curl -X PUT -v localhost:8088/user/2/l1/p1/d1
    @RequestMapping(value = "/user", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public void updateUser(@RequestBody User user) {
        LOGGER.debug("updateUser: id = {}", user.getUserId());
        userService.updateUser(user);
    }

    //curl -v localhost:8088/user/login/userLogin1
    @RequestMapping(value = "/user/login/{login}", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.FOUND)
    public @ResponseBody User getUserByLogin(@PathVariable(value = "login") String login) {
        LOGGER.debug("getUser: login = {}", login);
        return userService.getUserByLogin(login);
    }

    //curl -v localhost:8088/user/1
    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.FOUND)
    public @ResponseBody User getUserById(@PathVariable(value = "id") Integer id) {
        LOGGER.debug("getUserById: login = {}", id);
        return userService.getUserById(id);
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    public void deleteUserById(@PathVariable(value = "id") Integer id) {
        LOGGER.debug("deleteUserById: id = {}", id);
        userService.deleteUser(id);
    }
}
