package softuni.exam.models.dto;

import com.google.gson.annotations.Expose;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class CountrySeedDto {

    @Expose
    private String countryName;
    @Expose
    private String currency;

    @NotBlank
    @Size(min = 2, max = 60)
    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    @NotBlank
    @Size(min = 2, max = 20)
    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
