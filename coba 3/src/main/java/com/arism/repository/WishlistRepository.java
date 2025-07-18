package com.arism.repository;

import com.arism.model.Wishlist;
import com.arism.model.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    List<Wishlist> findByUser(User user);
}