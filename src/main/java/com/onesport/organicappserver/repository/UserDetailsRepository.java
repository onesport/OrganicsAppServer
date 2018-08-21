package com.onesport.organicappserver.repository;

import com.onesport.organicappserver.entity.UserDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDetailsRepository extends JpaRepository<UserDetailsEntity, Integer> {
    UserDetailsEntity findByEmail(@Param("email") String email);
}
