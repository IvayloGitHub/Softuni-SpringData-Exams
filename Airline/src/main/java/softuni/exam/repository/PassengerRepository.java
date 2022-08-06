package softuni.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import softuni.exam.models.entity.PassengerEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface PassengerRepository  extends JpaRepository<PassengerEntity, Long> {
    boolean existsByEmail(String email);

    Optional<PassengerEntity> findByEmail(String email);

    @Query("SELECT p FROM PassengerEntity AS p ORDER BY SIZE(p.tickets) DESC, p.email")
    List<PassengerEntity> OrderAllPassengersByTicketsCountDescThenByEmail();
}
