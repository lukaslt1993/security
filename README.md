## Description

This application implements Spring Security without the WebSecurityConfigurerAdapter which was deprecated in Spring Security 5.7.0-M2

https://spring.io/blog/2022/02/21/spring-security-without-the-websecurityconfigureradapter

## Implementation approach

Authentication and authorization is based on JWT

application.properties file stores JWT secret which is used by AuthenticationFilter and AuthorizationFilter
