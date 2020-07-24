package org.wj.prajumsook;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.wj.prajumsook.configuration.AuthenticationManager;
import org.wj.prajumsook.configuration.JWTUtil;
import org.wj.prajumsook.configuration.SecurityContextRepository;
import org.wj.prajumsook.configuration.WebFluxSecurityDBConfiguration;
import org.wj.prajumsook.controller.ProductHandler;
import org.wj.prajumsook.controller.ProductRouter;
import org.wj.prajumsook.domain.Product;
import org.wj.prajumsook.repository.UserRepository;
import org.wj.prajumsook.service.ProductService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Log4j2
@WebFluxTest
@ContextConfiguration(classes = {ProductHandler.class, ProductRouter.class, WebFluxSecurityDBConfiguration.class})
@WithMockUser(username = "testuser", password = "testpass", authorities = {"ROLE_USER", "ROLE_ADMIN"})
public class ProductControllerTest {

    @MockBean
    private ProductService productService;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private JWTUtil jwtUtil;
    @MockBean
    private AuthenticationManager authenticationManager;
    @MockBean
    private SecurityContextRepository securityContextRepository;

    private WebTestClient webTestClient;

    @Autowired
    public ProductControllerTest(WebTestClient webTestClient) {
        this.webTestClient = webTestClient;
    }

    @Test
    public void findAllProduct() {
        Mockito.when(productService.findAll())
                .thenReturn(
                        Flux.just(
                                new Product(UUID.randomUUID().toString(), "Macbook Pro", 2999.99),
                                new Product(UUID.randomUUID().toString(), "Macbook Air", 1999.99),
                                new Product(UUID.randomUUID().toString(), "Ipad Pro", 999.99)
                        )
                );
        webTestClient.get()
                .uri("/product")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.[0].name").isEqualTo("Macbook Pro");

        log.info("Test find All Product");
    }

    @Test
    public void findProductByIdTest() {
        Product product = new Product(UUID.randomUUID().toString(), "Macbook Pro", 2999.99);
        Mockito.when(productService.findById(product.getId()))
                .thenReturn(Mono.just(product));

        webTestClient.get()
                .uri("/product/" + product.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo("Macbook Pro")
                .jsonPath("$.price").isEqualTo(2999.99);

        log.info("Test find product by id");
    }

    @Test
    public void saveProductTest() {
        Product product = new Product(UUID.randomUUID().toString(), "Macbook Pro", 2999.99);
        Mockito.when(productService.save(Mockito.any(Product.class)))
                .thenReturn(Mono.just(product));

        webTestClient.post()
                .uri("/product")
                .body(Mono.just(product), Product.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.name").isEqualTo("Macbook Pro")
                .jsonPath("$.price").isEqualTo(2999.99);

        log.info("Test save product");
    }

    @Test
    public void updateProductTest() {
        Product product = new Product(UUID.randomUUID().toString(), "Macbook Pro", 2999.99);
        Mockito.when(productService.findById(product.getId()))
                .thenReturn(Mono.just(product));
        Mockito.when(productService.save(product))
                .thenReturn(Mono.just(product));

        webTestClient.put()
                .uri("/product/" + product.getId())
                .body(Mono.just(product), Product.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo("Macbook Pro")
                .jsonPath("$.price").isEqualTo(2999.99);

        log.info("Test update product");
    }

    @Test
    public void deleteProductTest() {
        Product product = new Product(UUID.randomUUID().toString(), "Macbook Pro", 2999.99);
        Mockito.when(productService.findById(product.getId()))
                .thenReturn(Mono.just(product));
        Mockito.when(productService.delete(product.getId()))
                .thenReturn(Mono.just(product));

        webTestClient.delete()
                .uri("/product/" + product.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo("Macbook Pro")
                .jsonPath("$.price").isEqualTo(2999.99);

        log.info("Test delete product by id");
    }
}
