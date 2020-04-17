package rest.api.test.community;

import com.thedeanda.lorem.LoremIpsum;
import org.testng.annotations.Test;
import rest.api.test.BaseApiTest;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.path.json.JsonPath.with;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.greaterThan;

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
        Long campaignId = with(
                given()
                        .spec(specForCommunity())
                        .when()
                        .get("/campaigns")
                        .asString()).getLong("[0].id");
        System.out.println(campaignId);

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
                .body("title", equalToIgnoringCase(title));
    }

    @Test
    public void updateAnIdeaShouldSucceed() {
        Long campaignId = with(
                given()
                        .spec(specForCommunity())
                        .when()
                        .get("/campaigns")
                        .asString()).getLong("[0].id");
        System.out.println(campaignId);
        String text = LoremIpsum.getInstance().getParagraphs(1, 1);
        String title = LoremIpsum.getInstance().getTitle(5);
        Map<String, Object> jsonAsMap = new HashMap<>();
        jsonAsMap.put("text", text);
        jsonAsMap.put("title", title);
        jsonAsMap.put("campaignId", campaignId);

        Long ideaId = with(given()
                .spec(specForCommunity())
                .body(jsonAsMap)
                .when()
                .post("/idea")
                .asString())
                .getLong("id");
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
                .body("title", equalToIgnoringCase(titleUpdate));
    }
}
