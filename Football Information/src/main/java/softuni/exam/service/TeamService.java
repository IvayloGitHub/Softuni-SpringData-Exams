package softuni.exam.service;

import softuni.exam.domain.entities.TeamEntity;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface TeamService {

    String importTeams() throws JAXBException, FileNotFoundException;

    boolean areImported();

    String readTeamsXmlFile() throws IOException;

    TeamEntity findTeamByName(String name);
}
