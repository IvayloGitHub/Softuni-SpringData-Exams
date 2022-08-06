package softuni.exam.service;


import softuni.exam.models.entity.TownEntity;

import java.io.IOException;

public interface TownService {

    boolean areImported();

    String readTownsFileContent() throws IOException;
	
	String importTowns() throws IOException;

    TownEntity findTownByName(String town);

    boolean isEntityExistsByTownName(String name);
}
