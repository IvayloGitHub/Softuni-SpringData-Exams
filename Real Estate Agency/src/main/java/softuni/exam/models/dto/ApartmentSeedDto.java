package softuni.exam.models.dto;

import softuni.exam.models.entity.enums.ApartmentEnum;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class ApartmentSeedDto {

    @XmlElement(name = "apartmentType")
    private ApartmentEnum apartmentType;
    @XmlElement(name = "area")
    private Double area;
    @XmlElement(name = "town")
    private String town;

    @NotNull
    public ApartmentEnum getApartmentType() {
        return apartmentType;
    }

    public void setApartmentType(ApartmentEnum apartmentType) {
        this.apartmentType = apartmentType;
    }

    @NotNull
    @DecimalMin("40.0")
    public Double getArea() {
        return area;
    }

    public void setArea(Double area) {
        this.area = area;
    }

    @NotBlank
    @Size(min = 2)
    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }
}
