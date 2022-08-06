package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ApartmentSeedRootDto;
import softuni.exam.models.entity.ApartmentEntity;
import softuni.exam.models.entity.TownEntity;
import softuni.exam.repository.ApartmentRepository;
import softuni.exam.service.ApartmentService;
import softuni.exam.service.TownService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class ApartmentServiceImpl implements ApartmentService {
    private static final String APARTMENTS_FILE_PATH = "src/main/resources/files/xml/apartments.xml";
    private final ApartmentRepository apartmentRepository;
    private final ModelMapper modelMapper;
    private final XmlParser xmlParser;
    private final ValidationUtil validationUtil;
    private final TownService townService;

    public ApartmentServiceImpl(ApartmentRepository apartmentRepository, ModelMapper modelMapper, XmlParser xmlParser, ValidationUtil validationUtil, TownService townService) {
        this.apartmentRepository = apartmentRepository;
        this.modelMapper = modelMapper;
        this.xmlParser = xmlParser;
        this.validationUtil = validationUtil;
        this.townService = townService;
    }

    @Override
    public boolean areImported() {
        return apartmentRepository.count() > 0;
    }

    @Override
    public String readApartmentsFromFile() throws IOException {
        return Files
                .readString(Path.of(APARTMENTS_FILE_PATH));
    }

    @Override
    public String importApartments() throws IOException, JAXBException {
        StringBuilder sb = new StringBuilder();

        xmlParser.fromFile(APARTMENTS_FILE_PATH, ApartmentSeedRootDto.class)
                .getApartments()
                .stream()
                .filter(apartmentSeedDto -> {
                    boolean isValid = validationUtil.isValid(apartmentSeedDto)
                            && !isApartmentEntityExistsByTownAndArea(townService.findTownByName(apartmentSeedDto.getTown()), apartmentSeedDto.getArea());

                    sb
                            .append(isValid ? String.format("Successfully imported apartment %s - %.2f",
                                    apartmentSeedDto.getApartmentType(), apartmentSeedDto.getArea())
                                    : "Invalid apartment")
                            .append(System.lineSeparator());
                    return isValid;
                })
                .map(apartmentSeedDto -> {
                    ApartmentEntity apartment = modelMapper.map(apartmentSeedDto, ApartmentEntity.class);
                    apartment.setTown(townService.findTownByName(apartmentSeedDto.getTown()));
                    return apartment;
                })
                .forEach(apartmentRepository::save);
        return sb.toString();
    }

    @Override
    public ApartmentEntity findApartmentById(Long id) {
        return apartmentRepository.findById(id)
                .orElse(null);
    }

    private boolean isApartmentEntityExistsByTownAndArea(TownEntity town, Double area) {
        return apartmentRepository.existsByTownAndArea(town, area);
    }
}
