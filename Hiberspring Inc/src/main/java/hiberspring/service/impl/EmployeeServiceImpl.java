package hiberspring.service.impl;

import hiberspring.domain.dtos.EmployeeSeedRootDto;
import hiberspring.domain.entities.EmployeeEntity;
import hiberspring.repository.EmployeeRepository;
import hiberspring.service.BranchService;
import hiberspring.service.EmployeeCardService;
import hiberspring.service.EmployeeService;
import hiberspring.util.ValidationUtil;
import hiberspring.util.XmlParser;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private static final String EMPLOYEES_FILE_PATH = "src/main/resources/files/employees.xml";
    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;
    private final XmlParser xmlParser;
    private final ValidationUtil validationUtil;
    private final BranchService branchService;
    private final EmployeeCardService employeeCardService;


    public EmployeeServiceImpl(EmployeeRepository employeeRepository, ModelMapper modelMapper, XmlParser xmlParser, ValidationUtil validationUtil, BranchService branchService, EmployeeCardService employeeCardService) {
        this.employeeRepository = employeeRepository;
        this.modelMapper = modelMapper;
        this.xmlParser = xmlParser;
        this.validationUtil = validationUtil;
        this.branchService = branchService;
        this.employeeCardService = employeeCardService;

    }


    @Override
    public boolean employeesAreImported() {
        return employeeRepository.count() > 0;
    }

    @Override
    public String readEmployeesXmlFile() throws IOException {
        return Files
                .readString(Path.of(EMPLOYEES_FILE_PATH));
    }

    @Override
    public String importEmployees() throws JAXBException, FileNotFoundException {
        StringBuilder sb = new StringBuilder();

        xmlParser.fromFile(EMPLOYEES_FILE_PATH, EmployeeSeedRootDto.class)
                .getEmployees()
                .stream()
                .filter(employeeSeedDto -> {
                    boolean isValid = validationUtil.isValid(employeeSeedDto)
                            && employeeCardService.isEmployeeCardEntityExistsByNumber(employeeSeedDto.getCard())
                            && branchService.isBranchEntityExistsByName(employeeSeedDto.getBranch())
                            && !isEmployeeEntityExistByCardNumber(employeeSeedDto.getCard());

                    sb
                            .append(isValid ? String.format("Successfully imported Employee %s %s.",
                                    employeeSeedDto.getFirstName(), employeeSeedDto.getLastName())
                                    : "Error: Invalid data.")
                            .append(System.lineSeparator());
                    return isValid;
                })
                .map(employeeSeedDto -> {
                    EmployeeEntity employee = modelMapper.map(employeeSeedDto, EmployeeEntity.class);
                    employee.setCard(employeeCardService.findEmployeeCardByNumber(employeeSeedDto.getCard()));
                    employee.setBranch(branchService.findBranchByName(employeeSeedDto.getBranch()));
                    return employee;
                })
                .forEach(employeeRepository::save);
        return sb.toString();
    }

    private boolean isEmployeeEntityExistByCardNumber(String cardNumber) {
        return employeeRepository.existsByCard_Number(cardNumber);
    }

    @Override
    public String exportProductiveEmployees() {
        StringBuilder sb = new StringBuilder();

        employeeRepository.findAllEmployeesWhereBranchHasMoreThanOneProduct()
                .forEach(employeeEntity -> {
                    sb
                            .append(String.format("Name: %s %s%n" +
                                    "Position: %s%n" +
                                    "Card Number: %s%n" +
                                    "-------------------------%n",
                                    employeeEntity.getFirstName(), employeeEntity.getLastName(),
                                    employeeEntity.getPosition(),
                                    employeeEntity.getCard().getNumber()));
                });
        return sb.toString();
    }
}
