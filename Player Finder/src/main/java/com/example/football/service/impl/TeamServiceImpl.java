package com.example.football.service.impl;

import com.example.football.models.dto.TeamSeedDto;
import com.example.football.models.entity.TeamEntity;
import com.example.football.repository.TeamRepository;
import com.example.football.service.TeamService;
import com.example.football.service.TownService;
import com.example.football.util.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Service
public class TeamServiceImpl implements TeamService {
    private static final String TEAMS_FILE_PATH = "src/main/resources/files/json/teams.json";
    private final TeamRepository teamRepository;
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final ValidationUtil validationUtil;
    private final TownService townService;


    public TeamServiceImpl(TeamRepository teamRepository, ModelMapper modelMapper, Gson gson, ValidationUtil validationUtil, TownService townService) {
        this.teamRepository = teamRepository;
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.validationUtil = validationUtil;

        this.townService = townService;
    }

    @Override
    public boolean areImported() {
        return teamRepository.count() > 0;
    }

    @Override
    public String readTeamsFileContent() throws IOException {
        return Files
                .readString(Path.of(TEAMS_FILE_PATH));
    }

    @Override
    public String importTeams() throws IOException {
        StringBuilder sb = new StringBuilder();

        Arrays.stream(gson.fromJson(readTeamsFileContent(), TeamSeedDto[].class))
                .filter(teamSeedDto -> {
                    boolean isValid = validationUtil.isValid(teamSeedDto)
                            && !isTeamEntityExistsByName(teamSeedDto.getName());

                    sb
                            .append(isValid ? String.format("Successfully imported Team %s - %d",
                                    teamSeedDto.getName(), teamSeedDto.getFanBase())
                                    : "Invalid Team")
                            .append(System.lineSeparator());
                    return isValid;
                })
                .map(teamSeedDto -> {
                    TeamEntity team = modelMapper.map(teamSeedDto, TeamEntity.class);
                    team.setTown(townService.findTownByName(teamSeedDto.getTownName()));
                    return team;
                })
                .forEach(teamRepository::save);
        return sb.toString();
    }

    @Override
    public TeamEntity findTeamByName(String name) {
        return teamRepository.findByName(name)
                .orElse(null);
    }

    private boolean isTeamEntityExistsByName(String name) {
        return teamRepository.existsByName(name);
    }
}
