package com.example.Spring_product.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Spring_product.Entity.ProductEntity;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

}
