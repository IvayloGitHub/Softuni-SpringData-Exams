package softuni.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.exam.models.entity.PlaneEntity;

import java.util.Optional;

@Repository
public interface PlaneRepository extends JpaRepository<PlaneEntity, Long> {
    boolean existsByRegisterNumber(String registerNumber);

    Optional<PlaneEntity> findByRegisterNumber(String registerNumber);
}
