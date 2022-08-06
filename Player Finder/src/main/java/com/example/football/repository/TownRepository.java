package com.example.football.repository;


import com.example.football.models.entity.TownEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TownRepository extends JpaRepository<TownEntity, Long> {
    boolean existsByName(String name);

    Optional<TownEntity> findByName(String name);
}
