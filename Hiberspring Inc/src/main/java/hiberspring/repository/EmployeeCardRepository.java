package hiberspring.repository;

import hiberspring.domain.entities.EmployeeCardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeCardRepository extends JpaRepository<EmployeeCardEntity, Long> {

    boolean existsByNumber(String number);

    Optional<EmployeeCardEntity> findByNumber(String number);
}
