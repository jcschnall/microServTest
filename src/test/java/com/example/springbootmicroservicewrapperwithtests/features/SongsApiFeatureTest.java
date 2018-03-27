package com.example.springbootmicroservicewrapperwithtests.features;

import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import com.example.songsapi.repositories.SongsRepository;
import com.example.songsapi.models.Songs;
import static io.restassured.RestAssured.*;
import io.restassured.RestAssured.*;
import io.restassured.*;

import java.util.stream.Stream;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SongsApiFeatureTest {

    @Autowired
    private SongsRepository songsRepository;

    @Before
    public void setUp() {
        songsRepository.deleteAll();
    }

    @After
    public void tearDown() {
        songsRepository.deleteAll();
    }


    @Test
    public void shouldAllowFullCrudForASong() throws Exception {


        Songs firstSongs = new Songs(
                "someone",
                "Ima",
                "Person"
        );

        Songs secondSongs = new Songs(
                "someone_else",
                "Someone",
                "Else"
        );

        Stream.of(firstSongs, secondSongs)
                .forEach(Songs -> {
                    songsRepository.save(Songs);
                });


    }

    .when()
	.get("http://localhost:8080/Songss/")
    .then()
        .statusCode(is(200))
                .and().body(containsString("someone"))
                .and().body(containsString("Else"));



    // Test creating a Songs
    Songs SongsNotYetInDb = new Songs(
            "new_Songs",
            "Not",
            "Yet Created"
    );

        given()
        .contentType(JSON)
        .and().body(SongsNotYetInDb)
        .when()
        .post("http://localhost:8080/Songss")
        .then()
        .statusCode(is(200))
                .and().body(containsString("new_Songs"));

        // Test get all Songss
        when()
        .get("http://localhost:8080/Songss/")
        .then()
        .statusCode(is(200))
                .and().body(containsString("someone"))
                .and().body(containsString("Else"))
                .and().body(containsString("Yet Created"));

        // Test finding one Songs by ID
        when()
        .get("http://localhost:8080/Songss/" + secondSongs.getId())
                .then()
        .statusCode(is(200))
                .and().body(containsString("Someone"))
                .and().body(containsString("Else"));

        // Test updating a Songs
        secondSongs.setFirstName("changed_name");

        given()
        .contentType(JSON)
        .and().body(secondSongs)
        .when()
        .patch("http://localhost:8080/Songss/" + secondSongs.getId())
                .then()
        .statusCode(is(200))
                .and().body(containsString("changed_name"));

        // Test deleting a Songs
        when()
        .delete("http://localhost:8080/Songss/" + secondSongs.getId())
                .then()
        .statusCode(is(200));

}
