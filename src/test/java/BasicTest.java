import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class BasicTest {
    public static void main(String[] args) {
        RestAssured .baseURI = "https://rahulshettyacademy.com";

        //POST method
        String response = given()
                .log()
                .all()
                .queryParam("key =qaclick123")
                .header("Content-Type", "application/json;charset=UTF-8")
                .body("{\n" +
                        "  \"location\": {\n" +
                        "    \"lat\": -38.383494,\n" +
                        "    \"lng\": 33.427362\n" +
                        "  },\n" +
                        "  \"accuracy\": 50,\n" +
                        "  \"name\": \"Frontline house\",\n" +
                        "  \"phone_number\": \"(+91) 983 893 3937\",\n" +
                        "  \"address\": \"29, side layout, cohen 09\",\n" +
                        "  \"types\": [\n" +
                        "    \"shoe park\",\n" +
                        "    \"shop\"\n" +
                        "  ],\n" +
                        "  \"website\": \"http://google.com\",\n" +
                        "  \"language\": \"French-IN\"\n" +
                        "}\n")
                .when()
                .post("/maps/api/place/add/json")
                .then()
                .log()
                .all()
                .assertThat()
                .statusCode(200)
                .body("scope", equalTo("APP"))
                .extract()
                .response()
                .asString();

        JsonPath jsonPath = new JsonPath(response);
        String placeId = jsonPath.getString("place_id");
        System.out.println(placeId);

        //PUT method
        String newAddress = "70 Summer walk, USA";
        given()
                .queryParam("key", "qaclick123")
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "\"place_id\":\"" + placeId + "\",\n" +
                        "\"address\":\"" + newAddress + "\",\n" +
                        "\"key\":\"qaclick123\"\n" +
                        "}")
                .when()
                .put("/maps/api/place/update/json")
                .then()
                .log()
                .all()
                .assertThat()
                .body("msg", equalTo("Address successfully updated"));

        //GET method
        String getResponse = given()
                .queryParam("key", "qaclick123")
                .queryParam("place_id", placeId)
                .when()
                .get("/maps/api/place/update/json")
                .then()
                .assertThat()
                .statusCode(200)
                .log()
                .all()
                .extract()
                .response()
                .asString();

        JsonPath jsonPath1 = new JsonPath(getResponse);
        Assert.assertEquals(200, jsonPath1.getString("address")); //This assertion fails because the response is empty with status code 200
    }
}
