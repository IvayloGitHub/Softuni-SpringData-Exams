package softuni.exam.models.entity;

import softuni.exam.models.entity.AgentEntity;
import softuni.exam.models.entity.ApartmentEntity;
import softuni.exam.models.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "offers")
public class OfferEntity extends BaseEntity {

    private BigDecimal price;
    private LocalDate publishedOn;
    private ApartmentEntity apartment;
    private AgentEntity agent;

    @Column
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Column(name = "published_on")
    public LocalDate getPublishedOn() {
        return publishedOn;
    }

    public void setPublishedOn(LocalDate publishedOn) {
        this.publishedOn = publishedOn;
    }

    @ManyToOne
    public ApartmentEntity getApartment() {
        return apartment;
    }

    public void setApartment(ApartmentEntity apartment) {
        this.apartment = apartment;
    }

    @ManyToOne
    public AgentEntity getAgent() {
        return agent;
    }

    public void setAgent(AgentEntity agent) {
        this.agent = agent;
    }
}
