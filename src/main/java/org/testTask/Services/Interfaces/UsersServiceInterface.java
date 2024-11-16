package org.testTask.Services.Interfaces;

import org.testTask.databases.entity.User;

import java.util.List;

public interface UsersServiceInterface {
    List<User> findAllUsers();
}
