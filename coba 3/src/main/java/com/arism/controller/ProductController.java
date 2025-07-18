package com.arism.controller;

import com.arism.dto.ProductDto;
import com.arism.dto.ProductListDto;
import com.arism.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ProductDto> createProduct(
            @RequestPart(value = "product") @Valid ProductDto productDto,
            @RequestPart(value = "image", required = false) MultipartFile image ) throws IOException {
        // Here, the user (with ADMIN role) can post Product in ProductDto object,
        // then productService process the ProductDto into Product object to persist in the DB
        return ResponseEntity.ok(productService.createProduct(productDto, image));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long id,
                                                    @RequestPart(value = "product") @Valid ProductDto productDto,
                                                    @RequestPart(value = "image", required = false) MultipartFile image) throws IOException {
        // The ADMIN user can update product by sending ProductDto object with image as optional
        // then productService process and persist it into DB
        return ResponseEntity.ok(productService.updateProduct(id, productDto, image));
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) throws IOException {
        // ADMIN User can delete product by providing product id, returning no Content response
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable Long id) {
        // this endpoint can be accessed publicly without authentication by providing the id of the product
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @GetMapping
    public ResponseEntity<List<ProductListDto>> getAllProducts() {
        // Also unauthenticated user can access all products
        return ResponseEntity.ok(productService.getAllProducts());
    }
}
