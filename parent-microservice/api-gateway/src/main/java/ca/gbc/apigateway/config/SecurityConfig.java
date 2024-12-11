package ca.gbc.apigateway.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configures the security filter chain for HTTP requests
     *
     * This configuration requires all incoming HTTP requests to be authenticated.
     * It also configures OAuth2 resource server support to validate JWT tokens
     *
     *
     * @param httpSecurity the HttpSecurity to customize
     * @return the configured SecurityFilterChain
     * @throws Exception if an error occurs while configuring security
     */

    private final String[] noauthResourceUris = {
            "/swagger-ui",
            "/swagger-ui/*",
            "v3/api-docs/**",
            "/swagger-resources/**",
            "/api-docs/**",
            "/aggregate/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        log.info("Initializing Spring Security Filter Chain...");

        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable) //disable CSRF protection
                //.authorizeHttpRequests(authorize -> authorize
                //        .anyRequest().permitAll()) // ALl requests temporarily permitted
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(noauthResourceUris)
                        .permitAll()
                        .anyRequest().authenticated()) //All request require authentication
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(Customizer.withDefaults()))
                .build();


    }
}
