package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.PassengerSeedDto;
import softuni.exam.models.entity.PassengerEntity;
import softuni.exam.repository.PassengerRepository;
import softuni.exam.service.PassengerService;
import softuni.exam.service.TownService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Service
public class PassengerServiceImpl implements PassengerService {
    private static final String PASSENGERS_FILE_PATH = "src/main/resources/files/json/passengers.json";
    private final PassengerRepository passengerRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final Gson gson;
    private final TownService townService;

    public PassengerServiceImpl(PassengerRepository passengerRepository, ModelMapper modelMapper, ValidationUtil validationUtil, Gson gson, TownService townService) {
        this.passengerRepository = passengerRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gson = gson;
        this.townService = townService;
    }

    @Override
    public boolean areImported() {
        return passengerRepository.count() > 0;
    }

    @Override
    public String readPassengersFileContent() throws IOException {
        return Files
                .readString(Path.of(PASSENGERS_FILE_PATH));
    }

    @Override
    public String importPassengers() throws IOException {
        StringBuilder sb = new StringBuilder();

        Arrays.stream(gson.fromJson(readPassengersFileContent(), PassengerSeedDto[].class))
                .filter(passengerSeedDto -> {
                    boolean isValid = validationUtil.isValid(passengerSeedDto)
                            && !isEntityExistByEmail(passengerSeedDto.getEmail())
                            && townService.isEntityExistsByTownName(passengerSeedDto.getTown());

                    sb
                            .append(isValid ? String.format("Successfully imported Passenger %s - %s",
                                    passengerSeedDto.getLastName(), passengerSeedDto.getEmail())
                                    : "Invalid Passenger")
                            .append(System.lineSeparator());
                    return isValid;
                })
                .map(passengerSeedDto -> {
                    PassengerEntity passenger = modelMapper.map(passengerSeedDto, PassengerEntity.class);
                    passenger.setTown(townService.findTownByName(passengerSeedDto.getTown()));
                    return passenger;
                })
                .forEach(passengerRepository::save);
        return sb.toString();
    }

    @Override
    public boolean isEntityExistByEmail(String email) {
        return passengerRepository.existsByEmail(email);
    }

    @Override
    public String getPassengersOrderByTicketsCountDescendingThenByEmail() {
        StringBuilder sb = new StringBuilder();

        passengerRepository.OrderAllPassengersByTicketsCountDescThenByEmail()
                .forEach(passengerEntity -> {
                    sb
                            .append(String.format("Passenger %s  %s\n" +
                                    "\tEmail - %s\n" +
                                    "\tPhone - %s\n" +
                                    "\tNumber of tickets - %d\n",
                                    passengerEntity.getFirstName(), passengerEntity.getLastName(),
                                    passengerEntity.getEmail(), passengerEntity.getPhoneNumber(),
                                    passengerEntity.getTickets().size()))
                            .append(System.lineSeparator());
                });
        return sb.toString();
    }

    @Override
    public PassengerEntity findPassengerByEmail(String email) {
        return passengerRepository.findByEmail(email)
                .orElse(null);
    }
}
