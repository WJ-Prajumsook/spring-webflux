package org.wj.prajumsook.service;

import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.wj.prajumsook.domain.Product;
import org.wj.prajumsook.repository.ProductRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;

    @Override
    public Mono<Product> findById(String productId) {
        return productRepository.findById(productId);
    }

    @Override
    public Flux<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Mono<Product> save(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Mono<Product> delete(String productId) {
        return productRepository.findById(productId)
                .flatMap(product -> productRepository.deleteById(product.getId())
                .thenReturn(product));
    }
}
