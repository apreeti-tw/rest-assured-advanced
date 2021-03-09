import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.sessionId;
import static java.nio.file.Files.readAllBytes;

public class CreateIssueTest {
    static String response = "";
    static SessionFilter sessionFilter = null;
    static JsonPath jsonPath = null;

    @BeforeClass
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

    @Test
    public static void createIssue() throws IOException {
        response = given()
                .header("Content-Type", "application/json")
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
        System.out.println("Issue created: "+ jsonPath.getString("key"));
    }

    @AfterClass
    public static void tearDown(){
        given()
                .header("Content-Type", "application/json")
                .filter(sessionFilter)
                .when()
                .delete("/rest/api/2/issue/{issueIdOrKey}"+jsonPath.getString("key"))
                .then()
                .assertThat()
                .statusCode(204);
    }
}
