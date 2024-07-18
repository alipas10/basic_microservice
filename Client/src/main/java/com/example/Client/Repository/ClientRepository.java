package com.example.Client.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Client.Entity.ClientEntiry;

public interface ClientRepository extends JpaRepository<ClientEntiry, Long> {

}
