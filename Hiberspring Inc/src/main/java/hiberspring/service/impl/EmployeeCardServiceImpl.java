package hiberspring.service.impl;

import com.google.gson.Gson;
import hiberspring.domain.dtos.EmployeeCardDto;
import hiberspring.domain.entities.EmployeeCardEntity;
import hiberspring.repository.EmployeeCardRepository;
import hiberspring.service.EmployeeCardService;
import hiberspring.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Service
public class EmployeeCardServiceImpl implements EmployeeCardService {
    private static final String EMPLOYEE_CARDS_FILE_PATH = "src/main/resources/files/employee-cards.json";
    private final EmployeeCardRepository employeeCardRepository;
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final ValidationUtil validationUtil;

    public EmployeeCardServiceImpl(EmployeeCardRepository employeeCardRepository, ModelMapper modelMapper, Gson gson, ValidationUtil validationUtil) {
        this.employeeCardRepository = employeeCardRepository;
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.validationUtil = validationUtil;
    }


    @Override
    public boolean employeeCardsAreImported() {
        return employeeCardRepository.count() > 0;
    }

    @Override
    public String readEmployeeCardsJsonFile() throws IOException {
        return Files
                .readString(Path.of(EMPLOYEE_CARDS_FILE_PATH));
    }

    @Override
    public String importEmployeeCards() throws IOException {
        StringBuilder sb = new StringBuilder();

        Arrays.stream(gson.fromJson(readEmployeeCardsJsonFile(), EmployeeCardDto[].class))
                .filter(employeeCardDto -> {
                    boolean isValid = validationUtil.isValid(employeeCardDto)
                            && !isEmployeeCardEntityExistsByNumber(employeeCardDto.getNumber());

                    sb
                            .append(isValid ? String.format("Successfully imported Employee Card %s.",
                                    employeeCardDto.getNumber())
                                    : "Error: Invalid data.")
                            .append(System.lineSeparator());
                    return isValid;
                })
                .map(employeeCardDto -> modelMapper.map(employeeCardDto, EmployeeCardEntity.class))
                .forEach(employeeCardRepository::save);
        return sb.toString();
    }

    @Override
    public boolean isEmployeeCardEntityExistsByNumber(String number) {
        return employeeCardRepository.existsByNumber(number);
    }

    @Override
    public EmployeeCardEntity findEmployeeCardByNumber(String number) {
        return employeeCardRepository.findByNumber(number)
                .orElse(null);
    }
}
