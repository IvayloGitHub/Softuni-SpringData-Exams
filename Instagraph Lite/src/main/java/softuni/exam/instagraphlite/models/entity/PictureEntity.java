package softuni.exam.instagraphlite.models.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "pictures")
public class PictureEntity extends BaseEntity {

    private String path;
    private Double size;

    public PictureEntity() {
    }

    @Column(unique = true)
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Column
    public Double getSize() {
        return size;
    }

    public void setSize(Double size) {
        this.size = size;
    }
}
