import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/**
 * Created by kohlih on 12-11-2017.
 */
public class HamcrestExamples {

    @Test
    public void TestCase1WithHamcrest(){
        given().
            param("q","paulo+coelho").
        when().
            get("https://www.googleapis.com/books/v1/volumes").
        then().
            assertThat().statusCode(200).body("totalItems",greaterThan(0));
    }

    @Test
    public void TestCase2WithHamcrest(){
        given().
            param("q","potter").
        when().
            get("https://www.googleapis.com/books/v1/volumes").
        then().
            assertThat().statusCode(200).body("items", hasSize(10));
    }

    @Test
    public void TestCase3WithHamcrest(){
        given().
            param("q","dan+brown").param("maxResults","25").
        when().
            get("https://www.googleapis.com/books/v1/volumes").
        then().
            assertThat().statusCode(200).body("items", hasSize(25));
    }

    @Test
    public void TestCase4WithHamcrest(){
        given().
            param("q","").
        when().
            get("https://www.googleapis.com/books/v1/volumes").
        then().
            assertThat().statusCode(400).body("error.message", equalTo("Missing query.")).body("error.errors[0].location",equalTo("q"));
    }

    @Test
    public void TestCase5WithHamcrest(){
        given().
            param("q","india").param("maxResults","50").
        when().
            get("https://www.googleapis.com/books/v1/volumes").
        then().
            assertThat().statusCode(400).body("error.message", containsString("Values must be within the range: [0, 40]"),"error.errors[0].location",equalTo("maxResults"));
    }
}
