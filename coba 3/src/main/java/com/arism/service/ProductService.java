package com.arism.service;

import com.arism.dto.ProductDto;
import com.arism.dto.ProductListDto;
import com.arism.exception.ResourceNotFoundException;
import com.arism.mapper.ProductMapper;
import com.arism.model.Product;
import com.arism.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    private static final String UPLOAD_DIR = "src/main/resources/static/images/";

    @Transactional
    public ProductDto createProduct(ProductDto productDto, MultipartFile image) throws IOException {
        // Map the request (productDto) into Product object
        Product product = productMapper.toEntity(productDto);

        // Check if the request contains 'image' field
        if (image!=null && !image.isEmpty()) {
            String fileName = saveImage(image);
            product.setImage("/images/"+fileName);
        }

        // Persist in the DB and return it as ProductDto object
        Product savedProduct = productRepository.save(product);
        return productMapper.toDto(savedProduct);
    }

    @Transactional
    public ProductDto updateProduct(Long id, ProductDto productDto, MultipartFile image) throws IOException {
        // Check if the product id is exist in the DB
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Product not found"));

        // Set properties for the product from the request
        existingProduct.setName(productDto.getName());
        existingProduct.setDescription(productDto.getDescription());
        existingProduct.setPrice(productDto.getPrice());
        existingProduct.setQuantity(productDto.getQuantity());

        // Check if image request is not empty
        if (image!=null && !image.isEmpty()) {
            String fileName = saveImage(image);
            existingProduct.setImage("/images/"+fileName);
        }

        // Persist updated product into DB and return it as ProductDto object
        Product updatedProduct =  productRepository.save(existingProduct);
        return productMapper.toDto(updatedProduct);
    }

    @Transactional
    public void deleteProduct(Long id) throws IOException {
        // Check if the product id is exist in the DB
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found");
        }

        // Execute product deletion
        productRepository.deleteById(id);
    }

    public ProductDto getProductById(Long id) {
        // Check if the product id is exist in the DB and return it as ProductDto object
        Product product = productRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Product not found"));
        return productMapper.toDto(product);
    }

    public List<ProductListDto> getAllProducts() {
        // Get All Products List
        return productRepository.findAllWithoutComments();
    }

    public String saveImage(MultipartFile image) throws IOException {
        // Set the fileName to be saved, set the path to save
        String fileName = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + "-" + image.getOriginalFilename();
        Path path = Paths.get(UPLOAD_DIR+fileName);
        Files.createDirectories(path.getParent());

        // Write the file into local storage
        Files.write(path, image.getBytes());
        return fileName;
    }
}
