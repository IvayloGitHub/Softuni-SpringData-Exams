package softuni.exam.instagraphlite.models.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "users")
public class UserEntity extends BaseEntity {

    private String username;
    private String password;
    private PictureEntity profilePicture;

    private Set<PostEntity> posts;

    public UserEntity() {
    }
    @Column(unique = true)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    @Column
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @ManyToOne
    public PictureEntity getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(PictureEntity profilePicture) {
        this.profilePicture = profilePicture;
    }
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    public Set<PostEntity> getPosts() {
        return posts;
    }

    public void setPosts(Set<PostEntity> posts) {
        this.posts = posts;
    }
}
