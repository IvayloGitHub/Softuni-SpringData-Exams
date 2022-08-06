package softuni.exam.models.entity;

import softuni.exam.models.entity.enums.ApartmentEnum;

import javax.persistence.*;

@Entity
@Table(name = "apartments")
public class ApartmentEntity extends BaseEntity{

    private ApartmentEnum apartmentType;
    private Double area;
    private TownEntity town;

    @Enumerated(EnumType.STRING)
    public ApartmentEnum getApartmentType() {
        return apartmentType;
    }

    public void setApartmentType(ApartmentEnum apartmentType) {
        this.apartmentType = apartmentType;
    }

    @Column
    public Double getArea() {
        return area;
    }

    public void setArea(Double area) {
        this.area = area;
    }

    @ManyToOne
    public TownEntity getTown() {
        return town;
    }

    public void setTown(TownEntity town) {
        this.town = town;
    }
}
