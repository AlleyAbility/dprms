package com.example.dprms.security;

import com.example.dprms.jwt.JWTAuthenticationFilter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.sql.DataSource;


@Configuration
@EnableWebSecurity
public class UserRegistrationSecurityConfig {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JWTAuthenticationFilter authenticationFilter;

    @Autowired
    private UserRegistrationDetailsService userDetailsService;

    private static final String[] SECURED_URLs = {
            "/dprms/**"};

    private static final String[] UN_SECURED_URLs = {
            "/projects/**",
            "/projects/{id}",
            "/users/**",
            "/login",
            "/register/**",
            "/roles/**",
            "/documents/**"
    };

    @Bean
    public AuthenticationProvider authenticationProvider(){
        var authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

            http.csrf().disable()
                    .authorizeHttpRequests()
                    .requestMatchers(UN_SECURED_URLs).permitAll()
                    .requestMatchers(SECURED_URLs).hasAnyAuthority("ROLE_ADMIN", "ROLE_MANAGER")
                    .and()
//                    .authorizeHttpRequests(authorizeRequests ->
//                            authorizeRequests
//                                    .dispatcherTypeMatchers(HttpMethod.POST, DispatcherType.valueOf("/")).hasAnyRole("ROLE_ADMIN", "ROLE_MANAGER")
//                                    .dispatcherTypeMatchers(HttpMethod.POST, DispatcherType.valueOf("/register")).hasAnyRole("ROLE_ADMIN", "ROLE_MANAGER","ROLE_USER")
//                                    .dispatcherTypeMatchers(HttpMethod.GET).hasAnyRole("ROLE_ADMIN", "ROLE_MANAGER", "ROLE_USER")
//                                    .dispatcherTypeMatchers(HttpMethod.PUT).hasAnyRole("ROLE_ADMIN", "ROLE_MANAGER")
//                                    .dispatcherTypeMatchers(HttpMethod.DELETE).hasAnyRole("ROLE_ADMIN", "ROLE_MANAGER")
//                    )
                    .authorizeHttpRequests().anyRequest().authenticated()
                    .and()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                    .authenticationProvider(authenticationProvider())
                    .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);

            return http.build();
        }

        @Bean
        public PasswordEncoder passwordEncoder(){
            return new BCryptPasswordEncoder();
        }

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
            return authConfig.getAuthenticationManager();
        }

        @Bean
        public ModelMapper modelMapper() {
            return new ModelMapper();
        }
    }