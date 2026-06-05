package tests.auth;

import core.TestUtils;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.JSONHelper;

import java.io.IOException;
import java.util.List;

public class PostSignOut {
    @Test(priority = 9, description = "TC-INT-AU-003 : User Can Sign Out With Valid Token")
    public void userCanSignOutWithValidToken() throws IOException {
        // Take token
        String token = JSONHelper.getJSONValueByKey("token", "token.json");

        // Hit endpoint & calculate response time
        long startTime = System.currentTimeMillis();
        Response response = TestUtils.templateResponsePost("v1/logout", 200, "Post Sign Out", null, token);
        long responseTime = System.currentTimeMillis() - startTime;

        // Parse response
        JsonPath res = response.jsonPath();

        // Validate top-level key not null
        List<String> topLevelKeysNotNullTest = List.of("message");
        for (String key : topLevelKeysNotNullTest){
            Assert.assertNotNull(res.get(key), "Response missing required top-level keys");
        }

        // Validate success message
        Assert.assertEquals(res.get("message"),"Successfully logged out");

        // Validate performance (response time)
        Assert.assertTrue(responseTime < 2000, "Response time exceeds 2000ms");
    }
}
