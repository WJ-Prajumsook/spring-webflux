package org.wj.prajumsook.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.wj.prajumsook.domain.Product;

public interface ProductRepository extends ReactiveMongoRepository<Product, String> {
}
