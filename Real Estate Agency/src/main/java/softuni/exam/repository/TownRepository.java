package softuni.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.exam.models.entity.TownEntity;

import java.util.Optional;

@Repository
public interface TownRepository extends JpaRepository<TownEntity, Long> {

    boolean existsByTownName(String townName);
    Optional<TownEntity> findByTownName(String townName);
}
