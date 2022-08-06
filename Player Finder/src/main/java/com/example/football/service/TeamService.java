package com.example.football.service;

import com.example.football.models.entity.TeamEntity;
import com.example.football.models.entity.TownEntity;

import java.io.IOException;

//ToDo - Implement all methods
public interface TeamService {
    boolean areImported();

    String readTeamsFileContent() throws IOException;

    String importTeams() throws IOException;

    TeamEntity findTeamByName(String name);
}
