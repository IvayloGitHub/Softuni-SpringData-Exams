package softuni.exam.instagraphlite.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import softuni.exam.instagraphlite.models.entity.UserEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    boolean existsByUsername(String username);

    Optional<UserEntity> findByUsername(String username);
//    @Query("SELECT DISTINCT u FROM UserEntity AS u JOIN FETCH u.posts AS p ORDER BY SIZE(p) DESC, u.id")
    @Query("SELECT u FROM UserEntity AS u ORDER BY SIZE(u.posts) DESC, u.id")
    List<UserEntity> OrderAllUsersByPostsCountThanById();
}
