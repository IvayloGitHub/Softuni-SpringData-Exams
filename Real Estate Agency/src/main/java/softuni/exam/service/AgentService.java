package softuni.exam.service;


import softuni.exam.models.entity.AgentEntity;

import java.io.IOException;
public interface AgentService {

    boolean areImported();

    String readAgentsFromFile() throws IOException;
	
	String importAgents() throws IOException;

    boolean isAgentEntityExistsByFirstName(String firstName);

    AgentEntity findAgentByName(String name);
}
