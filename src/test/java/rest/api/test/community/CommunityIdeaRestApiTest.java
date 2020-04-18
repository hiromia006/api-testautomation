package rest.api.test.community;

import com.thedeanda.lorem.LoremIpsum;
import io.restassured.path.json.JsonPath;
import org.testng.annotations.Test;
import rest.api.test.BaseApiTest;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.path.json.JsonPath.with;
import static org.hamcrest.Matchers.*;

public class CommunityIdeaRestApiTest extends BaseApiTest {

    @Test(enabled = true)
    public void getCampaignsShouldSucceed() {
        given()
                .spec(specForCommunity())
                .when()
                .get("/campaigns")
                .then()
                .spec(responseSpec())
                .body("size()", greaterThan(1));
    }

    @Test
    public void createAnIdeaShouldSucceed() {
        int campaignId = with(
                given()
                        .spec(specForCommunity())
                        .when()
                        .get("/campaigns")
                        .asString()
        ).getInt("[0].id");

        String text = LoremIpsum.getInstance().getParagraphs(1, 1);
        String title = LoremIpsum.getInstance().getTitle(5);
        Map<String, Object> jsonAsMap = new HashMap<>();
        jsonAsMap.put("text", text);
        jsonAsMap.put("title", title);
        jsonAsMap.put("campaignId", campaignId);

        given()
                .spec(specForCommunity())
                .body(jsonAsMap)
                .when()
                .post("/idea")
                .then()
                .assertThat()
                .spec(responseSpec())
                .body("text", equalToIgnoringCase(text))
                .body("title", equalToIgnoringCase(title))
                .body("campaignId", equalTo(campaignId));
    }

    @Test
    public void updateAnIdeaShouldSucceed() {
        int campaignId = with(
                given()
                        .spec(specForCommunity())
                        .when()
                        .get("/campaigns")
                        .asString()
        ).getInt("[0].id");


        String text = LoremIpsum.getInstance().getParagraphs(1, 1);
        String title = LoremIpsum.getInstance().getTitle(5);
        Map<String, Object> jsonAsMap = new HashMap<>();
        jsonAsMap.put("text", text);
        jsonAsMap.put("title", title);
        jsonAsMap.put("campaignId", campaignId);

        int ideaId = with(
                given()
                        .spec(specForCommunity())
                        .body(jsonAsMap)
                        .when()
                        .post("/idea")
                        .asString()
        ).getInt("id");
        String textUpdate = LoremIpsum.getInstance().getParagraphs(1, 1);
        String titleUpdate = LoremIpsum.getInstance().getTitle(5);
        Map<String, Object> jsonAsMapUpdate = new HashMap<>();
        jsonAsMapUpdate.put("text", textUpdate);
        jsonAsMapUpdate.put("title", titleUpdate);
        jsonAsMapUpdate.put("campaignId", campaignId);
        jsonAsMapUpdate.put("id", ideaId);

        given()
                .spec(specForCommunity())
                .body(jsonAsMapUpdate)
                .when()
                .post("/idea")
                .then()
                .assertThat()
                .spec(responseSpec())
                .body("text", equalToIgnoringCase(textUpdate))
                .body("title", equalToIgnoringCase(titleUpdate))
                .body("id", equalTo(ideaId))
                .body("campaignId", equalTo(campaignId));

        given()
                .spec(specForCommunity())
                .when()
                .delete("/idea/" + ideaId + "/delete")
                .then()
                .assertThat()
                .spec(responseSpec())
                .body("size()", greaterThanOrEqualTo(1))
                .body("id", equalTo(ideaId));
    }

    @Test
    public void getIdeasShouldSucceed() {
        given()
                .spec(specForCommunity())
                .when()
                .get("/ideas")
                .then()
                .spec(responseSpec())
                .body("size()", greaterThan(1));
    }

    @Test
    public void deleteAnIdeaShouldSucceed() {
        int ideaId = with(given()
                .spec(specForCommunity())
                .when()
                .get("/ideas")
                .asString()

        ).getInt("[0].id");

        given()
                .spec(specForCommunity())
                .when()
                .delete("/idea/" + ideaId + "/delete")
                .then()
                .assertThat()
                .spec(responseSpec())
                .body("size()", greaterThanOrEqualTo(1))
                .body("id", equalTo(ideaId));
    }

    @Test
    public void getIdeaByNumberShouldSucceed() {
        JsonPath response = with(given()
                .spec(specForCommunity())
                .when()
                .get("/ideas")
                .asString()
        );

        int ideaNumber = response.getInt("[0].ideaNumber");
        int ideaId = response.getInt("[0].id");
        int campaignId = response.getInt("[0].campaignId");
        given()
                .spec(specForCommunity())
                .when()
                .get("/idea/number/" + ideaNumber)
                .then()
                .assertThat()
                .spec(responseSpec())
                .body("size()", greaterThanOrEqualTo(1))
                .body("ideaNumber", equalTo(ideaNumber))
                .body("id", equalTo(ideaId))
                .body("campaignId", equalTo(campaignId));
    }
}
