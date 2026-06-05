package tests.sportcategory;

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

public class UpdateCategoryById {
    @Test(priority = 2, description = "TC-INT-SC-005 : User Can Update Sport Category With Valid Data")
    public void userCanUpdateSportCategoryWithValidData() {
        // Get token
        String token = JSONHelper.getJSONValueByKey("token", "token.json");
        String id = JSONHelper.getJSONValueByKey("id", "sport-category.json");

        // Get dataset from CSV
        SportCategoryBody sportCategoryBody = new SportCategoryBody();
        List<JSONObject> dataset = sportCategoryBody.createSportCategoryValidData("sport-category");

        // Hit endpoint & calculate response time
        long startTime = System.currentTimeMillis();
        Response response = TestUtils.templateResponsePost("v1/sport-categories/update/"+id, 200, "Post Update Sport Category", dataset.get(0), token);
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

        // Validate updated category name match with req body name
        Assert.assertEquals(data.get("name"), dataset.get(0).get("name"));

        // Validate performance (response time)
        Assert.assertTrue(responseTime < 2000, "Response time exceeds 2000ms");
    }

    @Test(priority = 2, description = "TC-INT-SC-006 : User Cant Update Sport Category With Invalid Empty Data")
    public void userCantUpdateSportCategoryWithInvalidEmptyData() {
        // Get token
        String token = JSONHelper.getJSONValueByKey("token", "token.json");
        String id = JSONHelper.getJSONValueByKey("id", "sport-category.json");

        // Hit endpoint & calculate response time
        long startTime = System.currentTimeMillis();
        Response response = TestUtils.templateResponsePost("v1/sport-categories/update/"+id, 406, "Post Update Sport Category", null, token);
        long responseTime = System.currentTimeMillis() - startTime;

        // Parse response
        JsonPath res = response.jsonPath();

        Assert.assertTrue(
                (Boolean) res.get("error"),
                "error is not true");
        Assert.assertTrue(
                res.get("message") instanceof String && !((String) res.get("message")).trim().isEmpty(),
                "message is invalid");

        Assert.assertEquals(res.get("message"), "The name field is required.");

        // Validate performance (response time)
        Assert.assertTrue(responseTime < 2000, "Response time exceeds 2000ms");
    }
}
