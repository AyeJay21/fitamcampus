package de.thk.gm.gdw.fitamcampus_muhammedali_garanli;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // allow everything
                )
                .csrf(csrf -> csrf.disable()) // disable CSRF
                .cors(cors -> cors.disable()) // disable CORS
                .httpBasic(basic -> basic.disable()) // disable HTTP Basic
                .formLogin(form -> form.disable()) // disable form login
                .logout(logout -> logout.disable()); // disable logout

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
