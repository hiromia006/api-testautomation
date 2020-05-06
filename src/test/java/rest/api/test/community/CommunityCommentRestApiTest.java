package rest.api.test.community;

import com.thedeanda.lorem.LoremIpsum;
import org.testng.annotations.Test;
import rest.api.test.BaseApiTest;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.path.json.JsonPath.with;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

public class CommunityCommentRestApiTest extends BaseApiTest {
    @Test
    public void postCommentAnIdea() {
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

    @Test(enabled = false)
    public void getCommentsShouldSucceed() {
        given()
                .spec(specForCommunity())
                .when()
                .get("/comments")
                .then()
                .spec(responseSpec())
                .body("size()", greaterThan(0));
    }

    @Test
    public void getCommentAllShouldSucceed() {
        given()
                .spec(specForCommunity())
                .when()
                .get("/comments/all")
                .then()
                .spec(responseSpec())
                .body("size()", greaterThan(0));
    }

}
