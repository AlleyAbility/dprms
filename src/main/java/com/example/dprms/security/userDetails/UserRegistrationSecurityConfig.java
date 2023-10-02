package com.example.dprms.security.userDetails;

import com.example.dprms.security.jwt.JWTAuthenticationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


import javax.sql.DataSource;


@Configuration
@EnableWebSecurity
@EnableWebMvc
public class UserRegistrationSecurityConfig implements WebMvcConfigurer {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JWTAuthenticationFilter authenticationFilter;

    @Autowired
    private UserRegistrationDetailsService userDetailsService;

    private static final String[] SECURED_URLs = {
            "/dprms/**"};

    private static final String[] UN_SECURED_URLs = {
            "/api/v1/projects/**",
            "/api/v1/users/**",
            "/login",
            "/users/verifyEmail/**",
            "/register/**",
            "/api/v1/roles/**",
            "/api/v1/documents/**",
            "upload/**",
            "/api/v1/notifications/**",
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
                    .logout()
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/login?logout")
                    .permitAll()
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

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Adjust the mapping pattern to match your API endpoints
                .allowedOrigins("http://localhost:4200") // Whitelist the origin of your Angular app
                .allowedMethods("GET", "POST", "PUT", "DELETE") // Allow specific HTTP methods
                .allowedHeaders("*")
                .allowCredentials(true) // Allow sending cookies
                .maxAge(3600); // Cache the CORS configuration for 1 hour (optional)
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/upload/**")  // URL pattern to access files
                .addResourceLocations("file:upload/");  // Actual file system path to the directory
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        // Configure a custom date format
        objectMapper.setDateFormat(new StdDateFormat());

        // You can also configure other serialization/deserialization features if needed
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        return objectMapper;
    }


}