package com.bsf.lukas.jwtdemo.security;

import com.bsf.lukas.jwtdemo.model.User;
import com.bsf.lukas.jwtdemo.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static org.springframework.security.core.userdetails.User.builder;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findFirstByUserName(username);

        if (user != null) {
            String password = user.getPassword();
            return builder().username(username).password(password).roles(user.getRole().getValue()).build();
        }

        throw new UsernameNotFoundException("User " + username + " not found");
    }
}
