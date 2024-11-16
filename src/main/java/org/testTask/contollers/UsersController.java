package org.testTask.contollers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.testTask.Services.Interfaces.UsersServiceInterface;
import org.testTask.databases.entity.User;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UsersController {

    private static final Logger logger = LogManager.getLogger(UsersController.class);

    private final UsersServiceInterface usersService;

    public UsersController(UsersServiceInterface accountsService) {
        this.usersService = accountsService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<User>> getAllUsers() {
        logger.info("Request to get all users received");
        List<User> users = usersService.findAllUsers();
        logger.info("Returning {} users", users.size());
        return ResponseEntity.ok(users);
    }
}
