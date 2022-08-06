package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.AgentSeedDto;
import softuni.exam.models.entity.AgentEntity;
import softuni.exam.repository.AgentRepository;
import softuni.exam.service.AgentService;
import softuni.exam.service.TownService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Service
public class AgentServiceImpl implements AgentService {
    private static final String AGENTS_FILE_PATH = "src/main/resources/files/json/agents.json";
    private final AgentRepository agentRepository;
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final ValidationUtil validationUtil;
    private final TownService townService;

    public AgentServiceImpl(AgentRepository agentRepository, ModelMapper modelMapper, Gson gson, ValidationUtil validationUtil, TownService townService) {
        this.agentRepository = agentRepository;
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.validationUtil = validationUtil;
        this.townService = townService;
    }

    @Override
    public boolean areImported() {
        return agentRepository.count() > 0;
    }

    @Override
    public String readAgentsFromFile() throws IOException {
        return Files
                .readString(Path.of(AGENTS_FILE_PATH));
    }

    @Override
    public String importAgents() throws IOException {
        StringBuilder sb = new StringBuilder();

        Arrays.stream(gson.fromJson(readAgentsFromFile(), AgentSeedDto[].class))
                .filter(agentSeedDto -> {
                    boolean isValid = validationUtil.isValid(agentSeedDto)
                            && !isAgentEntityExistsByFirstName(agentSeedDto.getFirstName());

                    sb
                            .append(isValid ? String.format("Successfully imported agent - %s %s",
                                    agentSeedDto.getFirstName(), agentSeedDto.getLastName())
                                    : "Invalid agent")
                            .append(System.lineSeparator());
                    return isValid;
                })
                .map(agentSeedDto -> {
                    AgentEntity agent = modelMapper.map(agentSeedDto, AgentEntity.class);
                    agent.setTown(townService.findTownByName(agentSeedDto.getTown()));
                    return agent;
                })
                .forEach(agentRepository::save);
        return sb.toString();
    }



    @Override
    public boolean isAgentEntityExistsByFirstName(String firstName) {
        return agentRepository.existsByFirstName(firstName);
    }

    @Override
    public AgentEntity findAgentByName(String name) {
        return agentRepository.findByFirstName(name)
                .orElse(null);
    }
}
