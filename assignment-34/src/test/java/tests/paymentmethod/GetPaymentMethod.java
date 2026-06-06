package tests.paymentmethod;

import core.TestUtils;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.JSONHelper;

import java.util.List;
import java.util.Map;

import static utils.JSONHelper.saveToJSON;

public class GetPaymentMethod {
    @Test(priority = 2, description = "TC-INT-PM-001 : User Can See Payment Method With Valid Data")
    public void userCanSeePaymentMethodWithValidData() {
        // Get token
        String token = JSONHelper.getJSONValueByKey("token", "token.json");

        // Hit endpoint & calculate response time
        long startTime = System.currentTimeMillis();
        Response response = TestUtils.templateResponseGet(
                "v1/payment-methods",
                200,
                "Get Payment Method",
                token);
        long responseTime = System.currentTimeMillis() - startTime;

        // Parse response
        JsonPath res = response.jsonPath();

        // Validate top-level key result type and value
        List<Map<String, Object>> data = res.get("result");

        Assert.assertFalse(
                (Boolean) res.get("error"),
                "error is not false");

        // Validate body.result array
        Assert.assertTrue(
                data instanceof java.util.List,
                "result should be an array");
        List<String> stringFields = List.of("name", "created_at", "updated_at", "image_url", "virtual_account_number", "virtual_account_name");
        List<String> intFields = List.of("id");
        TestUtils.validateColumn(data, stringFields, "string", false);
        TestUtils.validateColumn(data, intFields, "number", false);

        // Extract & store id
        Object idObj = data.get(0).get("id");
        String id = String.valueOf(idObj);
        saveToJSON("id", id, "payment-method.json");

        // Validate performance (response time)
        Assert.assertTrue(responseTime < 2000, "Response time exceeds 2000ms");
    }
}
