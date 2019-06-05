package com.amazon.repository;

import com.amazon.domain.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerRepository extends JpaRepository<Seller, Long> {

    Seller findByBusinessDisplayName(String businessDisplayName);


}
