package com.example.football.service;


import com.example.football.models.entity.TownEntity;

import java.io.IOException;

//ToDo - Implement all methods
public interface TownService {

    boolean areImported();

    String readTownsFileContent() throws IOException;
	
	String importTowns() throws IOException;

    TownEntity findTownByName(String townName);
}
