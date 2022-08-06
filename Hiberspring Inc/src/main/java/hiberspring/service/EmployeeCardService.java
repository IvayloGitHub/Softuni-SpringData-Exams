package hiberspring.service;

import hiberspring.domain.entities.EmployeeCardEntity;
import hiberspring.repository.EmployeeCardRepository;

import java.io.IOException;

public interface EmployeeCardService {

    boolean employeeCardsAreImported();

    String readEmployeeCardsJsonFile() throws IOException;

    String importEmployeeCards() throws IOException;

    boolean isEmployeeCardEntityExistsByNumber(String number);

    EmployeeCardEntity findEmployeeCardByNumber(String number);
}
