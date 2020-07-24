package org.wj.prajumsook.service;

import org.wj.prajumsook.domain.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductService {

    Mono<Product> findById(String productId);
    Flux<Product> findAll();
    Mono<Product> save(Product product);
    Mono<Product> delete(String productId);

}
