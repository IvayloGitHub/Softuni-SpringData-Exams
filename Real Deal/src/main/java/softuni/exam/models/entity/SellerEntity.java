package softuni.exam.models.entity;

import softuni.exam.models.entity.enums.RatingEnum;

import javax.persistence.*;

@Entity
@Table(name = "sellers")
public class SellerEntity extends BaseEntity {
    private String firstName;
    private String lastName;
    private String email;
    private RatingEnum rating;
    private String town;

    public SellerEntity() {
    }

    @Column
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Column
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Column(unique = true)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Enumerated(EnumType.STRING)
    public RatingEnum getRating() {
        return rating;
    }

    public void setRating(RatingEnum rating) {
        this.rating = rating;
    }

    @Column
    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }
}
