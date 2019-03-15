package com.aaroncarlson.polls.config;

import com.aaroncarlson.polls.security.CustomUserDetailsService;
import com.aaroncarlson.polls.security.JWTAuthenticationFilter;
import com.aaroncarlson.polls.security.JwtAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @EnableWebSecurity - primary spring security annotation used to enable web security in a project
 * @EnableGlobalMethodSecurity - enable method level security based on annotations
 *  - secureEnabled: enables the @Secured annotation using which you can protect controller/service methods
 *  - jsr250Enabled: enables the @RoleAllowed annotation
 *  - perPostEnabled: enables more complex expression based access control syntax with @PreAuthorize and
 *    @PostAuthorize annotations
 * WebSecurityConfigurerAdapter - implements Spring Security's WebSecurityConfigurer interface. It provides
 * default security configurations and allows other classes to extend it and customize the security
 * configurations by overriding its methods.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * To authenticate a User or perform various role-based checks, Spring security needs to load users details
     * from the database, this is the service to manage that
     */
    @Autowired
    CustomUserDetailsService customUserDetailsService;
    /**
     * Used to return a 401 unauthorized error to clients that try to access a protected resource without
     * proper authentication. It implements Spring Security's AuthenticationEntryPoint interface
     */
    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;

    /**
     * JWTAuthenticatonFilter implements a filter to:
     *  - read JWT authentication token from the Authorization header of all requests
     *  - validates the token
     *  - loads the user details associated with that token
     *  - sets the user details in Spring Security's SecurityContext. Spring Security uses the UserDetails
     *  to perform authorization checks. Can also access the UserDetails stored in the SecurityContext in
     *  the controllers to perform business logic
     */
    @Bean
    public JWTAuthenticationFilter jwtAuthenticationFilter() {
        return new JWTAuthenticationFilter();
    }

    /**
     * AuthenticationManagerBuilder is used to create an AuthenticationManager instance which is the main
     * Spring Security interface for authenticating a user. Can use AuthenticationManagerBuilder to build
     * in-memory authentication, LDAP authentication, JDBC authentication, or add custom authentication provider.
     * In this example there is a customUserDetailsService and a passwordEncoder to build the AuthenticationManager.
     */
    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder)
            throws Exception {
        authenticationManagerBuilder
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    /* Use the AuthenticationManager to authenticate a user in the login API */
    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * HttpSecurity configurations are used to configure security functionalities like csrf, sessionManagement,
     * and add rules to protect resources based on various conditions. In this example, access to static resources
     * and few other public APIs is allowed for everyone; whereas, there are restrictions to other APIs for only
     * authorized users.
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                    .and()
                .csrf()
                    .disable()
                .exceptionHandling()
                    .authenticationEntryPoint(unauthorizedHandler)
                    .and()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                .authorizeRequests()
                    .antMatchers("/",
                            "/favicon.ico",
                            "/**/*.png",
                            "/**/*.gif",
                            "/**/*.svg",
                            "/**/*.jpg",
                            "/**/*.html",
                            "/**/*.css",
                            "/**/*.js")
                            .permitAll()
                    .antMatchers("/api/auth/**")
                            .permitAll()
                    .antMatchers("/api/user/checkUsernameAvailability",
                                "/api/user/checkEmailAvailability")
                            .permitAll()
                    .antMatchers(HttpMethod.GET, "/api/polls/**", "/api/users/**")
                            .permitAll()
                    .anyRequest()
                            .authenticated();
        // Add our custom JWT security filter
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

}
