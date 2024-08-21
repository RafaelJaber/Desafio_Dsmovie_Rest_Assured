package com.devsuperior.dsmovie.controllers;

import com.devsuperior.dsmovie.tests.TokenUtil;
import io.restassured.http.ContentType;
import net.datafaker.Faker;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.hasItem;

@Tag("RA")
public class ScoreControllerRATests {

	Faker faker = new Faker();

	private Long existingMovieId, nonExistingMovieId;
	private String movieTitle;
	private String clientUsername, clientPassword, adminUsername, adminPassword;
	private String clientToken, adminToken, invalidToken;

	private Map<String, Object> postScoreInstance;

	@BeforeEach
	public void setUp() throws JSONException {
		baseURI = "http://localhost:8080";
		existingMovieId = 1L;
		nonExistingMovieId = 999L;

		movieTitle = "The Witcher";
		clientUsername = "alex@gmail.com";
		clientPassword = "123456";
		adminUsername = "maria@gmail.com";
		adminPassword = "123456";

		clientToken = TokenUtil.obtainAccessToken(clientUsername, clientPassword);
		adminToken = TokenUtil.obtainAccessToken(adminUsername, adminPassword);
		invalidToken = adminToken + "xpto";

		postScoreInstance = new HashMap<>();
		postScoreInstance.put("movieId", existingMovieId);
		postScoreInstance.put("score", faker.number().randomDouble(1, 0, 5));
	}

	@Test
	public void saveScoreShouldReturnNotFoundWhenMovieIdDoesNotExist() throws Exception {
		postScoreInstance.put("movieId", nonExistingMovieId);
		JSONObject newScore = new JSONObject(postScoreInstance);
		given()
			.header("Content-Type", "application/json")
			.header("Authorization", "Bearer " + adminToken)
			.body(newScore)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.put("/scores")
		.then()
			.statusCode(404)
			.body("error", is("Recurso não encontrado"));
	}
	
	@Test
	public void saveScoreShouldReturnUnprocessableEntityWhenMissingMovieId() throws Exception {
		postScoreInstance.put("movieId", "");
		JSONObject newScore = new JSONObject(postScoreInstance);
		given()
			.header("Content-Type", "application/json")
			.header("Authorization", "Bearer " + adminToken)
			.body(newScore)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.put("/scores")
		.then()
			.statusCode(422)
			.body("errors.fieldName", hasItem("movieId"))
			.body("errors.message", hasItem("Campo requerido"));
	}
	
	@Test
	public void saveScoreShouldReturnUnprocessableEntityWhenScoreIsLessThanZero() throws Exception {
		postScoreInstance.put("score", -1);
		JSONObject newScore = new JSONObject(postScoreInstance);
		given()
			.header("Content-Type", "application/json")
			.header("Authorization", "Bearer " + adminToken)
			.body(newScore)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.put("/scores")
		.then()
			.statusCode(422)
			.body("errors.fieldName", hasItem("score"))
			.body("errors.message", hasItem("Valor mínimo 0"));
	}
}
