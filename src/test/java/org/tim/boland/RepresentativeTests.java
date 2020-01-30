package org.tim.boland;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.Assert;
import org.tim.boland.pojos.representative.Office;
import org.tim.boland.pojos.representative.Official;
import org.tim.boland.pojos.representative.RepresentativeCA;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static io.restassured.RestAssured.form;
import static io.restassured.RestAssured.given;

@RunWith(DataProviderRunner.class)
public class RepresentativeTests {

    private static RequestSpecification requestSpec;
    private static ResponseSpecification responseSpec;

    @BeforeClass
    public static void createRequestSpecification() {

        requestSpec = new RequestSpecBuilder().
                setBaseUri("https://www.googleapis.com/civicinfo/v2/representatives").
                addQueryParam("key", Utils.apiKey).
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
     * Create a DataProvider with two test data rows:
     * address
     * 29245 Dakota Dr. Valencia CA
     * 1263 Pacific Ave. Kansas City KS
     ******************************************************/

    @DataProvider
    public static Object[][] address() {
        return new Object[][]{
                {"29245 Dakota Dr. Valencia CA"},
        };
    }


    /*******************************************************************
     * Request Representative Info data and validate the first candidate listed
     * for each address
     ********************************************************************/
    @Test
    @UseDataProvider("address")
    public void representativeInfoByAddress_minimum_requirements(String address) {
        Response response = given().
                    log().all().
                    spec(requestSpec).
                    queryParam("address", address).
                when().
                    get().
                then().
                    log().body().
                    spec(responseSpec).
                    extract().response();

        RepresentativeCA representativeCA = response.as(RepresentativeCA.class, ObjectMapperType.GSON);

        List<Office> offices = representativeCA.getOffices();
            for (Office office : offices) {
                Assert.assertFalse(office.getName() + " Division ID is blank", office.getDivisionId().equalsIgnoreCase(""));
                Assert.assertFalse( "Name is Blank", office.getName().equalsIgnoreCase(""));
            }

        List<Official> officials = representativeCA.getOfficials();
            for (Official official : officials) {
                List<String> emails = official.getEmails();
                    if (emails != null) {
                        String email = emails.get(0);
                        Assert.assertTrue(email.matches("^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$"));
            }
        }
    }
}
