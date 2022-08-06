package softuni.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.exam.models.entity.AgentEntity;

import java.util.Optional;

@Repository
public interface AgentRepository extends JpaRepository<AgentEntity, Long> {

    boolean existsByFirstName(String firstName);

    Optional<AgentEntity> findByFirstName(String name);
}
