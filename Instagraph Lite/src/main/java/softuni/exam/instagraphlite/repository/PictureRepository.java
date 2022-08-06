package softuni.exam.instagraphlite.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.exam.instagraphlite.models.entity.PictureEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface PictureRepository extends JpaRepository<PictureEntity, Long> {
    boolean existsByPath(String path);

    Optional<PictureEntity> findByPath(String path);

    List<PictureEntity> findAllBySizeGreaterThanOrderBySize(Double size);


}
