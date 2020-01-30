package org.tim.boland;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@RunWith(DataProviderRunner.class)
public class VoterInfoTests {

    private static RequestSpecification requestSpec;
    private static ResponseSpecification response200Spec;
    private static ResponseSpecification response400Spec;

    @BeforeClass
    public static void createRequestSpecification() {

        requestSpec = new RequestSpecBuilder().
                setBaseUri("https://www.googleapis.com/civicinfo/v2/voterinfo").
                addQueryParam("key",Utils.apiKey).
                build();
    }

    @BeforeClass
    public static void createResponseSpecification() {

        response200Spec = new ResponseSpecBuilder().
                expectStatusCode(200).
                expectContentType(ContentType.JSON).
                build();

        response400Spec = new ResponseSpecBuilder().
                expectStatusCode(400).
                expectContentType(ContentType.JSON).
                build();
    }


    /*******************************************************
     * Create a DataProvider with two test data rows:
     * address                              - election_id   - first candidate in list
     * 29245 Dakota Dr. Valencia CA         - 2000          - Tony Strickland
     * 1263 Pacific Ave. Kansas City KS     - 2000          - Greg Orman
     ******************************************************/

    @DataProvider
    public static Object[][] addressesAndElectionIds() {
        return new Object[][]{
                {"29245 Dakota Dr. Valencia CA ", "2000", "Tony Strickland"},
                {"1263 Pacific Ave. Kansas City KS", "2000", "Greg Orman"}
        };
    }

    /*******************************************************
     * Create a DataProvider with two test data rows:
     * address                              - election_id   - the number of contests for the given address and election id
     * 29245 Dakota Dr. Valencia CA         - 2000          - 19
     * 1263 Pacific Ave. Kansas City KS     - 2000          - 18
     ******************************************************/

    @DataProvider
    public static Object[][] numberOfContests() {
        return new Object[][]{
                {"29245 Dakota Dr. Valencia CA ", "2000", 19},
                {"1263 Pacific Ave. Kansas City KS", "2000", 18}
        };
    }

    /*******************************************************************
     * Request VoterInfo data and validate the first candidate listed
     * for each address
     ********************************************************************/

    @Test
    @UseDataProvider("addressesAndElectionIds")
    public void requestVoterInfo_checkFirstCandidateName_expectSpecifiedCandidate(String address, String electionId, String expectedCandidate) {
        given().
                log().all().
                spec(requestSpec).
            queryParam("address", address).
            queryParam("electionId", electionId).
        when().
            get().
        then().
            log().body().
            spec(response200Spec).
            assertThat().body("contests[0].candidates[0].'name'", equalTo(expectedCandidate));
    }

    /*******************************************************************
     * Request VoterInfo data and validate the number of contests
     * and election name for the given addresses
     ********************************************************************/

    @Test
    @UseDataProvider("numberOfContests")
    public void requestVoterInfo_validateNumberOfContests_expectSpecifiedCount(String address, String electionId, int numberOfContests) {

        given().
            spec(requestSpec).
            queryParam("address", address).
            queryParam("electionId", electionId).
        when().
            get().
        then().
            log().body().
            spec(response200Spec).
            assertThat().body("$", hasKey("normalizedInput")).
            assertThat().body("election.name", equalTo("VIP Test Election")).
            assertThat().body("contests.size()", is(numberOfContests));
    }

    /*****************************************************************************
     * Request VoterInfo data without the optional Election Id and validate 400
     * is returned
     *
     */

    @Test
    @UseDataProvider("numberOfContests")
    public void requestVoterInfo_withoutElectionId_validateNumberOfContests_expectSpecifiedCount(String address, String electionId, int numberOfContests) {

        given().
            log().all().
            spec(requestSpec).
                queryParam("address", address).
            when().
                get().
            then().
                log().body().
                spec(response400Spec).
                assertThat().body("error.message", equalTo("Election unknown"));
                //assertThat().body("elections.'electionDay'", hasItem("2021-06-06"));

    }


    /******************************************************************************
     * Request VoterInfo but only data from official state sources will be returned.
     * Verify officialOnly is true across the board.
     *
     * I think the Dakota Dr. address is failing due to a bug.  I think all address responses should
     * still have the contests[] section
     */
    @Test
    @UseDataProvider("numberOfContests")
    public void requestVoterInfo_withOfficialOnly_(String address, String electionId, int numberOfContests) {
        Response response =
        given().
            log().all().
            spec(requestSpec).
            queryParam("address", address).
            queryParam("electionId", electionId).
            queryParam("officialOnly", true).
        when().
            get().
        then().
            log().body().extract().response();

        JSONObject responseJson = new JSONObject(response.getBody().asString());
        JSONArray contests = responseJson.optJSONArray("contests");
        if (contests != null) {
            for (int i = 0; i < contests.length(); i++) {
                JSONObject objAtIndex = contests.optJSONObject(i);
                if (objAtIndex != null) {
                    JSONArray sources = objAtIndex.optJSONArray("sources");
                        if (sources != null){
                            for (int j = 0; j < sources.length(); j++) {
                                JSONObject objAtIndexJ = sources.getJSONObject(i);
                                    if (objAtIndexJ != null) {
                                        boolean official = objAtIndexJ.optBoolean("official");
                                        Assert.assertTrue(official);
                                    }
                            }
                        }
                    }
                }
            }
    }
}


