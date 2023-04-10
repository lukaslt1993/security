package com.bsf.lukas.jwtdemo.component;

import com.bsf.lukas.jwtdemo.model.User;
import com.bsf.lukas.jwtdemo.repository.UserRepository;
import com.bsf.lukas.jwtdemo.security.SecurityConstants;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements ApplicationListener<ApplicationReadyEvent> {

    public static final String ADMIN_USERNAME = "admin";

    public static final String ADMIN_PASSWORD = "admin";

    private final UserRepository userRepo;

    private final PasswordEncoder passwordEncoder;

    public DataLoader(
            UserRepository userRepo,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public void loadAdminUser() {
        if (userRepo.findFirstByUserName(ADMIN_USERNAME) == null) {
            User user = new User();
            user.setUserName(ADMIN_USERNAME);
            user.setPassword(passwordEncoder.encode(ADMIN_PASSWORD));
            user.setRole(SecurityConstants.Role.ADMIN);
            userRepo.save(user);
        }
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        loadAdminUser();
    }
}
