package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.CitySeedDto;
import softuni.exam.models.entity.CityEntity;
import softuni.exam.repository.CityRepository;
import softuni.exam.service.CityService;
import softuni.exam.service.CountryService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Service
public class CityServiceImpl implements CityService {
    private static final String CITIES_FILE_PATH = "src/main/resources/files/json/cities.json";
    private final CityRepository cityRepository;
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final ValidationUtil validationUtil;
    private final CountryService countryService;

    public CityServiceImpl(CityRepository cityRepository, ModelMapper modelMapper, Gson gson, ValidationUtil validationUtil, CountryService countryService) {
        this.cityRepository = cityRepository;
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.validationUtil = validationUtil;
        this.countryService = countryService;
    }

    @Override
    public boolean areImported() {
        return cityRepository.count() > 0;
    }

    @Override
    public String readCitiesFileContent() throws IOException {
        return Files
                .readString(Path.of(CITIES_FILE_PATH));
    }

    @Override
    public String importCities() throws IOException {
        StringBuilder sb = new StringBuilder();

        Arrays.stream(gson.fromJson(readCitiesFileContent(), CitySeedDto[].class))
                .filter(citySeedDto -> {
                    boolean isValid = validationUtil.isValid(citySeedDto)
                            && !isCityEntityExistsByName(citySeedDto.getCityName());

                    sb
                            .append(isValid ? String.format("Successfully imported city %s - %d",
                                    citySeedDto.getCityName(), citySeedDto.getPopulation())
                                    : "Invalid city")
                            .append(System.lineSeparator());
                    return isValid;
                })
                .map(citySeedDto -> {
                    CityEntity city = modelMapper.map(citySeedDto, CityEntity.class);
                    city.setCountry(countryService.findCountryById(citySeedDto.getCountry()));
                    return city;
                })
                .forEach(cityRepository::save);
        return sb.toString();
    }

    @Override
    public CityEntity findCityById(Long id) {
        return cityRepository.findById(id)
                .orElse(null);
    }

    private boolean isCityEntityExistsByName(String cityName) {
        return cityRepository.existsByCityName(cityName);
    }
}
