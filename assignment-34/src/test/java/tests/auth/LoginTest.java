package tests.auth;

import body.auth.LoginBody;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.testng.annotations.Test;
import utils.ConfigReader;

import java.io.FileWriter;
import java.io.IOException;

public class LoginTest {
    @Test
    public void loginTest() throws IOException {
        RestAssured.baseURI = ConfigReader.getProperty("baseUrl");

        // Take payload
        LoginBody loginBody = new LoginBody();

        // Hit endpoint
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(loginBody.loginData().toString())
                .when()
                .post("v1/login")
                .then()
                .extract().response();

        System.out.println("Response : " + response.asString());

        // Extract token
        String token = response.jsonPath().get("data.token");
        System.out.println("Token : " + token);

        // Store token
        JSONObject tokenJson = new JSONObject();
        tokenJson.put("token", token);

        try (FileWriter file = new FileWriter("src/resources/json/token.json")){
            file.write(tokenJson.toJSONString());
            file.flush();
        }
        System.out.println("Token saved to token.json");
    }
}