package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.OfferSeedRootDto;
import softuni.exam.models.entity.OfferEntity;
import softuni.exam.repository.OfferRepository;
import softuni.exam.service.AgentService;
import softuni.exam.service.ApartmentService;
import softuni.exam.service.OfferService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class OfferServiceImpl implements OfferService {
    private static final String OFFERS_FILE_PATH = "src/main/resources/files/xml/offers.xml";
    private final OfferRepository offerRepository;
    private final ModelMapper modelMapper;
    private final XmlParser xmlParser;
    private final ValidationUtil validationUtil;
    private final AgentService agentService;
    private final ApartmentService apartmentService;


    public OfferServiceImpl(OfferRepository offerRepository, ModelMapper modelMapper, XmlParser xmlParser, ValidationUtil validationUtil, AgentService agentService, ApartmentService apartmentService) {
        this.offerRepository = offerRepository;
        this.modelMapper = modelMapper;
        this.xmlParser = xmlParser;
        this.validationUtil = validationUtil;
        this.agentService = agentService;
        this.apartmentService = apartmentService;
    }

    @Override
    public boolean areImported() {
        return offerRepository.count() > 0;
    }

    @Override
    public String readOffersFileContent() throws IOException {
        return Files
                .readString(Path.of(OFFERS_FILE_PATH));
    }

    @Override
    public String importOffers() throws IOException, JAXBException {
        StringBuilder sb = new StringBuilder();

        xmlParser.fromFile(OFFERS_FILE_PATH, OfferSeedRootDto.class)
                .getOffers()
                .stream()
                .filter(offerSeedDto -> {
                    boolean isValid = validationUtil.isValid(offerSeedDto)
                            && agentService.isAgentEntityExistsByFirstName(offerSeedDto.getAgent().getName());

                    sb
                            .append(isValid ? String.format("Successfully imported offer %.2f",
                                    offerSeedDto.getPrice())
                                    : "Invalid offer")
                            .append(System.lineSeparator());
                    return isValid;
                })
                .map(offerSeedDto -> {
                    OfferEntity offer = modelMapper.map(offerSeedDto, OfferEntity.class);
                    offer.setAgent(agentService.findAgentByName(offerSeedDto.getAgent().getName()));
                    offer.setApartment(apartmentService.findApartmentById(offerSeedDto.getApartment().getId()));

                    return offer;
                })
                .forEach(offerRepository::save);
        return sb.toString();
    }

    @Override
    public String exportOffers() {
        StringBuilder sb = new StringBuilder();

        offerRepository.findBestOffers()
                .forEach(offerEntity -> {
                    sb
                            .append(String.format("""
                                    Agent %s %s with offer №%d:
                                      -Apartment area: %.2f
                                      --Town: %s
                                      ---Price: %.2f$
                                    """,
                                    offerEntity.getAgent().getFirstName(), offerEntity.getAgent().getLastName(),
                                    offerEntity.getId(), offerEntity.getApartment().getArea(),
                                    offerEntity.getApartment().getTown().getTownName(),
                                    offerEntity.getPrice()));
                });
        return sb.toString();
    }
}
