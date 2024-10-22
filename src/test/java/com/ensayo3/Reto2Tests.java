package com.ensayo3;

import com.ensayo3.dto.responses.AstronomyPictureResponse;
import com.ensayo3.dto.responses.LandsatResponse;
import com.ensayo3.dto.responses.NEOResponse;
import io.restassured.http.ContentType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
public class Reto2Tests {

    private static final String NASA_API_KEY = "eSbA0bwPm6RXjgyWYykRUFagLadym4QOwG3pjAoW";
    private static final String NASA_BASE_URI = "https://api.nasa.gov/";

    @Test
    public void asteroidsNeoWsTest() {
        String startDate = "2024-10-01";
        String endDate = "2024-10-02";

        String uri = NASA_BASE_URI + String.format("neo/rest/v1/feed?start_date=%s&end_date=%s&api_key=%s",
                startDate, endDate, NASA_API_KEY);

        NEOResponse response = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .get(uri)
                .then()
                .statusCode(200)
                .extract().as(NEOResponse.class);

        assertNotNull(response);
        assertNotNull(response.getElement_count());

        log.info("Success asteroids call response: {}", response);
    }

    @Test
    public void getLandsatImageTest() {
        String latitude = "75.57";
        String longitude = "62.19";
        String date = "2024-10-01";
        String dim = "0.1";

        String uri = NASA_BASE_URI + String.format("planetary/earth/assets?lon=%s&lat=%s&date=%s&dim=%s&api_key=%s",
                longitude, latitude, date, dim, NASA_API_KEY);

        LandsatResponse response = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .get(uri)
                .then()
                .statusCode(200)
                .extract().as(LandsatResponse.class);

        assertNotNull(response);
        assertNotNull(response.getUrl());

        log.info("Landsat response success for image: {}", response.getUrl());
    }

    @Test
    public void getAstronomyPictureOfTheDayTest() {
        String uri = NASA_BASE_URI + String.format("planetary/apod?api_key=%s", NASA_API_KEY);

        AstronomyPictureResponse response = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .get(uri)
                .then()
                .statusCode(200)
                .extract().as(AstronomyPictureResponse.class);

        assertNotNull(response);
        assertNotNull(response.getUrl());

        log.info("Success for Astronomy picture call, visit the POD: {}", response.getUrl());
    }

}
