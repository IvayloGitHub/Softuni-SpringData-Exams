package hiberspring.service;

import hiberspring.domain.entities.TownEntity;

import java.io.IOException;

public interface TownService {

    boolean townsAreImported();

    String readTownsJsonFile() throws IOException;

    String importTowns() throws IOException;

    TownEntity findTownByName(String town);

    boolean isTownEntityExistsByName(String town);
}
