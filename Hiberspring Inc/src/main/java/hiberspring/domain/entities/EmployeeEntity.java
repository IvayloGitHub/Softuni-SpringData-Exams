package hiberspring.domain.entities;

import javax.persistence.*;

@Entity
@Table(name = "employees")
public class EmployeeEntity extends BaseEntity{

    private String firstName;
    private String lastName;
    private String position;
    private EmployeeCardEntity card;
    private BranchEntity branch;

    public EmployeeEntity() {
    }

    @Column(name = "first_name")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Column(name = "last_name")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Column
    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @OneToOne
    public EmployeeCardEntity getCard() {
        return card;
    }

    public void setCard(EmployeeCardEntity card) {
        this.card = card;
    }

    @ManyToOne
    public BranchEntity getBranch() {
        return branch;
    }

    public void setBranch(BranchEntity branch) {
        this.branch = branch;
    }
}
