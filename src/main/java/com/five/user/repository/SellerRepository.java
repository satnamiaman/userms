package com.five.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.five.user.entity.Seller;

public interface SellerRepository extends JpaRepository<Seller, String> {

	Seller findByEmail(String Email);

	Optional<Seller> findByphoneno(String phoneno);
}

