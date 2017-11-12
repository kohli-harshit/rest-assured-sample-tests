package api;

import com.google.common.base.Joiner;
import com.jayway.jsonpath.JsonPath;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

/**
 * Created by kohlih on 12-11-2017.
 */
public class PostToken {
    String baseURI;
    String apiPath="/oauth2/v4/token";
    int expectedStatusCode;
    String contentType;
    Map<String,String> bodyParams;
    String accessToken;

    RequestSpecBuilder requestSpecBuilder;
    RequestSpecification requestSpecification;
    ResponseSpecBuilder responseSpecBuilder;
    ResponseSpecification responseSpecification;
    Response apiResponse;

    public PostToken(String baseURI) {
        this.baseURI=baseURI;
        requestSpecBuilder=new RequestSpecBuilder();
        responseSpecBuilder=new ResponseSpecBuilder();
        bodyParams = new HashMap<String, String>();
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setBodyParams(Map<String, String> bodyParams) {
        this.bodyParams = bodyParams;
    }

    public void addBodyParam(String key, String value) {
        bodyParams.put(key,value);
    }

    public void setExpectedStatusCode(int expectedStatusCode) {
        this.expectedStatusCode = expectedStatusCode;
    }

    public String getAccessToken() {
        return accessToken;
    }

    private void createRequest(){
        requestSpecBuilder.setBaseUri(baseURI);
        requestSpecBuilder.setBasePath(apiPath);
        requestSpecBuilder.setContentType(contentType);
        requestSpecBuilder.setBody(Joiner.on("&").withKeyValueSeparator("=").join(bodyParams));
        requestSpecification = requestSpecBuilder.build();
    }

    private void executeRequest(){
        apiResponse = given().spec(requestSpecification).post();
    }

    private void validateResponse(){
        responseSpecBuilder.expectStatusCode(expectedStatusCode);
        responseSpecification = responseSpecBuilder.build();
        apiResponse.then().spec(responseSpecification);
        accessToken= JsonPath.read(apiResponse.asString(),"$.access_token");
    }

    public void perform(){
        createRequest();
        executeRequest();
        validateResponse();
    }



}
