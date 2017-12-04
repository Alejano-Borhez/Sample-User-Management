package com.epam.test.rest;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
import static org.easymock.EasyMock.verify;

import com.epam.test.dao.User;
import com.epam.test.dao.UserDao;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-spring-rest-mock-flux.xml")
public class UserControllerMockFluxTest {

    private WebTestClient webTestClient;

    @Autowired
    private UserDao userDao;
    @Autowired
    private UserRestController controller;

    private List<User> userList;

    @Before
    public void setUp() {
        webTestClient = WebTestClient.bindToController(controller).build();

        userList = Arrays.asList(
                new User(1, "Login1", "Password1", "Desc1"),
                new User(2, "Login2", "Password2", "Desc2"),
                new User(3, "Login3", "Password3", "Desc3")
        );
    }

    @After
    public void tearDown() {
        verify(userDao);
        reset(userDao);
    }

    @Test
    public void getUsersTest() throws Exception {
        expect(userDao.getAllUsers()).andReturn(
                userList
        );
        replay(userDao);

        Flux<User> result = webTestClient.get()
                .uri("/users/reactive")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .exchange()
                .expectStatus().isOk()
                .returnResult(User.class)
                .getResponseBody();

        StepVerifier.create(result)
                .expectNext(userList.get(0))
                .expectNext(userList.get(1))
                .expectNext(userList.get(2))
                .thenCancel()
                .verify(Duration.ofSeconds(userList.size()));

        verify(userDao);
    }
}