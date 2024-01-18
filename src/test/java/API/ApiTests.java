package API;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.jar.JarEntry;

public class ApiTests {

    @BeforeClass
    public void setUp(){
        RestAssured.baseURI = "https://jsonplaceholder.typicode.com";
    }


    @Test
    public void testGetPosts(){

        Response response = RestAssured.get("/posts");

        //validate the response status code and content
        response.then().statusCode(200);
        response.then().log().body();

    }

    @Test
    public void testCreatePost(){
        JSONObject createParams = new JSONObject();
        createParams.put("title","New post");
        createParams.put("body","This is the body of the new post");
        createParams.put("userId",1);

        Response createResponse = RestAssured.given()
                .header("Content-type","application/json")
                .and()
                .body(createParams.toString())
                .when()
                .post("/posts");


        createResponse.then().statusCode(201);
        createResponse.then().log().body();

    }

    @Test
    public void testUpdatePost() {
        JSONObject requestParams = new JSONObject();
        requestParams.put("id", 1);
        requestParams.put("title", "Updated Title");
        requestParams.put("body", "Updated body content");
        requestParams.put("userId", 1);

        Response response = RestAssured.given()
                .header("Content-type", "application/json")
                .and()
                .body(requestParams.toString())
                .when()
                .put("/posts/1");

        response.then().statusCode(200);
        response.then().log().body();
    }

    @Test
    public void testDeletePost() {
        Response response = RestAssured.delete("/posts/1");

        response.then().statusCode(200);
        response.then().log().body();
    }



}
