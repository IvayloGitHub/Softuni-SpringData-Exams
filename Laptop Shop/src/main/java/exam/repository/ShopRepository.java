package exam.repository;

import exam.model.entity.ShopEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShopRepository extends JpaRepository<ShopEntity, Long> {
    boolean existsByName(String name);

    Optional<ShopEntity> findByName(String name);
}
