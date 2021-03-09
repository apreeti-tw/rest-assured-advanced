import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class ParseComplexJSON {
    static JsonPath jsonPath;

    @BeforeClass
    public void loadData(){
        jsonPath = new JsonPath(DataFiles.getCourses());
    }

    @Test
    public void shouldPrintNumberOfCourses(){
        System.out.println("Number of courses: " + jsonPath.getInt("courses.size()"));
    }

    @Test
    public void shouldPrintPurchaseAmount(){
        System.out.println("Purchase Amount: " + jsonPath.getInt("dashboard.purchaseAmount"));
    }

    @Test
    public void shouldPrintTitleOfFirstCourse(){
        System.out.println("First course title: " + jsonPath.<String>get("courses[0].title"));
    }

    @Test
    public void shouldPrintAllCourseTitles(){
        for (int i = 0; i < jsonPath.getInt("courses.size()"); i++) {
            System.out.println("Course "+ (i+1) +" title: " + jsonPath.<String>get("courses["+ i +"].title"));
            System.out.println("Course "+ (i+1) +" price: " + jsonPath.getInt("courses["+ i +"].price"));
        }
    }

    @Test
    public void shouldPrintNumberOfCopiesSoldByRPA(){
        for (int i = 0; i < jsonPath.getInt("courses.size()"); i++) {
            if(jsonPath.<String>get("courses["+ i +"].title").equalsIgnoreCase("RPA")){
                System.out.println("Copies sold by RPA: " + jsonPath.getInt("courses["+ i +"].copies"));
                break;
            }
        }
    }

    @Test
    public void shouldMatchSumOfAllPricesWithPurchasePrice(){
        int purchasePrice = jsonPath.getInt("dashboard.purchaseAmount");
        int priceTotal = 0;
        for (int i = 0; i < jsonPath.getInt("courses.size()"); i++) {
            priceTotal+=(jsonPath.getInt("courses["+ i +"].price")*jsonPath.getInt("courses["+ i +"].copies"));
        }
        Assert.assertEquals(purchasePrice, priceTotal);
    }
}
