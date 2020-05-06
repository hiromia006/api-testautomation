package rest.api.test;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;

public abstract class BaseApiTest {
    protected static final String API_TOKEN = "e9c0e2c0-748b-4195-859a-6c6e2416a219";

    @BeforeClass
    protected void setup() {
        RestAssured.baseURI = "https://ideascale.rocks";
        RestAssured.port = 443;
        RestAssured.basePath = "/a/rest/v1";
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    protected RequestSpecification specForCommunity() {
        return new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .addHeader("api_token", API_TOKEN)
                .addFilter(new AllureRestAssured())
                .build();
    }

    protected ResponseSpecification responseSpec() {
        return new ResponseSpecBuilder().expectStatusCode(200).build();
    }

}
