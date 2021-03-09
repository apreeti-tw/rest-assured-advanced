import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class OAuth2AuthorizationCode {
static String CLIENT_ID = "";
static String CLIENT_SECRET = "";
static String base_url = "http://coop.apps.symfonycasts.com";
static String USER_ID = "";

public static void main(String[] args) {
  RestAssured.baseURI = base_url;

  Response response = RestAssured.given()
          .auth()
          .oauth2(generateAccessToken())
          .when()
          .log()
          .all()
          .post("/api/"+USER_ID+"/barn-unlock")
          .then()
          .extract()
          .response();

  System.out.println(response.jsonPath().getString("message"));
}

public static String generateAccessToken(){
    Response accessTokenResponse =
            RestAssured.given()
                    .formParam("client_id", CLIENT_ID)
                    .formParam("client_secret", CLIENT_SECRET)
                    .formParam("grant_type", "authorization_code")
                    .formParam("redirect_uri", base_url)
                    .formParam("code", generateCode())
                    .when()
                    .post("/token");

    return accessTokenResponse.jsonPath().getString("access_token");
}

    private static String generateCode() {
        System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+"/src/chromedriver");
        WebDriver driver = new ChromeDriver();
        driver.get(base_url+"/authorize?client_id="+CLIENT_ID+"&redirect_uri="+base_url+"&response_type=code&scope=barn-unlock&state=");
        driver.findElement(By.id("form-email")).sendKeys("");
        driver.findElement(By.id("form-password")).sendKeys("");
        driver.findElement(By.cssSelector("button")).submit();
        driver.findElement(By.linkText("Yes, I Authorize This Request")).click();

        String code = StringUtils.substringBetween(driver.getCurrentUrl(), "code=", "&");

        driver.close();
        driver.quit();

        return code;
    }
}
