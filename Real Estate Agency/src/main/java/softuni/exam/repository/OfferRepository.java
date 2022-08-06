package softuni.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import softuni.exam.models.entity.OfferEntity;
import softuni.exam.models.entity.enums.ApartmentEnum;

import java.util.List;
import java.util.Optional;

@Repository
public interface OfferRepository extends JpaRepository<OfferEntity, Long> {

    @Query("SELECT o FROM OfferEntity AS o WHERE o.apartment.apartmentType = 'three_rooms' ORDER BY o.apartment.area DESC, o.price")
    List<OfferEntity> findBestOffers();

}
