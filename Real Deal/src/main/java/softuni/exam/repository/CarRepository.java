package softuni.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import softuni.exam.models.entity.CarEntity;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<CarEntity, Long> {
    @Query("SELECT c FROM CarEntity AS c ORDER BY SIZE(c.pictures) DESC, c.make")
    List<CarEntity> findAllCarsByPicturesCountDescThenByMake();
}
