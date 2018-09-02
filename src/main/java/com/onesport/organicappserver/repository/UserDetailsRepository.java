package com.onesport.organicappserver.repository;

import com.onesport.organicappserver.entity.UserDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDetailsRepository extends JpaRepository<UserDetailsEntity, Integer> {

    List<UserDetailsEntity> findByEmail(@Param("email") String email);
}
