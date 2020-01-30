package org.tim.boland;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.BeforeClass;
import org.junit.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class ElectionTests {

    private static RequestSpecification requestSpec;
    private static ResponseSpecification responseSpec;

    @BeforeClass
    public static void createRequestSpecification() {

        requestSpec = new RequestSpecBuilder().
                setBaseUri("https://www.googleapis.com/civicinfo/v2/elections").
                addQueryParam("key",Utils.apiKey).
                build();
    }

    @BeforeClass
    public static void createResponseSpecification() {

        responseSpec = new ResponseSpecBuilder().
                expectStatusCode(200).
                expectContentType(ContentType.JSON).
                build();
    }

    /*******************************************************
     * Send a GET request to /civicinfo/v2/elections
     * and check that the response has an election id of 2000
     ******************************************************/

    @Test
    public void requestElections_checkElectionId_expect2000() {

        given().
            spec(requestSpec).
        when().
            get().
        then().
            log().body().
            spec(responseSpec).
            assertThat().body("elections[0].'id'", equalTo("2000"));
    }

    /*******************************************************
     * Send a GET request to /civicinfo/v2/elections
     * and check that the response has HTTP status code 200
     *
     * Every test validates this, but if you want to validate
     * just the status code
     ******************************************************/

    @Test
    public void requestElections_checkStatusCode_expectHttp200() {
        given().
            spec(requestSpec).
        when().
            get().
        then().
            assertThat().
            statusCode(200);
    }

    /*******************************************************
     * Send a GET request to /civicinfo/v2/elections
     * and check that the response is in JSON format
     ******************************************************/

    @Test
    public void requestElections_checkContentType_expectApplicationJson() {
        given().
            log().all().
            spec(requestSpec).
        when().
            get().
        then().
            log().body().
            assertThat().contentType(ContentType.JSON);
    }

    /***********************************************
     * Send a GET request to /civicinfo/v2/elections and check
     * that the election name returned is equal to 'VIP Test Election'
     **********************************************/

    @Test
    public void requestElections_checkElectionName_expectVIPTestElection() {
        given().
            log().all().
            spec(requestSpec).
        when().
            get().
        then().
            log().body().
            spec(responseSpec).
            assertThat().body("elections[0].'name'", equalTo("VIP Test Election"));
    }

    /***********************************************
     * Send a GET request to /civicinfo/v2/elections and check
     * that the election day returned is equal to '2021-06-06'
     **********************************************/

    @Test
    public void requestElections_checkElectionDay_expect2021_06_06() {
        given().
            log().all().
            spec(requestSpec).
        when().
            get().
        then().
            spec(responseSpec).
            assertThat().body("elections.'electionDay'", hasItem("2021-06-06"));
    }
}
