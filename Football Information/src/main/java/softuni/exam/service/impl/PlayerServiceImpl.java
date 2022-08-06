package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.domain.dto.PlayerSeedDto;
import softuni.exam.domain.entities.PlayerEntity;
import softuni.exam.repository.PlayerRepository;
import softuni.exam.service.PictureService;
import softuni.exam.service.PlayerService;
import softuni.exam.service.TeamService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Service
public class PlayerServiceImpl implements PlayerService {
    private static final String PLAYERS_FILE_PATH = "src/main/resources/files/json/players.json";
    private final PlayerRepository playerRepository;
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final ValidationUtil validationUtil;
    private final TeamService teamService;
    private final PictureService pictureService;

    public PlayerServiceImpl(PlayerRepository playerRepository, ModelMapper modelMapper, Gson gson, ValidationUtil validationUtil, TeamService teamService, PictureService pictureService) {
        this.playerRepository = playerRepository;
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.validationUtil = validationUtil;
        this.teamService = teamService;
        this.pictureService = pictureService;
    }


    @Override
    public String importPlayers() throws IOException {
        StringBuilder sb = new StringBuilder();

        Arrays.stream(gson.fromJson(readPlayersJsonFile(), PlayerSeedDto[].class))
                .filter(playerSeedDto -> {
                    boolean isValid = validationUtil.isValid(playerSeedDto);

                    sb
                            .append(isValid ? String.format("Successfully imported player: %s %s",
                                    playerSeedDto.getFirstName(), playerSeedDto.getLastName())
                                    : "Invalid player")
                            .append(System.lineSeparator());
                    return isValid;
                })
                .map(playerSeedDto -> {
                    PlayerEntity player = modelMapper.map(playerSeedDto, PlayerEntity.class);
                    player.setTeam(teamService.findTeamByName(playerSeedDto.getTeam().getName()));
                    player.setPicture(pictureService.findPictureByUrl(playerSeedDto.getPicture().getUrl()));
                    return player;
                })
                .forEach(playerRepository::save);
        return sb.toString();
    }

    @Override
    public boolean areImported() {
        return playerRepository.count() > 0;
    }

    @Override
    public String readPlayersJsonFile() throws IOException {
        return Files
                .readString(Path.of(PLAYERS_FILE_PATH));
    }

    @Override
    public String exportPlayersWhereSalaryBiggerThan() {
        StringBuilder sb = new StringBuilder();

        playerRepository.findAllBySalaryGreaterThanOrderBySalaryDesc(BigDecimal.valueOf(100000))
                .forEach(playerEntity -> {
                    sb
                            .append(String.format("Player name: %s %s%n" +
                                    "\tNumber: %d%n" +
                                    "\tSalary: %.2f%n" +
                                    "\tTeam: %s%n",
                                    playerEntity.getFirstName(), playerEntity.getLastName(),
                                    playerEntity.getNumber(),
                                    playerEntity.getSalary(),
                                    playerEntity.getTeam().getName()));
                });
        return sb.toString();
    }

    @Override
    public String exportPlayersInATeam() {
        StringBuilder sb = new StringBuilder();
        sb.append("Team: North Hub").append(System.lineSeparator());
        playerRepository.findAllPlayerInTeamNorthHub()
                .forEach(playerEntity -> {
                    sb
                            .append(String.format("\tPlayer name: %s %s - %s%n" +
                                    "\tNumber: %d%n",
                                    playerEntity.getFirstName(), playerEntity.getLastName(),
                                    playerEntity.getPosition(), playerEntity.getNumber()));
                });
        return sb.toString();
    }
}
