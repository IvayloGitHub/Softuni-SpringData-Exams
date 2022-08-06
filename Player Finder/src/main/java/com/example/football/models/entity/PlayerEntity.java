package com.example.football.models.entity;

import com.example.football.models.entity.enums.PositionEnum;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "players")
public class PlayerEntity extends BaseEntity{

    private String firstName;
    private String lastName;
    private String email;
    private LocalDate birthDate;
    private PositionEnum position;
    private TownEntity town;
    private TeamEntity team;
    private StatEntity stat;

    public PlayerEntity() {
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

    @Column(unique = true)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "birth_date")
    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    @Enumerated(EnumType.ORDINAL)
    public PositionEnum getPosition() {
        return position;
    }

    public void setPosition(PositionEnum position) {
        this.position = position;
    }

    @ManyToOne
    public TownEntity getTown() {
        return town;
    }

    public void setTown(TownEntity town) {
        this.town = town;
    }

    @ManyToOne
    public TeamEntity getTeam() {
        return team;
    }

    public void setTeam(TeamEntity team) {
        this.team = team;
    }

    @ManyToOne
    public StatEntity getStat() {
        return stat;
    }

    public void setStat(StatEntity stat) {
        this.stat = stat;
    }
}
