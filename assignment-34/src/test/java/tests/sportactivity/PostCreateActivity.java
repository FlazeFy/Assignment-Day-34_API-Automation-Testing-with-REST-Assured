package tests.sportactivity;

import base.BaseTest;

import body.sportactivity.SportActivityBody;
import core.TestUtils;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.JSONHelper;

import java.util.List;
import java.util.Map;

public class PostCreateActivity extends BaseTest {
    @Test(priority = 3, description = "TC-INT-SA-001 : User Can Create Sport Activity With Valid Data")
    public void userCanCreateSportActivityWithValidData() {
        // Get token
        String token = JSONHelper.getJSONValueByKey("token", "token.json");

        // Get dataset from CSV
        SportActivityBody sportActivityBody = new SportActivityBody();
        List<JSONObject> dataset = sportActivityBody.createSportActivityValidData("sport-activity");

        // Loop dataset
        for (JSONObject payload : dataset) {
            // Hit endpoint & calculate response time
            long startTime = System.currentTimeMillis();
            Response response = TestUtils.templateResponsePost(
                    "v1/sport-activities/create",
                    200,
                    "Post Create Sport Activity",
                    payload,
                    token
            );
            long responseTime = System.currentTimeMillis() - startTime;
            System.out.println("check");
            System.out.println(response.asPrettyString());

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
            List<String> stringFields = List.of(
                    "sport_category_id", "title", "description", "address", "map_url", "activity_date", "start_time",
                    "end_time", "created_at", "updated_at"
            );
            List<String> intFields = List.of("id", "city_id", "price", "slot", "user_id");
            List<String> nullableFields = List.of("price_discount");
            TestUtils.validateColumn(data, stringFields, "string", false);
            TestUtils.validateColumn(data, intFields, "number", false);
            TestUtils.validateColumn(data, nullableFields, "number", true);

            // Validate request and response match all keys
            for (Object keyObj : payload.keySet()) {
                String key = (String) keyObj;

                Assert.assertEquals(data.get(key), payload.get(key), "Mismatch at key: " + key);
            }

            // Extract & store id
            String activityId = res.getString("result.id");
            JSONHelper.saveToJSON("id", activityId, "sport-activity.json");

            // Validate performance (response time)
            Assert.assertTrue(responseTime < 2000, "Response time exceeds 2000ms");
        }
    }
}
