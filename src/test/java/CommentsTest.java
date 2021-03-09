import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;
import org.testng.annotations.*;

import java.io.IOException;

import static io.restassured.RestAssured.given;

public class CommentsTest {
    static String response = "";
    static String key = "";
    static String commentId = "";
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
        key = jsonPath.getString("key");

        response = given()
                .header("Content-Type", "application/json")
                .pathParam("issueIdOrKey", key)
                .filter(sessionFilter)
                .body(DataFiles.getComment())
                .when()
                .post("/rest/api/2/issue/{issueIdOrKey}/comment")
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .response()
                .asString();

        jsonPath = new JsonPath(response);
        commentId = jsonPath.getString("id");
    }

    @Test
    public static void getCommentByRestrictingResult() throws IOException {
        response = given()
                .header("Content-Type", "application/json")
                .queryParam("fields", "comment")
                .pathParam("issueIdOrKey", key)
                .filter(sessionFilter)
                .body(DataFiles.getComment())
                .when()
                .get("/rest/api/2/issue/{issueIdOrKey}")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .response()
                .asString();

        jsonPath = new JsonPath(response);
        boolean found = false;
        for (int i = 0; i < jsonPath.getInt("fields.comment.comments.size()"); i++) {
            if(jsonPath.getString("fields.comment.comments["+i+"].id").equalsIgnoreCase(commentId)){
                found = true;
                break;
            }
        }
        if (found){
            System.out.println("Comment "+commentId+" was found in the response");
        }
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
