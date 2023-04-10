package com.bsf.lukas.jwtdemo.service;

import com.bsf.lukas.jwtdemo.exception.EntityCreationException;
import com.bsf.lukas.jwtdemo.exception.EntityNotFoundException;
import com.bsf.lukas.jwtdemo.model.User;
import com.bsf.lukas.jwtdemo.repository.UserRepository;
import com.bsf.lukas.jwtdemo.security.SecurityConstants;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UserService {

    public static final String USER_DOES_NOT_EXIST = "User does not exist";
    public static final String USER_ALREADY_EXIST = "User already exist";
    public static final String ACCESS_DENIED_MESSAGE = "Users could be accessed only by themselves or admin";
    private final UserRepository repo;

    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository repo,
            PasswordEncoder passwordEncoder
    ) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
    }

    private UserDetails getLoggedInUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null) {
            Object principal = auth.getPrincipal();
            if (principal instanceof UserDetails) {
                return (UserDetails) principal;
            }
        }

        return null;
    }

    private boolean isAdmin() {
        UserDetails user = getLoggedInUser();

        if (user != null) {
            return user.getAuthorities().stream()
                    .anyMatch(role -> role.getAuthority().contains(SecurityConstants.Role.ADMIN.getValue()));
        }

        return false;
    }

    private boolean hasAccessTo(User user) {
        return isAdmin() || getLoggedInUser() != null && getLoggedInUser().getUsername().equals(user.getUserName());
    }

    private void checkUserExist(Long id) {
        if (!repo.existsById(id)) {
            throw new EntityNotFoundException(USER_DOES_NOT_EXIST, id);
        }
    }

    public User createUser(User user) {
        if (repo.findFirstByUserName(user.getUserName()) != null) {
            throw new EntityCreationException(USER_ALREADY_EXIST, user.getUserId());
        }

        setPassword(user, user.getPassword());
        user.setRole(SecurityConstants.Role.USER);
        return repo.save(user);
    }

    public User getUser(Long id) {
        return getUser(repo.findById(id).orElse(null));
    }

    public User getUser(String name) {
        return getUser(repo.findFirstByUserName(name));
    }

    private User getUser(User user) {
        if (user != null) {
            if (hasAccessTo(user)) {
                return user;
            }

            throw new AccessDeniedException(ACCESS_DENIED_MESSAGE);
        }

        return null;
    }

    public List<User> getUsers() {
        if (isAdmin()) {
            return StreamSupport.stream(repo.findAll().spliterator(), false)
                    .collect(Collectors.toList());
        }

        throw new AccessDeniedException(ACCESS_DENIED_MESSAGE);
    }

    public User updateUser(Long id, User updatedUser) {
        checkUserExist(id);
        User result = getUser(repo.findById(id).get());
        result.setUserName(updatedUser.getUserName());
        setPassword(result, updatedUser.getPassword());
        return repo.save(result);
    }

    public void deleteUser(Long id) {
        checkUserExist(id);
        repo.delete(getUser(id));
    }

    private void setPassword(User user, String password) {
        user.setPassword(passwordEncoder.encode(password));
    }
}