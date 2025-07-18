package com.arism.service;

import com.arism.dto.ProductDto;
import com.arism.dto.ProductListDto;
import com.arism.exception.ResourceNotFoundException;
import com.arism.mapper.ProductMapper;
import com.arism.model.Product;
import com.arism.repository.ProductRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @Mock
    private MultipartFile mockImage;

    @InjectMocks
    private ProductService productService;

    @Test
    void createProduct_shouldSaveProductWithoutImage() throws Exception {
        ProductDto inputDto = new ProductDto(); // populate if needed
        Product product = new Product();
        Product savedProduct = new Product();
        ProductDto outputDto = new ProductDto();

        when(productMapper.toEntity(inputDto)).thenReturn(product);
        when(mockImage.isEmpty()).thenReturn(true); // simulate no image
        when(productRepository.save(product)).thenReturn(savedProduct);
        when(productMapper.toDto(savedProduct)).thenReturn(outputDto);

        ProductDto result = productService.createProduct(inputDto, mockImage);

        Assertions.assertThat(result).isEqualTo(outputDto);
        verify(productRepository).save(product);
        verify(mockImage, times(1)).isEmpty();
    }

    @Test
    void createProduct_shouldSaveProductWithImage() throws Exception {
        ProductDto inputDto = new ProductDto();
        Product product = new Product();
        Product savedProduct = new Product();
        ProductDto outputDto = new ProductDto();

        byte[] dummyBytes = "fake image".getBytes();

        // Mocks
        when(productMapper.toEntity(inputDto)).thenReturn(product);
        when(mockImage.isEmpty()).thenReturn(false);
        when(productRepository.save(any())).thenReturn(savedProduct);
        when(productMapper.toDto(savedProduct)).thenReturn(outputDto);

        // Create spy of the service to override saveImage()
        ProductService spyService = Mockito.spy(productService);
        doReturn("fixed-timestamp-product.png").when(spyService).saveImage(mockImage);

        // Call method
        ProductDto result = spyService.createProduct(inputDto, mockImage);

        // Verify
        Assertions.assertThat(result).isEqualTo(outputDto);
        Assertions.assertThat(product.getImage()).isEqualTo("/images/fixed-timestamp-product.png");
        verify(productRepository).save(product);
    }

    @Test
    void getProductById_shouldReturnDto_whenProductExists() {
        Long id = 1L;
        Product product = new Product();
        ProductDto productDto = new ProductDto();

        when(productRepository.findById(id)).thenReturn(Optional.of(product));
        when(productMapper.toDto(product)).thenReturn(productDto);

        ProductDto result = productService.getProductById(id);

        Assertions.assertThat(result).isEqualTo(productDto);
        verify(productRepository).findById(id);
        verify(productMapper).toDto(product);
    }

    @Test
    void getProductById_shouldThrow_whenProductNotFound() {
        Long id = 2L;

        when(productRepository.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> productService.getProductById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Product not found");

        verify(productRepository).findById(id);
        verifyNoInteractions(productMapper); // mapper shouldn't be called
    }

    @Test
    void getAllProducts_shouldReturnList_whenProductsExist() {
        List<ProductListDto> mockProducts = List.of(
                new ProductListDto(1L, "Product A", "Test", BigDecimal.valueOf(100), 10, null),
                new ProductListDto(2L, "Product B", "Test", BigDecimal.valueOf(200), 21, null)
        );

        when(productRepository.findAllWithoutComments()).thenReturn(mockProducts);

        List<ProductListDto> result = productService.getAllProducts();

        Assertions.assertThat(result).hasSize(2);
        Assertions.assertThat(result).isEqualTo(mockProducts);
        verify(productRepository).findAllWithoutComments();
    }

    @Test
    void getAllProducts_shouldReturnEmptyList_whenNoProductsExist() {
        when(productRepository.findAllWithoutComments()).thenReturn(Collections.emptyList());

        List<ProductListDto> result = productService.getAllProducts();

        Assertions.assertThat(result).isEmpty();
        verify(productRepository).findAllWithoutComments();
    }

}
