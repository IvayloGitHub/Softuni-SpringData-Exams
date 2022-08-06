package softuni.exam.service;

import softuni.exam.models.entity.PassengerEntity;

import java.io.IOException;

public interface PassengerService {

    boolean areImported();

    String readPassengersFileContent() throws IOException;
	
	String importPassengers() throws IOException;

    boolean isEntityExistByEmail(String email);

    String getPassengersOrderByTicketsCountDescendingThenByEmail();

    PassengerEntity findPassengerByEmail(String email);
}
