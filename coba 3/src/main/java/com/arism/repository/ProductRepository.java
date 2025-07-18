package com.arism.repository;

import com.arism.dto.ProductListDto;
import com.arism.model.Comment;
import com.arism.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,Long> {
    @Query("SELECT new com.arism.dto.ProductListDto(p.id, p.name, p.description, p.price, p.quantity, p.image) FROM Product p")
    List<ProductListDto> findAllWithoutComments();
}
