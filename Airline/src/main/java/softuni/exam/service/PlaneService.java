package softuni.exam.service;


import softuni.exam.models.entity.PlaneEntity;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface PlaneService {

    boolean areImported();

    String readPlanesFileContent() throws IOException;
	
	String importPlanes() throws JAXBException, FileNotFoundException;

    boolean isEntityExistByRegisterNumber(String registerNumber);

    PlaneEntity findPlaneByRegisterNumber(String registerNumber);
}
