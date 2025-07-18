package com.arism.repository;

import com.arism.model.Cart;
import com.arism.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    Optional<Comment> findByProductId(Long productId);
}
