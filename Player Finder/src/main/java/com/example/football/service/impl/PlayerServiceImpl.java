package com.example.football.service.impl;

import com.example.football.models.dto.PlayerSeedRootDto;
import com.example.football.models.entity.PlayerEntity;
import com.example.football.repository.PlayerRepository;
import com.example.football.service.PlayerService;
import com.example.football.service.StatService;
import com.example.football.service.TeamService;
import com.example.football.service.TownService;
import com.example.football.util.ValidationUtil;
import com.example.football.util.XmlParser;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

@Service
public class PlayerServiceImpl implements PlayerService {
    private static final String PLAYERS_FILE_PATH = "src/main/resources/files/xml/players.xml";
    private final PlayerRepository playerRepository;
    private final ModelMapper modelMapper;
    private final XmlParser xmlParser;
    private final ValidationUtil validationUtil;
    private final TownService townService;
    private final StatService statService;
    private final TeamService teamService;

    public PlayerServiceImpl(PlayerRepository playerRepository, ModelMapper modelMapper, XmlParser xmlParser, ValidationUtil validationUtil, TownService townService, StatService statService, TeamService teamService) {
        this.playerRepository = playerRepository;
        this.modelMapper = modelMapper;
        this.xmlParser = xmlParser;
        this.validationUtil = validationUtil;
        this.townService = townService;
        this.statService = statService;
        this.teamService = teamService;
    }


    @Override
    public boolean areImported() {
        return playerRepository.count() > 0;
    }

    @Override
    public String readPlayersFileContent() throws IOException {
        return Files
                .readString(Path.of(PLAYERS_FILE_PATH));
    }

    @Override
    public String importPlayers() throws JAXBException, FileNotFoundException {
        StringBuilder sb = new StringBuilder();

        xmlParser.fromFile(PLAYERS_FILE_PATH, PlayerSeedRootDto.class)
                .getPlayers()
                .stream()
                .filter(playerSeedDto -> {
                    boolean isValid = validationUtil.isValid(playerSeedDto)
                            && !isPlayerEntityExistsByEmail(playerSeedDto.getEmail());

                    sb
                            .append(isValid ? String.format("Successfully imported Player %s %s - %s",
                                    playerSeedDto.getFirstName(), playerSeedDto.getLastName(),
                                    playerSeedDto.getPosition())
                                    : "Invalid Player")
                            .append(System.lineSeparator());
                    return isValid;
                })
                .map(playerSeedDto -> {
                    PlayerEntity player = modelMapper.map(playerSeedDto, PlayerEntity.class);
                    player.setTown(townService.findTownByName(playerSeedDto.getTown().getName()));
                    player.setTeam(teamService.findTeamByName(playerSeedDto.getTeam().getName()));
                    player.setStat(statService.findByStatId(playerSeedDto.getStat().getId()));

                    return player;
                })
                .forEach(playerRepository::save);

        return sb.toString();
    }

    private boolean isPlayerEntityExistsByEmail(String email) {
        return playerRepository.existsByEmail(email);
    }

    @Override
    public String exportBestPlayers() {
        StringBuilder sb = new StringBuilder();

        playerRepository.bestPlayersByStats(LocalDate.of(1995, 1, 1), LocalDate.of(2003, 1, 1))
                .forEach(playerEntity -> {
                    sb
                            .append(String.format("""
                                    "Player - %s %s
                                    \tPosition - %s
                                    \tTeam - %s
                                    \tStadium - %s
                                    """,
                                    playerEntity.getFirstName(), playerEntity.getLastName(),
                                    playerEntity.getPosition(), playerEntity.getTeam().getName(),
                                    playerEntity.getTeam().getStadiumName()));

                });
        return sb.toString();
    }
}
