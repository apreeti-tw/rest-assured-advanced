import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import pojo.Data;
import pojo.GetUsers;

import java.util.List;

public class POJODeserializationTest {
  public static void main(String[] args) {
      GetUsers getUsers = RestAssured.given()
              .expect()
              .defaultParser(Parser.JSON)
              .when()
              .get("https://reqres.in/api/users")
              .then()
              .extract()
              .response()
              .as(GetUsers.class);

    System.out.println(getUsers.getPage());
    System.out.println(getUsers.getTotal());

    List<Data> usersData = getUsers.getData();

    for (Data userData : usersData) {
      System.out.println(userData.getEmail());
    }
  }
}
