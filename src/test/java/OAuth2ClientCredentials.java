import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class OAuth2ClientCredentials {
    static String CLIENT_ID = "rest_assured_oauth_demo_app";
    static String CLIENT_SECRET = "";
    static String base_url = "http://coop.apps.symfonycasts.com/";
    static String USER_ID = "";

  public static void main(String[] args) {
      RestAssured.baseURI = base_url;

      String response = RestAssured.given()
              .auth()
              .oauth2(generateAccessToken())
              .when()
              .log()
              .all()
              .post("/api/"+USER_ID+"/barn-unlock")
              .asString();

      JsonPath jsonPath = new JsonPath(response);
      System.out.println(jsonPath.getString("message"));
  }

  public static String generateAccessToken(){
      Response accessTokenResponse =
            RestAssured.given()
            .formParam("client_id", CLIENT_ID)
            .formParam("client_secret", CLIENT_SECRET)
            .formParam("grant_type", "client_credentials")
            .when()
            .post("/token");

    return accessTokenResponse.jsonPath().getString("access_token");
  }
}
