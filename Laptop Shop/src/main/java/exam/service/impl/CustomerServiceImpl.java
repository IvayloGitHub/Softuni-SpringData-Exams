package exam.service.impl;

import com.google.gson.Gson;
import exam.model.entity.CustomerEntity;
import exam.model.entity.dto.CustomerSeedDto;
import exam.repository.CustomerRepository;
import exam.service.CustomerService;
import exam.service.TownService;
import exam.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Service
public class CustomerServiceImpl implements CustomerService {

    private static final String CUSTOMERS_FILE_PATH = "src/main/resources/files/json/customers.json";
    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final ValidationUtil validationUtil;
    private final TownService townService;

    public CustomerServiceImpl(CustomerRepository customerRepository, ModelMapper modelMapper, Gson gson, ValidationUtil validationUtil, TownService townService) {
        this.customerRepository = customerRepository;
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.validationUtil = validationUtil;
        this.townService = townService;
    }


    @Override
    public boolean areImported() {
        return customerRepository.count() > 0;
    }

    @Override
    public String readCustomersFileContent() throws IOException {
        return Files
                .readString(Path.of(CUSTOMERS_FILE_PATH));
    }

    @Override
    public String importCustomers() throws IOException {
        StringBuilder sb = new StringBuilder();

        Arrays.stream(gson.fromJson(readCustomersFileContent(), CustomerSeedDto[].class))
                .filter(customerSeedDto -> {
                    boolean isValid = validationUtil.isValid(customerSeedDto)
                            && !isEntityExistsByEmail(customerSeedDto.getEmail());

                    sb
                            .append(isValid ? String.format("Successfully imported Customer %s %s - %s",
                                    customerSeedDto.getFirstName(), customerSeedDto.getLastName(), customerSeedDto.getEmail())
                                    : "Invalid Customer")
                            .append(System.lineSeparator());
                    return isValid;
                })
                .map(customerSeedDto -> {
                    CustomerEntity customer = modelMapper.map(customerSeedDto, CustomerEntity.class);
                    customer.setTown(townService.findTownByName(customerSeedDto.getTown().getName()));
                    return customer;
                })
                .forEach(customerRepository::save);
        return sb.toString();
    }

    private boolean isEntityExistsByEmail(String email) {
        return customerRepository.existsByEmail(email);
    }
}
