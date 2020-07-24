package org.wj.prajumsook.configuration;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.security.web.server.savedrequest.NoOpServerRequestCache;
import org.wj.prajumsook.repository.UserRepository;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
@AllArgsConstructor
public class WebFluxSecurityDBConfiguration {

    private UserRepository userRepository;
    private AuthenticationManager authenticationManager;
    private SecurityContextRepository securityContextRepository;

    @Bean
    ReactiveUserDetailsService userDetailsService() {
        return (name) -> userRepository.findByUsername(name);
    }

    @Bean
    public SecurityWebFilterChain filterChain(ServerHttpSecurity http) {
        return http.authorizeExchange(
                authorizeExchangeSpec -> authorizeExchangeSpec
                        .pathMatchers("/token").permitAll()
                        .anyExchange().authenticated()
        )
                .exceptionHandling()
                .authenticationEntryPoint((response, error) -> Mono.fromRunnable(() -> {
                    response.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                })).accessDeniedHandler((response, error) -> Mono.fromRunnable(() -> {
                    response.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                })).and()
                .httpBasic().disable()
                .formLogin().disable()
                .csrf().disable()
                .authenticationManager(authenticationManager)
                .securityContextRepository(securityContextRepository)
                .requestCache().requestCache(NoOpServerRequestCache.getInstance())
                .and()
                .build();
    }
}
