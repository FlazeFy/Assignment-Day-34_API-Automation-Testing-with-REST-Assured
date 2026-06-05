package tests.sportcategory;

import base.BaseTest;
import body.sportcategory.SportCategoryBody;
import core.TestUtils;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.JSONHelper;

import java.util.List;
import java.util.Map;

public class PostCreateCategory extends BaseTest {
    @Test(priority = 2, description = "TC-INT-SC-001 : User Can Create Sport Category With Valid Data")
    public void userCanCreateSportCategoryWithValidData() {
        // Get token
        String token = JSONHelper.getJSONValueByKey("token", "token.json");

        // Get dataset from CSV
        SportCategoryBody sportCategoryBody = new SportCategoryBody();
        List<JSONObject> dataset = sportCategoryBody.createSportCategoryValidData("sport-category");

        // Loop based on dataset size
        for (JSONObject payload : dataset) {
            // Hit endpoint & calculate response time
            long startTime = System.currentTimeMillis();
            Response response = TestUtils.templateResponsePost("v1/sport-categories/create", 200, "Post Create Sport Category", payload, token);
            long responseTime = System.currentTimeMillis() - startTime;

            // Parse response
            JsonPath res = response.jsonPath();

            // Validate top-level key result type and value
            Map<String, Object> data = res.get("result");

            Assert.assertFalse(
                    (Boolean) res.get("error"),
                    "error is not false");
            Assert.assertTrue(
                    res.get("message") instanceof String && !((String) res.get("message")).trim().isEmpty(),
                    "message is invalid");
            Assert.assertFalse(
                    data instanceof java.util.List,
                    "result should not be an array");

            // Validate body.result object
            List<String> stringFields = List.of("name", "created_at", "updated_at");
            List<String> intFields = List.of("id");
            TestUtils.validateColumn(data, stringFields, "string", false);
            TestUtils.validateColumn(data, intFields, "number", false);

            // Extract & store id
            String categoryId = res.getString("result.id");
            JSONHelper.saveToJSON("id", categoryId, "sport-category.json");

            // Validate performance (response time)
            Assert.assertTrue(responseTime < 2000, "Response time exceeds 2000ms");
        }
    }

    @Test(priority = 2, description = "TC-INT-SC-002 : User Cant Create Sport Category With Invalid Name Data Type")
    public void userCantCreateSportCategoryWithInvalidNameDataType() {
        // Get token
        String token = JSONHelper.getJSONValueByKey("token", "token.json");

        // Get dataset from CSV
        SportCategoryBody sportCategoryBody = new SportCategoryBody();
        JSONObject payload = sportCategoryBody.createSportCategoryInvalidNameDataType();

        // Hit endpoint & calculate response time
        long startTime = System.currentTimeMillis();
        Response response = TestUtils.templateResponsePost("v1/sport-categories/create", 406, "Post Create Sport Category", payload, token);
        long responseTime = System.currentTimeMillis() - startTime;

        // Parse response
        JsonPath res = response.jsonPath();

        Assert.assertTrue(
                (Boolean) res.get("error"),
                "error is not true");
        Assert.assertTrue(
                res.get("message") instanceof String && !((String) res.get("message")).trim().isEmpty(),
                "message is invalid");

        Assert.assertEquals(res.get("message"), "The name field must be a string.");

        // Validate performance (response time)
        Assert.assertTrue(responseTime < 2000, "Response time exceeds 2000ms");
    }
}