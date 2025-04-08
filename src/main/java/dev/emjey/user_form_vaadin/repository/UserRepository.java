package dev.emjey.user_form_vaadin.repository;

import dev.emjey.user_form_vaadin.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
}
