package com.example.football.repository;

import com.example.football.models.entity.PlayerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PlayerRepository extends JpaRepository<PlayerEntity, Long> {

    boolean existsByEmail(String email);
    @Query("SELECT p FROM PlayerEntity AS p WHERE p.birthDate > :birthDate1 AND p.birthDate < :birthDate2 ORDER BY p.stat.shooting DESC , p.stat.passing DESC, p.stat.endurance DESC, p.lastName")
    List<PlayerEntity> bestPlayersByStats(@Param("birthDate1") LocalDate birthDate1, @Param("birthDate2") LocalDate birthDate2);
}
