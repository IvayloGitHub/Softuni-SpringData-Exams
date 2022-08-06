package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.domain.dto.TeamSeedRootDto;
import softuni.exam.domain.entities.PictureEntity;
import softuni.exam.domain.entities.TeamEntity;
import softuni.exam.repository.TeamRepository;
import softuni.exam.service.PictureService;
import softuni.exam.service.TeamService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class TeamServiceImpl implements TeamService {
    private static final String TEAMS_FILE_PATH = "src/main/resources/files/xml/teams.xml";
    private final TeamRepository teamRepository;
    private final ModelMapper modelMapper;
    private final XmlParser xmlParser;
    private final ValidationUtil validationUtil;
    private final PictureService pictureService;

    public TeamServiceImpl(TeamRepository teamRepository, ModelMapper modelMapper, XmlParser xmlParser, ValidationUtil validationUtil, PictureService pictureService) {
        this.teamRepository = teamRepository;
        this.modelMapper = modelMapper;
        this.xmlParser = xmlParser;
        this.validationUtil = validationUtil;
        this.pictureService = pictureService;
    }

    @Override
    public String importTeams() throws JAXBException, FileNotFoundException {
        StringBuilder sb = new StringBuilder();

        xmlParser.fromFile(TEAMS_FILE_PATH, TeamSeedRootDto.class)
                .getTeams()
                .stream()
                .filter(teamSeedDto -> {
                    boolean isValid = validationUtil.isValid(teamSeedDto)
                            && pictureService.isPictureEntityExistsByUrl(teamSeedDto.getPicture().getUrl());

                    sb
                            .append(isValid ? String.format("Successfully imported - %s",
                                    teamSeedDto.getName())
                                    : "Invalid team")
                            .append(System.lineSeparator());
                    return isValid;
                })
                .map(teamSeedDto -> {
                    TeamEntity team = modelMapper.map(teamSeedDto, TeamEntity.class);
                    team.setPicture(pictureService.findPictureByUrl(teamSeedDto.getPicture().getUrl()));
                    return team;
                })
                .forEach(teamRepository::save);
        return sb.toString();
    }


    @Override
    public boolean areImported() {
        return teamRepository.count() > 0;
    }

    @Override
    public String readTeamsXmlFile() throws IOException {
        return Files
                .readString(Path.of(TEAMS_FILE_PATH));
    }

    @Override
    public TeamEntity findTeamByName(String name) {
        return teamRepository.findByName(name)
                .orElse(null);
    }
}
