package dev.emjey.user_form_vaadin.service;

import dev.emjey.user_form_vaadin.entity.User;
import dev.emjey.user_form_vaadin.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    public UserServiceImpl(UserRepository userRepository, RedisTemplate<String, Object> redisTemplate) {
        this.userRepository = userRepository;
        this.redisTemplate = redisTemplate;
    }

    @Override
    @CacheEvict(value = "users", allEntries = true)
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    @Transactional
    @CacheEvict(value = "users", allEntries = true)
    public User updateUser(User user) {
        User unwrappedUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("User with id " + user.getId() + " not found"));
        unwrappedUser.setFirstName(user.getFirstName());
        unwrappedUser.setLastName(user.getLastName());
        unwrappedUser.setEmail(user.getEmail());
        unwrappedUser.setUsername(user.getUsername());
        unwrappedUser.setBirthdate(user.getBirthdate());
        User updatedUser = userRepository.save(unwrappedUser);
        redisTemplate.keys("users*").forEach(redisTemplate::delete);
        return updatedUser;
    }

    @Override
    @Cacheable(value = "users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    @CacheEvict(value = "users", allEntries = true)
    public void refreshCache() {
    }
}
