package softuni.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.exam.models.entity.ApartmentEntity;
import softuni.exam.models.entity.TownEntity;

import java.util.Optional;

@Repository
public interface ApartmentRepository extends JpaRepository<ApartmentEntity, Long> {

    boolean existsByTownAndArea(TownEntity town, Double area);
//    Optional<ApartmentEntity> findApartmentEntitiesByTownAndArea(TownEntity town, Double area);
}
