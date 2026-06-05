package tests.auth;

import base.BaseTest;
import body.auth.PostLoginBody;
import core.TestUtils;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static utils.JSONHelper.saveToJSON;

public class PostLogin extends BaseTest {
    @Test(priority = 1, description = "TC-INT-AU-001 : User Can Login With Valid Data")
    public void userCanLoginWithValidData() throws IOException {
        // Take payload
        PostLoginBody loginBody = new PostLoginBody();
        String payload = loginBody.loginValidData().toString();

        // Hit endpoint & calculate response time
        long startTime = System.currentTimeMillis();
        Response response = TestUtils.templateResponsePost("v1/login", 200, "Post Login", payload, null);
        long responseTime = System.currentTimeMillis() - startTime;

        // Parse response
        JsonPath res = response.jsonPath();

        // Validate top-level key not null
        List<String> topLevelKeysNotNullTest = List.of("success", "data", "message");
        for (String key : topLevelKeysNotNullTest){
            Assert.assertNotNull(res.get(key), "Response missing required top-level keys");
        }

        // Validate top-level key data type and value
        Map<String, Object> data = res.get("data");

        Assert.assertTrue(
                (Boolean) res.get("success"),
                "success is not true");
        Assert.assertTrue(
                res.get("message") instanceof String && !((String) res.get("message")).trim().isEmpty(),
                "message is invalid");
        Assert.assertFalse(
                data instanceof java.util.List,
                "data should not be an array");

        // Validate body.data object
        List<String> stringFields = List.of("token", "name", "email");
        TestUtils.validateColumn(data, stringFields, "string", false);

        // Validate email max char
        Assert.assertTrue(
                ((String) data.get("email")).length() < 255,
                "Email exceeds 255 chars");

        // Validate email format
        String email = (String) data.get("email");
        String emailRegex = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$";
        Assert.assertTrue(email.matches(emailRegex), "Email format is invalid");

        // Validate email matching
        String requestEmail = (String) loginBody.loginValidData().get("email");
        Assert.assertEquals(email, requestEmail, "Email does not match request");

        // Validate performance (response time)
        Assert.assertTrue(responseTime < 2000, "Response time exceeds 2000ms");

        // Extract & store token
        String token = (String)data.get("token");
        saveToJSON("token", token, "token.json");
    }

    @Test(priority = 1, description = "TC-INT-AU-002 : User Cant Login With Invalid Unregistered Account")
    public void userCantLoginWithInvalidUnregisteredAccount() throws IOException {
        // Take payload
        PostLoginBody loginBody = new PostLoginBody();
        String payload = loginBody.loginInvalidUnregisteredData().toString();

        // Hit endpoint & calculate response time
        long startTime = System.currentTimeMillis();
        Response response = TestUtils.templateResponsePost("v1/login", 404, "Post Login", payload, null);
        long responseTime = System.currentTimeMillis() - startTime;

        // Parse response
        JsonPath res = response.jsonPath();

        // Validate top-level key not null
        List<String> topLevelKeysNotNullTest = List.of("success", "data", "message");
        for (String key : topLevelKeysNotNullTest){
            Assert.assertNotNull(res.get(key), "Response missing required top-level keys");
        }

        // Validate top-level key data type and value
        Map<String, Object> data = res.get("data");

        Assert.assertFalse(
                (Boolean) res.get("success"),
                "success is not false");
        Assert.assertTrue(
                res.get("message") instanceof String && !((String) res.get("message")).trim().isEmpty(),
                "message is invalid");
        Assert.assertFalse(
                data instanceof java.util.List,
                "data should not be an array");

        // Validate failed message
        Assert.assertEquals(res.get("message"),"Unauthorised.");

        // Validate performance (response time)
        Assert.assertTrue(responseTime < 2000, "Response time exceeds 2000ms");
    }
}