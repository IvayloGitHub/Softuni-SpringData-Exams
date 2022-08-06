package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.TicketSeedRootDto;
import softuni.exam.models.entity.TicketEntity;
import softuni.exam.repository.TicketRepository;
import softuni.exam.service.PassengerService;
import softuni.exam.service.PlaneService;
import softuni.exam.service.TicketService;
import softuni.exam.service.TownService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class TicketServiceImpl implements TicketService {
    private static final String TICKETS_FILE_PATH = "src/main/resources/files/xml/tickets.xml";
    private final TicketRepository ticketRepository;
    private final XmlParser xmlParser;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final TownService townService;
    private final PassengerService passengerService;
    private final PlaneService planeService;

    public TicketServiceImpl(TicketRepository ticketRepository, XmlParser xmlParser, ModelMapper modelMapper, ValidationUtil validationUtil, TownService townService, PassengerService passengerService, PlaneService planeService) {
        this.ticketRepository = ticketRepository;
        this.xmlParser = xmlParser;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.townService = townService;
        this.passengerService = passengerService;
        this.planeService = planeService;
    }


    @Override
    public boolean areImported() {
        return ticketRepository.count() > 0;
    }

    @Override
    public String readTicketsFileContent() throws IOException {
        return Files
                .readString(Path.of(TICKETS_FILE_PATH));
    }

    @Override
    public String importTickets() throws JAXBException, FileNotFoundException {
        StringBuilder sb = new StringBuilder();

        xmlParser
                .fromFile(TICKETS_FILE_PATH, TicketSeedRootDto.class)
                .getTickets()
                .stream()
                .filter(ticketSeedDto -> {
                    boolean isValid = validationUtil.isValid(ticketSeedDto)
                            && !isEntityExistsBySerialNumber(ticketSeedDto.getSerialNumber())
                            && townService.isEntityExistsByTownName(ticketSeedDto.getFromTown().getName())
                            && townService.isEntityExistsByTownName(ticketSeedDto.getToTown().getName())
                            && passengerService.isEntityExistByEmail(ticketSeedDto.getPassenger().getEmail())
                            && planeService.isEntityExistByRegisterNumber(ticketSeedDto.getPlane().getRegisterNumber());

                    sb
                            .append(isValid ? String.format("Successfully imported Ticket %s - %s",
                                    ticketSeedDto.getFromTown().getName(), ticketSeedDto.getToTown().getName())
                                    : "Invalid Ticket")
                            .append(System.lineSeparator());
                    return isValid;
                })
                .map(ticketSeedDto -> {
                    TicketEntity ticket = modelMapper.map(ticketSeedDto, TicketEntity.class);
                    ticket.setFromTown(townService.findTownByName(ticketSeedDto.getFromTown().getName()));
                    ticket.setPassenger(passengerService.findPassengerByEmail(ticketSeedDto.getPassenger().getEmail()));
                    ticket.setPlane(planeService.findPlaneByRegisterNumber(ticketSeedDto.getPlane().getRegisterNumber()));
                    ticket.setToTown(townService.findTownByName(ticketSeedDto.getToTown().getName()));
                    return ticket;
                })
                .forEach(ticketRepository::save);
        return sb.toString();
    }

    private boolean isEntityExistsBySerialNumber(String serialNumber) {
        return ticketRepository.existsBySerialNumber(serialNumber);
    }
}
