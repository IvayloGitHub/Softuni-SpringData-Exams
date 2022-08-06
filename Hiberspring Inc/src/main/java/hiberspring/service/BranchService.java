package hiberspring.service;

import hiberspring.domain.entities.BranchEntity;

import java.io.IOException;

public interface BranchService {

    boolean branchesAreImported();

    String readBranchesJsonFile() throws IOException;

    String importBranches() throws IOException;

    BranchEntity findBranchByName(String name);

    boolean isBranchEntityExistsByName(String branch);
}
