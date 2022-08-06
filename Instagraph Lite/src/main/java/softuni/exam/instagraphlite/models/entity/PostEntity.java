package softuni.exam.instagraphlite.models.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "posts")
public class PostEntity extends BaseEntity{
    private String caption;
    private UserEntity user;
    private PictureEntity picture;

    public PostEntity() {
    }
    @Column
    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    @ManyToOne
    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    @ManyToOne
    public PictureEntity getPicture() {
        return picture;
    }

    public void setPicture(PictureEntity picture) {
        this.picture = picture;
    }

}
