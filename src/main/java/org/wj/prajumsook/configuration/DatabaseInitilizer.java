package org.wj.prajumsook.configuration;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.stereotype.Component;
import org.wj.prajumsook.domain.Product;
import org.wj.prajumsook.domain.User;
import org.wj.prajumsook.repository.ProductRepository;
import org.wj.prajumsook.repository.UserRepository;
import reactor.core.publisher.Flux;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Component
@AllArgsConstructor
@Log4j2
public class DatabaseInitilizer implements ApplicationListener<ApplicationReadyEvent> {

    private ProductRepository productRepository;
    private UserRepository userRepository;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        productRepository.deleteAll()
                .thenMany(
                        Flux.just("Macbook Pro", "Macbook Air", "Ipad Pro", "Ipad Air", "Ipad Mini")
                        .map(
                                name -> new Product(
                                        UUID.randomUUID().toString(), name, ThreadLocalRandom.current()
                                        .nextDouble(1000, 5000)
                                )
                        )
                        .flatMap(productRepository::save)
                )
                .thenMany(productRepository.findAll())
                .subscribe(product -> log.info("Saved product {}", product));

        userRepository.deleteAll()
                .thenMany(Flux.just("wjp").map(user -> new User(
                        user,
                        PasswordEncoderFactories.createDelegatingPasswordEncoder().encode("passw0rd")
                )).flatMap(userRepository::save))
                .thenMany(userRepository.findAll())
                .subscribe(user -> log.info("User {}", user));
    }
}
