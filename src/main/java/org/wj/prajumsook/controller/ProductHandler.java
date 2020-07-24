package org.wj.prajumsook.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerRequestExtensionsKt;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.wj.prajumsook.configuration.AuthResponse;
import org.wj.prajumsook.configuration.JWTUtil;
import org.wj.prajumsook.domain.Product;
import org.wj.prajumsook.domain.User;
import org.wj.prajumsook.repository.UserRepository;
import org.wj.prajumsook.service.ProductService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class ProductHandler {

    private ProductService productService;
    private UserRepository userRepository;
    private JWTUtil jwtUtil;

    public Mono<ServerResponse> findAll(ServerRequest serverRequest) {
        Flux<Product> productFlux = productService.findAll();

        return ServerResponse.ok()
                .body(productFlux, Product.class);
    }

    public Mono<ServerResponse> findById(ServerRequest serverRequest) {
        String productId = serverRequest.pathVariable("productId");

        return productService.findById(productId)
                .flatMap(product -> ServerResponse.ok().bodyValue(product))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> save(ServerRequest serverRequest) {
        Mono<Product> productMono = serverRequest.bodyToMono(Product.class)
                .flatMap(productService::save);

        return ServerResponse.status(HttpStatus.CREATED).body(productMono, Product.class);
    }

    public Mono<ServerResponse> update(ServerRequest serverRequest) {
        String productId = serverRequest.pathVariable("productId");

        return productService.findById(productId)
                .flatMap(product -> {
                    Mono<Product> updated = serverRequest.bodyToMono(Product.class)
                            .flatMap(productService::save);
                    return ServerResponse.ok().body(updated, Product.class);
                })
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> delete(ServerRequest serverRequest) {
        String productId = serverRequest.pathVariable("productId");

        return productService.findById(productId)
                .flatMap(product -> productService.delete(product.getId())
                .then(ServerResponse.ok().bodyValue(product)))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> getToken(ServerRequest serverRequest) {
        Mono<User> userMono = serverRequest.bodyToMono(User.class);

        return userMono.flatMap(user -> userRepository.findByUsername(user.getUsername())
            .flatMap(userDetails -> {
                if(user.getPassword().equals(userDetails.getPassword())) {
                    return ServerResponse.ok().bodyValue(new AuthResponse(jwtUtil.generateToken(user)));
                } else {
                    return ServerResponse.badRequest().build();
                }
            }).switchIfEmpty(ServerResponse.badRequest().build()));

    }
}
