package com.example.Spring_order.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Spring_order.Entity.OrderEntity;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long>{

}
