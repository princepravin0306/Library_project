package com.libraryApp.libraryApp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.accept.ContentNegotiationStrategy;
import org.springframework.web.accept.HeaderContentNegotiationStrategy;

import com.okta.spring.boot.oauth.Okta;
import org.springframework.web.cors.CorsConfigurationSource;
import java.util.Arrays;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

        private static final String[] SECURED_URLs = { "/api/books/secure/**",
                        "/api/reviews/secure/**",
                        "/api/messages/secure/**",
                        "/api/admin/secure/**" };

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
                // Disable cross site request forgery
                httpSecurity.csrf(Customizer.withDefaults());

                // protect endpoint at /api/<type>/secure
                httpSecurity.authorizeHttpRequests(authorize -> authorize
                                .requestMatchers(SECURED_URLs).authenticated())
                                .oauth2ResourceServer(oauth2 -> oauth2
                                                .jwt(Customizer.withDefaults()));

                // Add cors filters
                httpSecurity.cors(Customizer.withDefaults());

                // Add content negotiation strategy
                httpSecurity.setSharedObject(ContentNegotiationStrategy.class,
                                new HeaderContentNegotiationStrategy());

                // force a non-empty response body for 401's to make the response friendly
                Okta.configureResourceServer401ResponseBody(httpSecurity);

                return httpSecurity.build();
        }

        @Bean
        CorsConfigurationSource corsConfigurationSource() {
                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
                configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
                configuration.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization"));
                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuration);
                return source;
        }

}