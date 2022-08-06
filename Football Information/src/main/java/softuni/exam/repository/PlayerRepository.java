package softuni.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import softuni.exam.domain.entities.PlayerEntity;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface PlayerRepository extends JpaRepository<PlayerEntity, Long> {

    @Query("SELECT p FROM PlayerEntity AS p WHERE p.team.name = 'North Hub'ORDER BY p.id")
    List<PlayerEntity> findAllPlayerInTeamNorthHub();

    List<PlayerEntity> findAllBySalaryGreaterThanOrderBySalaryDesc(BigDecimal salary);
}
