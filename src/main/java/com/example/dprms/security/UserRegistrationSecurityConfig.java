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
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class UserRegistrationSecurityConfig {

    @Autowired
    private JWTAuthenticationFilter authenticationFilter;

    @Autowired
    private UserRegistrationDetailsService userDetailsService;

    private static final String[] SECURED_URLs = {"/dprms/**"};

    private static final String[] UN_SECURED_URLs = {
            "/projects/**",
            "/projects/{id}",
            "/users/**",
            "/login/**",
            "/login",
            "/register",
            "/register/**",
            "/roles/**"
    };

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        var authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers(UN_SECURED_URLs).permitAll().and()
                .authorizeHttpRequests().requestMatchers(SECURED_URLs)
                .hasAnyAuthority( "ADMIN", "MANAGER").anyRequest().authenticated()
//                .and().formLogin() // Use form-based authentication
//                .defaultSuccessUrl("/users/all") // Redirect after successful login
//                .permitAll() // Allow access to login page without authentication
                .and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin().and().build();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        return http.cors()
//                .and().csrf().disable()
//                .authorizeHttpRequests()
//                .requestMatchers("/register/**","/login")
//                .permitAll()
//                .and()
//                .authorizeHttpRequests()
//                .requestMatchers("/dprms/**")
//                .hasAnyAuthority("USER", "ADMIN", "MANAGER")
//                .and().formLogin().defaultSuccessUrl("/dprms/")
//                .and()
//                .build();
//    }

}
