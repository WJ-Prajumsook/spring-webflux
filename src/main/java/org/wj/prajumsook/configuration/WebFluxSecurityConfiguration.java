/*
package org.wj.prajumsook.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
public class WebFluxSecurityConfiguration {

    @Value("${spring.security.basic.auth.username}")
    private String username;
    @Value("${spring.security.basic.auth.password}")
    private String password;
    @Value("${spring.security.basic.auth.roles}")
    private String roles;

    @Bean
    public MapReactiveUserDetailsService userDetailsService() {*/

    /*UserDetails userDetails = User.withDefaultPasswordEncoder()
                .username(username)
                .password(password)
                .roles(roles)
                .build();*/
/*
        UserDetails userDetails = User.withUsername(username)
                .password(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(password))
                .roles(roles)
                .build();

        return new MapReactiveUserDetailsService(userDetails);
    }

    @Bean
    public SecurityWebFilterChain filterChain(ServerHttpSecurity http) {
        return http.authorizeExchange(
                authorizeExchangeSpec -> authorizeExchangeSpec.anyExchange().authenticated()
        )
        .exceptionHandling()
        .authenticationEntryPoint((response, error) -> Mono.fromRunnable(() -> {
            response.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        })).accessDeniedHandler((response, error) -> Mono.fromRunnable(() -> {
            response.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
        })).and()
        .httpBasic(Customizer.withDefaults())
        .formLogin().disable()
        .csrf().disable()
        .build();
    }
}
*/
