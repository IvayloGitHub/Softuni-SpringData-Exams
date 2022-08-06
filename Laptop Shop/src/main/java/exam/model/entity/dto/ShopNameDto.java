package exam.model.entity.dto;

import com.google.gson.annotations.Expose;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class ShopNameDto {

    @Expose
    private String name;

    @NotBlank
    @Size(min = 4)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
