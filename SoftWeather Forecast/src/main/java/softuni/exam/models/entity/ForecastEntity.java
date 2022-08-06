package softuni.exam.models.entity;

import softuni.exam.models.entity.enums.DayOfWeekEnum;

import javax.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = "forecasts")
public class ForecastEntity extends BaseEntity {

    private DayOfWeekEnum dayOfWeek;
    private Double maxTemperature;
    private Double minTemperature;
    private LocalTime sunrise;
    private LocalTime sunset;
    private CityEntity city;

    public ForecastEntity() {
    }

    @Enumerated(EnumType.STRING)
    public DayOfWeekEnum getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeekEnum dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    @Column(name = "max_temperature")
    public Double getMaxTemperature() {
        return maxTemperature;
    }

    public void setMaxTemperature(Double maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    @Column(name = "min_temperature")
    public Double getMinTemperature() {
        return minTemperature;
    }

    public void setMinTemperature(Double minTemperature) {
        this.minTemperature = minTemperature;
    }

    @Column
    public LocalTime getSunrise() {
        return sunrise;
    }

    public void setSunrise(LocalTime sunrise) {
        this.sunrise = sunrise;
    }

    public LocalTime getSunset() {
        return sunset;
    }

    @Column
    public void setSunset(LocalTime sunset) {
        this.sunset = sunset;
    }

    @ManyToOne
    public CityEntity getCity() {
        return city;
    }

    public void setCity(CityEntity city) {
        this.city = city;
    }
}
