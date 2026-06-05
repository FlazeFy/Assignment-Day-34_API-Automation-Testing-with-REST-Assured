package tests.sportcategory;

import base.BaseTest;
import core.TestUtils;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.JSONHelper;

public class DeleteCategoryById extends BaseTest {
    @Test(priority = 8, description = "TC-INT-SC-003 : User Can Delete Category By Valid Id")
    public void userCanDeleteCategoryByValidId() {
        // Get token and id
        String token = JSONHelper.getJSONValueByKey("token", "token.json");
        String id = JSONHelper.getJSONValueByKey("id", "sport-category.json");

        // Hit endpoint & calculate response time
        long startTime = System.currentTimeMillis();
        Response response = TestUtils.templateResponseDelete("v1/sport-categories/delete/"+id, 200, "Delete Sport Category By Id", token);
        long responseTime = System.currentTimeMillis() - startTime;

        // Parse response
        JsonPath res = response.jsonPath();

        Assert.assertFalse(
                (Boolean) res.get("error"),
                "error is not false");
        Assert.assertTrue(
                res.get("message") instanceof String && !((String) res.get("message")).trim().isEmpty(),
                "Data deleted successfully");

        // Validate performance (response time)
        Assert.assertTrue(responseTime < 2000, "Response time exceeds 2000ms");
    }

    @Test(priority = 3, description = "TC-INT-SC-004 : User Cant Delete Category With Invalid Id")
    public void userCantDeleteCategoryWithInvalidId() {
        // Get token
        String token = JSONHelper.getJSONValueByKey("token", "token.json");
        String invalidId = "A";

        // Hit endpoint & calculate response time
        long startTime = System.currentTimeMillis();
        Response response = TestUtils.templateResponseDelete("v1/sport-categories/delete/"+invalidId, 406, "Delete Sport Category By Id", token);
        long responseTime = System.currentTimeMillis() - startTime;

        // Parse response
        JsonPath res = response.jsonPath();

        Assert.assertTrue(
                (Boolean) res.get("error"),
                "error is not true");
        Assert.assertTrue(
                res.get("message") instanceof String && !((String) res.get("message")).trim().isEmpty(),
                "message is invalid");

//        Assert.assertEquals(res.get("message"), "The name field must be a string.");

        // Validate performance (response time)
        Assert.assertTrue(responseTime < 2000, "The id param must be a integer.");
    }
}