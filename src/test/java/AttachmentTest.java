import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

import static io.restassured.RestAssured.given;

public class AttachmentTest {
    static String response = "";
    static String key = "";
    static SessionFilter sessionFilter = null;
    static JsonPath jsonPath = null;

    @BeforeSuite
    public void login() throws IOException {
        RestAssured.baseURI = "http://localhost:8080";
        sessionFilter = new SessionFilter();
        given()
                .header("Content-Type", "application/json")
                .body(DataFiles.getAuthentication())
                .filter(sessionFilter)
                .when()
                .post("/rest/auth/1/session")
                .then()
                .assertThat()
                .statusCode(200);
    }

    @BeforeTest
    public static void createIssue() throws IOException {
        response =
                given()
                        .header("Content-Type", "application/json")
                        .header("X-Atlassian-Token","no-check")
                        .body(DataFiles.getIssue())
                        .filter(sessionFilter)
                        .when()
                        .post("/rest/api/2/issue")
                        .then()
                        .assertThat()
                        .statusCode(201)
                        .extract()
                        .response()
                        .body()
                        .asString();

        jsonPath = new JsonPath(response);
        key = jsonPath.getString("key");
    }

    @Test
    public void addAttachment(){
        given()
                .header("X-Atlassian-Token","no-check")
                .pathParam("issueIdOrKey", key)
                .multiPart(new File("src/main/resources/scenery.jpeg"))
                .filter(sessionFilter)
                .when()
                .post("/rest/api/2/issue/{issueIdOrKey}/attachments")
                .then()
                .assertThat()
                .statusCode(200);
    }

    @AfterTest
    public static void tearDown(){
        given()
                .header("Content-Type", "application/json")
                .filter(sessionFilter)
                .pathParam("issueIdOrKey", key)
                .when()
                .delete("/rest/api/2/issue/{issueIdOrKey}")
                .then()
                .assertThat()
                .statusCode(204);
    }
}
