package com.onesport.organicappserver.repository;

import com.onesport.organicappserver.entity.ProductsEntity;
import com.onesport.organicappserver.entity.UserDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductsRepository extends JpaRepository<ProductsEntity, Integer> {
    List<ProductsEntity> getAllProducts(@Param("status") ProductsEntity.ProductAvailability productStatus);
}
