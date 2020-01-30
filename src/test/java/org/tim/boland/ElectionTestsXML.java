package org.tim.boland;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.BeforeClass;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;

public class ElectionTestsXML {

    private static RequestSpecification requestSpec;
    private static ResponseSpecification responseSpec;

    @BeforeClass
    public static void createRequestSpecification() {

        requestSpec = new RequestSpecBuilder().
                setBaseUri("https://www.googleapis.com/civicinfo/v2/elections").
                addQueryParam("key", Utils.apiKey).
                addHeader("Accept", "application/xml").
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
     * This is just a quick test to ensure that XML is
     * not returned
     ******************************************************/

    @Test
    public void requestElections_checkElectionId_expect2000() {

        given().
                log().all().
                spec(requestSpec).
                when().
                get().
                then().
                log().body().
                spec(responseSpec).
                assertThat().body("elections[0].'id'", equalTo("2000"));
    }
}