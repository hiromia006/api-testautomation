package rest.api.test.community;

import com.thedeanda.lorem.LoremIpsum;
import org.testng.annotations.Test;
import rest.api.test.BaseApiTest;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.path.json.JsonPath.with;
import static org.hamcrest.Matchers.*;

public class CommunityRestApiTest extends BaseApiTest {

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
    public void postACommentAnIdea() {
        String response = given()
                .spec(specForCommunity())
                .when()
                .get("/ideas")
                .asString();
        int ideaId = with(response).getInt("[0].id");
        int campaignId = with(response).getInt("[0].campaignId");

        String text = LoremIpsum.getInstance().getParagraphs(1, 1);
        Map<String, Object> json = new HashMap<>();
        json.put("text", text);

        given()
                .spec(specForCommunity())
                .when()
                .body(json)
                .post("/ideas/" + ideaId + "/comment")
                .then()
                .assertThat()
                .spec(responseSpec())
                .body("text", equalTo(text))
                .body("parentId", equalTo(ideaId))
                .body("campaignId", equalTo(campaignId));
    }

    @Test
    public void getCommentsShouldSucceed() {
        given()
                .spec(specForCommunity())
                .when()
                .get("/comments")
                .then()
                .spec(responseSpec())
                .body("size()", greaterThan(1));
    }

    @Test
    public void getCommentAllShouldSucceed() {
        given()
                .spec(specForCommunity())
                .when()
                .get("/comments/all")
                .then()
                .spec(responseSpec())
                .body("size()", greaterThan(1));
    }
}
