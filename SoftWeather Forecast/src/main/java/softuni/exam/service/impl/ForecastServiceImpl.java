package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ForecastSeedRootDto;
import softuni.exam.models.entity.CityEntity;
import softuni.exam.models.entity.ForecastEntity;
import softuni.exam.models.entity.enums.DayOfWeekEnum;
import softuni.exam.repository.ForecastRepository;
import softuni.exam.service.CityService;
import softuni.exam.service.ForecastService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class ForecastServiceImpl implements ForecastService {
    private static final String FORECASTS_FILE_PATH = "src/main/resources/files/xml/forecasts.xml";
    private final ForecastRepository forecastRepository;
    private final ModelMapper modelMapper;
    private final XmlParser xmlParser;
    private final ValidationUtil validationUtil;
    private final CityService cityService;

    public ForecastServiceImpl(ForecastRepository forecastRepository, ModelMapper modelMapper, XmlParser xmlParser, ValidationUtil validationUtil, CityService cityService) {
        this.forecastRepository = forecastRepository;
        this.modelMapper = modelMapper;
        this.xmlParser = xmlParser;
        this.validationUtil = validationUtil;
        this.cityService = cityService;
    }

    @Override
    public boolean areImported() {
        return forecastRepository.count() > 0;
    }

    @Override
    public String readForecastsFromFile() throws IOException {
        return Files
                .readString(Path.of(FORECASTS_FILE_PATH));
    }

    @Override
    public String importForecasts() throws IOException, JAXBException {
        StringBuilder sb = new StringBuilder();

        xmlParser.fromFile(FORECASTS_FILE_PATH, ForecastSeedRootDto.class)
                .getForecasts()
                .stream()
                .filter(forecastSeedDto -> {
                    boolean isValid = validationUtil.isValid(forecastSeedDto)
                            && !isForecastExistByDayAndCity(forecastSeedDto.getDayOfWeek(),
                            cityService.findCityById(forecastSeedDto.getCity()));

                    sb
                            .append(isValid ? String.format("Successfully import forecast %s - %.2f",
                                    forecastSeedDto.getDayOfWeek(), forecastSeedDto.getMaxTemperature())
                                    : "Invalid forecast")
                            .append(System.lineSeparator());
                    return isValid;
                })
                .map(forecastSeedDto -> {
                    ForecastEntity forecast = modelMapper.map(forecastSeedDto, ForecastEntity.class);
                    forecast.setCity(cityService.findCityById(forecastSeedDto.getCity()));
                    return forecast;
                })
                .forEach(forecastRepository::save);

        return sb.toString();
    }

    private boolean isForecastExistByDayAndCity(DayOfWeekEnum dayOfWeek, CityEntity city) {
        return forecastRepository.existsByDayOfWeekAndCity(dayOfWeek, city);
    }

    @Override
    public String exportForecasts() {
        StringBuilder sb = new StringBuilder();

        forecastRepository.findAllForecastForSunday(DayOfWeekEnum.SUNDAY, 150000)
                .forEach(forecastEntity -> {
                    sb
                            .append(String.format("""
                                    City: %s:
                                       -min temperature: %.2f
                                       --max temperature: %.2f
                                       ---sunrise: %s
                                       ----sunset: %s
                                    """,
                                    forecastEntity.getCity().getCityName(),
                                    forecastEntity.getMinTemperature(),
                                    forecastEntity.getMaxTemperature(),
                                    forecastEntity.getSunrise(),
                                    forecastEntity.getSunset()));
                });
        return sb.toString();
    }
}
