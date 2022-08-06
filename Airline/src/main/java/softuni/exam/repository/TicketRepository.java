package softuni.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.exam.models.entity.TicketEntity;

@Repository
public interface TicketRepository extends JpaRepository<TicketEntity, Long> {
    boolean existsBySerialNumber(String serialNumber);
}
