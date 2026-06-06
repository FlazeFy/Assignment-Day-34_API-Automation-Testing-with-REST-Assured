package tests.transaction;

import base.BaseTest;
import body.transaction.TransactionBody;
import core.TestUtils;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.JSONHelper;
import java.util.List;
import java.util.Map;

public class PostCreateTransaction extends BaseTest {
    @Test(priority = 5, description = "TC-INT-TR-005 : User Can Create Transaction With Valid Data")
    public void userCanCreateSportTransactionWithValidData() {
        // Get token
        String token = JSONHelper.getJSONValueByKey("token", "token.json");

        // Take payload
        TransactionBody transactionBodyBody = new TransactionBody();
        String payload = transactionBodyBody.createTransactionValidData().toString();

        // Hit endpoint & calculate response time
        long startTime = System.currentTimeMillis();
        Response response = TestUtils.templateResponsePost(
                "v1/transaction/create",
                200,
                "Post Create Transaction",
                payload,
                token
        );
        long responseTime = System.currentTimeMillis() - startTime;

        // Parse response
        JsonPath res = response.jsonPath();

        // Validate top-level key result type and value
        Map<String, Object> data = res.get("result");

        Assert.assertFalse(
                (Boolean) res.get("error"),
                "error is not false"
        );

        Assert.assertFalse(
                data instanceof java.util.List,
                "result should not be an array"
        );

        // Validate body.result object
        List<String> stringFields = List.of("invoice_id", "status", "order_date", "expired_date", "updated_at", "created_at");
        List<String> intFields = List.of("id", "total_amount", "payment_method_id", "user_id");
        TestUtils.validateColumn(data, stringFields, "string", false);
        TestUtils.validateColumn(data, intFields, "number", false);

        // Extract & store id
        String transactionId = res.getString("result.id");
        JSONHelper.saveToJSON("id", transactionId, "transaction.json");

        // Validate performance (response time)
        Assert.assertTrue(responseTime < 2000, "Response time exceeds 2000ms");
    }
}
