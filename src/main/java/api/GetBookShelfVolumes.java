package api;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.RestAssured.given;

/**
 * Created by kohlih on 12-11-2017.
 */
public class GetBookShelfVolumes {

    String baseURI;
    String apiPath="/mylibrary/bookshelves/{shelfId}/volumes";
    int expectedStatusCode;
    String accessToken;
    int shelfId;


    RequestSpecBuilder requestSpecBuilder;
    RequestSpecification requestSpecification;
    ResponseSpecBuilder responseSpecBuilder;
    ResponseSpecification responseSpecification;
    Response apiResponse;

    public GetBookShelfVolumes(String baseURI) {
        this.baseURI=baseURI;
        requestSpecBuilder=new RequestSpecBuilder();
        responseSpecBuilder=new ResponseSpecBuilder();
    }

    public void setExpectedStatusCode(int expectedStatusCode) {
        this.expectedStatusCode = expectedStatusCode;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setShelfId(int shelfId) {
        this.shelfId = shelfId;
    }

    public Response getApiResponse() {
        return apiResponse;
    }

    private void createRequest(){
        requestSpecBuilder.setBaseUri(baseURI);
        requestSpecBuilder.setBasePath(apiPath);
        requestSpecBuilder.addPathParam("shelfId",shelfId);
        requestSpecification = requestSpecBuilder.build();
    }

    private void executeRequest(){
        apiResponse = given().spec(requestSpecification).auth().oauth2(accessToken).get();
    }

    private void validateResponse(){
        responseSpecBuilder.expectStatusCode(expectedStatusCode);
        responseSpecification = responseSpecBuilder.build();
        apiResponse.then().spec(responseSpecification);
    }

    public void perform(){
        createRequest();
        executeRequest();
        validateResponse();
    }
}
