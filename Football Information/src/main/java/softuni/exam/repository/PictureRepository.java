package softuni.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.exam.domain.entities.PictureEntity;

import java.util.Optional;

@Repository
public interface PictureRepository extends JpaRepository<PictureEntity, Long> {
    Optional<PictureEntity> findByUrl(String url);
    boolean existsByUrl(String url);
}
