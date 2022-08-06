package softuni.exam.models.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "passengers")
public class PassengerEntity extends BaseEntity {

    private String firstName;
    private String lastName;
    private Integer age;
    private String phoneNumber;
    private String email;

    private TownEntity town;
    private Set<TicketEntity> tickets;

    public PassengerEntity() {
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
    @Column
    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
    @Column
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    @Column(unique = true)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @ManyToOne
    public TownEntity getTown() {
        return town;
    }

    public void setTown(TownEntity town) {
        this.town = town;
    }
    @OneToMany(mappedBy = "passenger", fetch = FetchType.EAGER)
    public Set<TicketEntity> getTickets() {
        return tickets;
    }

    public void setTickets(Set<TicketEntity> tickets) {
        this.tickets = tickets;
    }
}
