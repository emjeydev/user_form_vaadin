package dev.emjey.user_form_vaadin.service;

import dev.emjey.user_form_vaadin.entity.User;

import java.util.List;

public interface UserService {
    User createUser(User user);

    List<User> getAllUsers();

    void refreshCache();
}
