package com.arism.controller;

import com.arism.dto.ProductDto;
import com.arism.dto.ProductListDto;
import com.arism.service.ProductService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ProductControllerTest {
    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateProduct() throws IOException {
        // Arrange
        ProductDto productDto = new ProductDto();
        productDto.setName("Test Product");
        productDto.setDescription("Test Description");
        productDto.setPrice(BigDecimal.valueOf(312));
        productDto.setQuantity(6);
        MockMultipartFile image = new MockMultipartFile("image", "test.png", "image/png", "fake-image".getBytes());

        ProductDto savedProduct = new ProductDto();
        savedProduct.setName("Test Product");
        savedProduct.setDescription("Test Description");
        savedProduct.setPrice(BigDecimal.valueOf(312));
        savedProduct.setQuantity(6);

        when(productService.createProduct(productDto, image)).thenReturn(savedProduct);

        // Act
        ResponseEntity<ProductDto> response = productController.createProduct(productDto, image);

        // Assert
        verify(productService).createProduct(productDto, image);
        assertEquals(200, response.getStatusCode().value());
        Assertions.assertNotNull(response.getBody());
        assertEquals("Test Product", response.getBody().getName());
        assertEquals("Test Description", response.getBody().getDescription());
        assertEquals(BigDecimal.valueOf(312), response.getBody().getPrice());
        assertEquals(6, response.getBody().getQuantity());
    }

    @Test
    void testUpdateProduct() throws IOException {
        // Arrange
        Long productId = 1L;
        ProductDto updatedDto = new ProductDto();
        updatedDto.setName("Updated Product");

        MockMultipartFile image = new MockMultipartFile("image", "new-image.jpg", "image/jpeg", "img".getBytes());

        when(productService.updateProduct(productId, updatedDto, image)).thenReturn(updatedDto);

        // Act
        ResponseEntity<ProductDto> response = productController.updateProduct(productId, updatedDto, image);

        // Assert
        verify(productService).updateProduct(productId, updatedDto, image);
        assertEquals(200, response.getStatusCode().value());
        Assertions.assertNotNull(response.getBody());
        assertEquals("Updated Product", response.getBody().getName());
    }

    @Test
    void testDeleteProduct() throws IOException {
        // Arrange
        Long productId = 1L;

        // Act
        ResponseEntity<Void> response = productController.deleteProduct(productId);

        // Assert
        verify(productService).deleteProduct(productId);
        assertEquals(204, response.getStatusCode().value());
        assertNull(response.getBody());
    }

    @Test
    void testGetProductById() {
        // Arrange
        Long productId = 1L;
        ProductDto productDto = new ProductDto();
        productDto.setName("Fetched Product");

        when(productService.getProductById(productId)).thenReturn(productDto);

        // Act
        ResponseEntity<ProductDto> response = productController.getProduct(productId);

        // Assert
        verify(productService).getProductById(productId);
        assertEquals(200, response.getStatusCode().value());
        Assertions.assertNotNull(response.getBody());
        assertEquals("Fetched Product", response.getBody().getName());
    }

    @Test
    void testGetAllProducts() {
        // Arrange
        ProductListDto product1 = new ProductListDto();
        product1.setName("Product 1");
        ProductListDto product2 = new ProductListDto();
        product2.setName("Product 2");

        List<ProductListDto> productList = Arrays.asList(product1, product2);

        when(productService.getAllProducts()).thenReturn(productList);

        // Act
        ResponseEntity<List<ProductListDto>> response = productController.getAllProducts();

        // Assert
        verify(productService).getAllProducts();
        assertEquals(200, response.getStatusCode().value());
        Assertions.assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }

}
