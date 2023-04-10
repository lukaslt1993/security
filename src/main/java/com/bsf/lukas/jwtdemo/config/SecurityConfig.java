package com.bsf.lukas.jwtdemo.config;

import com.bsf.lukas.jwtdemo.controller.EndpointNames;
import com.bsf.lukas.jwtdemo.security.AuthenticationFilter;
import com.bsf.lukas.jwtdemo.security.AuthorizationFilter;
import com.bsf.lukas.jwtdemo.security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;
    private final AuthenticationFilter authenticationFilter;
    private final AuthorizationFilter authorizationFilter;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public SecurityConfig(
            UserDetailsServiceImpl userDetailsService,
            @Value("${jwt.secret}") String tokenSecret,
            @Lazy AuthenticationManager authenticationManager
    ) {
        this.userDetailsService = userDetailsService;
        this.authenticationFilter = new AuthenticationFilter(authenticationManager, tokenSecret);
        this.authorizationFilter = new AuthorizationFilter(authenticationManager, tokenSecret, userDetailsService);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests()
                .requestMatchers(HttpMethod.POST, EndpointNames.LOGIN).permitAll()
                .requestMatchers(HttpMethod.POST, EndpointNames.USER).permitAll()
                .anyRequest().authenticated().and().csrf().disable()
                .addFilter(authenticationFilter)
                .addFilter(authorizationFilter)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
        return authenticationManagerBuilder.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return passwordEncoder;
    }

}
