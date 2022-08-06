package hiberspring.domain.entities;

import javax.persistence.*;

@Entity
@Table(name = "products")
public class ProductEntity extends BaseEntity{

    private String name;
    private Integer clients;
    private BranchEntity branch;

    public ProductEntity() {
    }

    @Column
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getClients() {
        return clients;
    }

    @Column
    public void setClients(Integer clients) {
        this.clients = clients;
    }

    @ManyToOne
    public BranchEntity getBranch() {
        return branch;
    }

    public void setBranch(BranchEntity branch) {
        this.branch = branch;
    }
}
