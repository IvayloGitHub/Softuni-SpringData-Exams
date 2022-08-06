package softuni.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import softuni.exam.models.entity.CityEntity;
import softuni.exam.models.entity.ForecastEntity;
import softuni.exam.models.entity.enums.DayOfWeekEnum;

import java.util.List;
import java.util.Set;

@Repository
public interface ForecastRepository extends JpaRepository<ForecastEntity, Long> {
    boolean existsByDayOfWeekAndCity(DayOfWeekEnum dayOfWeek, CityEntity city);
//second option    List<ForecastEntity> findAllByDayOfWeekAndCity_PopulationLessThanOrderByMaxTemperatureDescIdAsc(DayOfWeekEnum dayOfWeek, Integer city_population);
    @Query("SELECT f FROM ForecastEntity AS f WHERE f.dayOfWeek = :day AND f.city.population < :population ORDER BY f.maxTemperature DESC, f.id")
    List<ForecastEntity> findAllForecastForSunday(@Param("day") DayOfWeekEnum day, @Param("population") Integer population);

}
