import io.restassured.RestAssured;
import pojo.PostUsers;

public class POJOSerializationTest {
  public static void main(String[] args) {
      PostUsers postUser = new PostUsers();
      postUser.setName("morpheus");
      postUser.setJob("leader");

      RestAssured.baseURI = "https://reqres.in/";

      String response = RestAssured.given()
                        .body(postUser)
                        .when()
                        .post("/api/users")
                        .then()
                        .extract()
                        .response()
                        .asString();

      System.out.println(response);
  }
}
