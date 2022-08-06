package softuni.exam.models.dto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@XmlAccessorType(XmlAccessType.FIELD)
public class TicketSeedDto {

    @XmlElement(name = "serial-number")
    private String serialNumber;

    @XmlElement(name = "price")
    private BigDecimal price;

    @XmlElement(name = "take-off")
    private String takeoff;

    @XmlElement(name = "from-town")
    private FromTownNameDto fromTown;

    @XmlElement(name = "to-town")
    private ToTownNameDto toTown;

    @XmlElement(name = "passenger")
    private PassengerEmailDto passenger;

    @XmlElement(name = "plane")
    private PlaneRegisterNumberDto plane;

    @Size(min = 2)
    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    @Positive
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getTakeoff() {
        return takeoff;
    }

    public void setTakeoff(String takeoff) {
        this.takeoff = takeoff;
    }

    public FromTownNameDto getFromTown() {
        return fromTown;
    }

    public void setFromTown(FromTownNameDto fromTown) {
        this.fromTown = fromTown;
    }

    public ToTownNameDto getToTown() {
        return toTown;
    }

    public void setToTown(ToTownNameDto toTown) {
        this.toTown = toTown;
    }

    public PassengerEmailDto getPassenger() {
        return passenger;
    }

    public void setPassenger(PassengerEmailDto passenger) {
        this.passenger = passenger;
    }

    public PlaneRegisterNumberDto getPlane() {
        return plane;
    }

    public void setPlane(PlaneRegisterNumberDto plane) {
        this.plane = plane;
    }
}
