import api.GetBookShelfVolumes;
import api.PostToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import io.restassured.response.Response;
import net.javacrumbs.jsonunit.core.Configuration;
import net.javacrumbs.jsonunit.core.internal.Diff;
import org.testng.Assert;
import org.testng.annotations.Test;
import pojo.Books;
import pojo.User;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import static io.restassured.RestAssured.given;

/**
 * Created by kohlih on 11-11-2017.
 */
public class AdvancedTests {

    @Test
    public void GoogleApiOAuthExample(){

        //Call API to get Token
        Response tokenResponse = given().contentType("application/x-www-form-urlencoded").
                when().body("client_id=value&client_secret=value&grant_type=refresh_token&refresh_token=value").post("https://www.googleapis.com/oauth2/v4/token").
                then().statusCode(200).extract().response();

        //Extract Token from Response
        String accessToken = JsonPath.read(tokenResponse.asString(),"$.access_token");
        System.out.println("Token = " + tokenResponse.asString());

        //Provide token as an OAuth2 parameter to rest-assured call
        Response response = given().auth().oauth2(accessToken).pathParam("shelfId","0").
                when().get("https://www.googleapis.com/books/v1/mylibrary/bookshelves/{shelfId}/volumes").
                then().statusCode(200).extract().response();

        //Count of Books from the Favourites Shelf should be greater than 0
        int totalItems = JsonPath.read(response.asString(),"$.totalItems");
        Assert.assertTrue(totalItems>0,"Total Books from Favourites shelf should be greater than 0");
    }

    @Test
    public void XMLPOJOExample() throws JAXBException {
        //Call Get User API
        Response response = given().accept("application/xml").pathParam("username","kohliharshit").
                when().get("http://petstore.swagger.io/v2/user/{username}").
                then().statusCode(200).extract().response();

        //Unmarshall(Deserialize) the Response
        StringReader apiResponse = new StringReader(response.asString());
        User objUser = (User)JAXBContext.newInstance(User.class).createUnmarshaller().unmarshal(apiResponse);

        //Play around with the Java Object
        Assert.assertEquals(objUser.getId(),"123");
        Assert.assertEquals(objUser.getFirstName(),"harshit");
    }

    @Test
    public void JSONPOJOExample() throws IOException {
        //Call API to get Token
        Response tokenResponse = given().contentType("application/x-www-form-urlencoded").
                when().body("client_id=value&client_secret=value&grant_type=refresh_token&refresh_token=value").post("https://www.googleapis.com/oauth2/v4/token").
                then().statusCode(200).extract().response();

        //Extract Token from Response
        String accessToken = JsonPath.read(tokenResponse.asString(),"$.access_token");
        System.out.println("Token = " + tokenResponse.asString());

        //Provide token as an OAuth2 parameter to rest-assured call
        Response response = given().auth().oauth2(accessToken).pathParam("shelfId","8").
                when().get("https://www.googleapis.com/books/v1/mylibrary/bookshelves/{shelfId}/volumes").
                then().statusCode(200).extract().response();

        //Total Count of Books from the Recommended Shelf should be 100
        Books books  = new ObjectMapper().readValue(response.asString(),Books.class);
        Assert.assertTrue(books.getTotalItems()==100,"Total Books from Recommended shelf should be 100");

        //Compare it with Expected result
        Books expectedBooks = new ObjectMapper().readValue(new File("test-data\\ExpectedFavourites.json"),Books.class);
        Diff diff = Diff.create(expectedBooks, books, "fullJson", "", Configuration.empty());
        Assert.assertTrue(diff.similar(),diff.toString());

        //Would fail if any key of JSON is different
        expectedBooks.getItems().get(0).setId("111");
        diff = Diff.create(expectedBooks, books, "fullJson", "", Configuration.empty());
        Assert.assertTrue(diff.similar(),diff.toString());
    }

    @Test
    public void ModularisedApiCall() throws IOException {
        //Call API to get Token
        PostToken postToken = new PostToken("https://www.googleapis.com");
        postToken.setContentType("application/x-www-form-urlencoded");
        postToken.addBodyParam("client_id","value");
        postToken.addBodyParam("client_secret","value");
        postToken.addBodyParam("grant_type","value");
        postToken.addBodyParam("refresh_token","value");
        postToken.setExpectedStatusCode(200);
        postToken.perform();

        String accessToken = postToken.getAccessToken();
        System.out.println("Token = " + accessToken);

        //Provide token as an OAuth2 parameter to rest-assured call
        GetBookShelfVolumes getBookShelfVolumes = new GetBookShelfVolumes("https://www.googleapis.com/books/v1");
        getBookShelfVolumes.setAccessToken(accessToken);
        getBookShelfVolumes.setShelfId(8);
        getBookShelfVolumes.setExpectedStatusCode(200);
        getBookShelfVolumes.perform();

        //Total Count of Books from the Recommended Shelf should be 100
        Books books  = new ObjectMapper().readValue(getBookShelfVolumes.getApiResponse().asString(),Books.class);
        Assert.assertTrue(books.getTotalItems()==100,"Total Books from Recommended shelf should be 100");

    }
}

