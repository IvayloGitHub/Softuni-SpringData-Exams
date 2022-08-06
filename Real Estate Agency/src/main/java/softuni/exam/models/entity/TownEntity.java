package softuni.exam.models.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "towns")
public class TownEntity extends BaseEntity{

    private String townName;
    private Integer population;

    public TownEntity() {
    }

    @Column(name = "town_name", unique = true)
    public String getTownName() {
        return townName;
    }

    public void setTownName(String townName) {
        this.townName = townName;
    }

    @Column
    public Integer getPopulation() {
        return population;
    }

    public void setPopulation(Integer population) {
        this.population = population;
    }
}
