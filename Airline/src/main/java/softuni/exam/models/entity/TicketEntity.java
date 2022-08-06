package softuni.exam.models.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tickets")
public class TicketEntity extends BaseEntity {
    private String serialNumber;
    private BigDecimal price;
    private LocalDateTime takeoff;

    private PassengerEntity passenger;

    private PlaneEntity plane;

    private TownEntity fromTown;

    private TownEntity toTown;

    public TicketEntity() {
    }

    @Column(unique = true)
    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    @Column
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Column
    public LocalDateTime getTakeoff() {
        return takeoff;
    }

    public void setTakeoff(LocalDateTime takeoff) {
        this.takeoff = takeoff;
    }

    @ManyToOne
    public PassengerEntity getPassenger() {
        return passenger;
    }

    public void setPassenger(PassengerEntity passenger) {
        this.passenger = passenger;
    }

    @ManyToOne
    public PlaneEntity getPlane() {
        return plane;
    }

    public void setPlane(PlaneEntity plane) {
        this.plane = plane;
    }

    @ManyToOne
    public TownEntity getFromTown() {
        return fromTown;
    }

    public void setFromTown(TownEntity fromTown) {
        this.fromTown = fromTown;
    }

    @ManyToOne
    public TownEntity getToTown() {
        return toTown;
    }

    public void setToTown(TownEntity toTown) {
        this.toTown = toTown;
    }
}
