package com.devsuperior.dsmovie.controllers;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("RA")
public class ScoreControllerRATests {
	
	@Test
	public void saveScoreShouldReturnNotFoundWhenMovieIdDoesNotExist() throws Exception {		
	}
	
	@Test
	public void saveScoreShouldReturnUnprocessableEntityWhenMissingMovieId() throws Exception {
	}
	
	@Test
	public void saveScoreShouldReturnUnprocessableEntityWhenScoreIsLessThanZero() throws Exception {		
	}
}
