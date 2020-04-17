package rest.api.test;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;

public abstract class BaseApiTest {
    protected static final String API_TOKEN = "42f2cfa8-4edd-46a0-9302-dbea252a9748";

    @BeforeClass
    protected void setup() {
        RestAssured.baseURI = "https://ideascale.club";
        RestAssured.port = 443;
        RestAssured.basePath = "/a/rest/v1";
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    protected RequestSpecification specForCommunity() {
        return new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .addHeader("api_token", API_TOKEN)
                .build();
    }

    protected ResponseSpecification responseSpec() {
        return new ResponseSpecBuilder().expectStatusCode(200).build();
    }

}
