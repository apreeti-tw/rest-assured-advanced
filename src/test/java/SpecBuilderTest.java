import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class SpecBuilderTest {
  public static void main(String[] args) {
      RequestSpecification requestSpecification = new RequestSpecBuilder().setBaseUri("https://reqres.in/").setContentType(ContentType.JSON).build();
      ResponseSpecification responseSpecification = new ResponseSpecBuilder().expectContentType(ContentType.JSON).expectStatusCode(200).build();

      RestAssured.given()
              .spec(requestSpecification)
              .when()
              .get("/api/users")
              .then()
              .log()
              .all()
              .spec(responseSpecification);

  }
}
