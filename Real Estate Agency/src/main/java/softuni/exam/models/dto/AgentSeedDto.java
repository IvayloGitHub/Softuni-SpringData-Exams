package softuni.exam.models.dto;

import com.google.gson.annotations.Expose;
import softuni.exam.models.entity.TownEntity;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class AgentSeedDto {

    @Expose
    private String firstName;
    @Expose
    private String lastName;
    @Expose
    private String town;
    @Expose
    private String email;

    @NotBlank
    @Size(min = 2)
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @NotBlank
    @Size(min = 2)
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @NotBlank
    @Size(min = 2)
    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    @Email
    @NotBlank
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
