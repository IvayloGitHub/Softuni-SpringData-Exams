package hiberspring.service.impl;

import com.google.gson.Gson;
import hiberspring.domain.dtos.BranchSeedDto;
import hiberspring.domain.entities.BranchEntity;
import hiberspring.repository.BranchRepository;
import hiberspring.service.BranchService;
import hiberspring.service.TownService;
import hiberspring.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Service
public class BranchServiceImpl implements BranchService {
    private static final String BRANCHES_FILE_PATH = "src/main/resources/files/branches.json";
    private final BranchRepository branchRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final Gson gson;
    private final TownService townService;

    public BranchServiceImpl(BranchRepository branchRepository, ModelMapper modelMapper, ValidationUtil validationUtil, Gson gson, TownService townService) {
        this.branchRepository = branchRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gson = gson;
        this.townService = townService;
    }


    @Override
    public boolean branchesAreImported() {
        return branchRepository.count() > 0;
    }

    @Override
    public String readBranchesJsonFile() throws IOException {
        return Files
                .readString(Path.of(BRANCHES_FILE_PATH));
    }

    @Override
    public String importBranches() throws IOException {
        StringBuilder sb = new StringBuilder();

        Arrays.stream(gson.fromJson(readBranchesJsonFile(), BranchSeedDto[].class))
                .filter(branchSeedDto -> {
                    boolean isValid = validationUtil.isValid(branchSeedDto)
                            && townService.isTownEntityExistsByName(branchSeedDto.getTown());

                    sb
                            .append(isValid ? String.format("Successfully imported Branch %s.",
                                    branchSeedDto.getName())
                                    : "Error: Invalid data.")
                            .append(System.lineSeparator());
                    return isValid;
                })
                .map(branchSeedDto -> {
                    BranchEntity branch = modelMapper.map(branchSeedDto, BranchEntity.class);
                    branch.setTown(townService.findTownByName(branchSeedDto.getTown()));
                    return branch;
                })
                .forEach(branchRepository::save);
        return sb.toString();
    }

    @Override
    public BranchEntity findBranchByName(String name) {
        return branchRepository.findByName(name)
                .orElse(null);
    }

    @Override
    public boolean isBranchEntityExistsByName(String name) {
        return branchRepository.existsByName(name);
    }
}
