package tests.transaction;

import core.TestUtils;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.JSONHelper;

import java.util.List;
import java.util.Map;

public class GetAllTransaction {
    public void validateResponseItem(List<Map<String, Object>> data) {
        List<String> stringFields = List.of("invoice_id","status","order_date","expired_date","created_at","updated_at","username");
        List<String> stringNullableFields = List.of("proof_payment_url");
        List<String> intFields = List.of("id","user_id","payment_method_id","total_amount");
        TestUtils.validateColumn(data, stringFields, "string", false);
        TestUtils.validateColumn(data, stringNullableFields, "string", true);
        TestUtils.validateColumn(data, intFields, "number", false);

        // Validate body.result.data[].transaction_items object
        for (Map<String, Object> transactionData : data) {
            Map<String, Object> transaction = (Map<String, Object>) transactionData.get("transaction_items");
            Assert.assertNotNull(transaction, "transaction_items should not be null");

            Assert.assertFalse(
                    transaction instanceof java.util.List,
                    "transaction_items should not be an array");
            List<String> stringTransactionFields = List.of("title", "created_at", "updated_at");
            List<String> intTransactionFields = List.of("id", "transaction_id", "sport_activity_id", "price");
            List<String> intTransactionNullableFields = List.of("price_discount");
            TestUtils.validateColumn(transaction, stringTransactionFields, "string", false);
            TestUtils.validateColumn(transaction, intTransactionFields, "number", false);
            TestUtils.validateColumn(transaction, intTransactionNullableFields, "number", true);

            // Validate body.result.data[].transaction_items.sport_activities object
            Map<String, Object> sportActivities =
                    (Map<String, Object>) transaction.get("sport_activities");
            Assert.assertNotNull(sportActivities, "sport_activities should not be null");
            Assert.assertFalse(
                    sportActivities instanceof java.util.List,
                    "sport_activities should not be an array");

            List<String> stringSportActivityFields = List.of(
                    "title", "description", "address", "map_url", "activity_date", "start_time", "end_time", "created_at", "updated_at"
            );
            List<String> stringSportActivityNullableFields = List.of("image_url");
            List<String> intSportActivityFields = List.of("id", "sport_category_id", "city_id", "user_id", "price", "slot");
            List<String> intSportActivityNullableFields = List.of("price_discount");

            TestUtils.validateColumn(sportActivities, stringSportActivityFields, "string", false);
            TestUtils.validateColumn(sportActivities, stringSportActivityNullableFields, "string", true);
            TestUtils.validateColumn(sportActivities, intSportActivityFields, "number", false);
            TestUtils.validateColumn(sportActivities, intSportActivityNullableFields, "number", true);
        }
    }

    public void validatePaginationProps(Map<String, Object> result) {
        Assert.assertFalse(
                result instanceof java.util.List,
                "result should not be an array");

        // Validate body.result object (pagination props)
        List<String> stringPaginationFields = List.of("path","first_page_url","last_page_url");
        List<String> stringNullablePaginationFields = List.of("next_page_url", "prev_page_url");
        List<String> intPaginationFields = List.of("per_page","total","current_page");
        List<String> intNullablePaginationFields = List.of("to");
        TestUtils.validateColumn(result, stringPaginationFields, "string", false);
        TestUtils.validateColumn(result, stringNullablePaginationFields, "string", true);
        TestUtils.validateColumn(result, intPaginationFields, "number", false);
        TestUtils.validateColumn(result, intNullablePaginationFields, "number", true);
    }

    @Test(priority = 3, description = "TC-INT-TR-001 : User Can See All Transaction Valid Data With Pagination")
    public void userCanSeeAllTransactionValidDataWithPagination() {
        // Get token
        String token = JSONHelper.getJSONValueByKey("token", "token.json");
        boolean isPaginate = true;
        int perPage = 10;
        int page = 1;

        // Hit endpoint & calculate response time
        long startTime = System.currentTimeMillis();
        Response response = TestUtils.templateResponseGet(
                "v1/all-transaction?is_paginate="+isPaginate+"&per_page="+perPage+"&page="+page,
                200,
                "Get All Transaction",
                token);
        long responseTime = System.currentTimeMillis() - startTime;

        // Parse response
        JsonPath res = response.jsonPath();

        // Validate top-level key result type and value
        Map<String, Object> result = res.get("result");
        Assert.assertFalse(
                (Boolean) res.get("error"),
                "error is not false");

        // Validate body.result object (pagination props)
        validatePaginationProps(result);

        // Validate body.result.data array
        List<Map<String, Object>> data = res.get("result.data");
        Assert.assertNotNull(data, "data should not be empty");

        // Template validate item (All Transaction)
        validateResponseItem(data);

        // Validate performance (response time)
        Assert.assertTrue(responseTime < 2000, "Response time exceeds 2000ms");
    }

    @Test(priority = 3, description = "TC-INT-TR-002 : User Can See All Transaction Valid Data Without Pagination")
    public void userCanSeeAllTransactionValidDataWithoutPagination() {
        // Get token
        String token = JSONHelper.getJSONValueByKey("token", "token.json");
        boolean isPaginate = false;

        // Hit endpoint & calculate response time
        long startTime = System.currentTimeMillis();
        Response response = TestUtils.templateResponseGet(
                "v1/all-transaction?is_paginate="+isPaginate,
                200,
                "Get All Transaction",
                token);
        long responseTime = System.currentTimeMillis() - startTime;

        // Parse response
        JsonPath res = response.jsonPath();

        // Validate top-level key result type and value
        List<Map<String, Object>> result = res.get("result");
        Assert.assertFalse((Boolean) res.get("error"), "error is not false");
        // Validate body.result[] array
        Assert.assertNotNull(result, "result should not be empty");

        // Template validate item (All Transaction)
        validateResponseItem(result);

        // Validate performance (response time)
        Assert.assertTrue(responseTime < 5000, "Response time exceeds 2000ms");
    }

    @Test(priority = 3, description = "TC-INT-TR-003 : User Can See All Transaction Empty Data With Invalid Search And Without Pagination")
    public void userCanSeeAllTransactionEmptyDataWithInvalidSearchAndWithoutPagination() {
        // Get token
        String token = JSONHelper.getJSONValueByKey("token", "token.json");
        String search = "XXXXXX";
        boolean isPaginate = false;

        // Hit endpoint & calculate response time
        long startTime = System.currentTimeMillis();
        Response response = TestUtils.templateResponseGet(
                "v1/all-transaction?is_paginate="+isPaginate+"&search="+search,
                200,
                "Get All Transaction",
                token);
        long responseTime = System.currentTimeMillis() - startTime;

        // Parse response
        JsonPath res = response.jsonPath();

        // Validate top-level key result type and value
        List<Map<String, Object>> result = res.get("result");
        Assert.assertFalse((Boolean) res.get("error"), "error is not false");
        // Validate body.result[] array
        Assert.assertEquals(result.size(), 0, "result should be empty");

        // Validate performance (response time)
        Assert.assertTrue(responseTime < 5000, "Response time exceeds 2000ms");
    }

    @Test(priority = 4, description = "TC-INT-TR-004 : User Can See All Transaction Empty Data With Invalid Search And With Pagination")
    public void userCanSeeAllTransactionEmptyDataWithInvalidSearchAndWithPagination() {
        // Get token
        String token = JSONHelper.getJSONValueByKey("token", "token.json");
        String search = "XXXXXX";
        boolean isPaginate = true;
        int perPage = 10;
        int page = 1;

        // Hit endpoint & calculate response time
        long startTime = System.currentTimeMillis();
        Response response = TestUtils.templateResponseGet(
                "v1/all-transaction?is_paginate="+isPaginate+"&per_page="+perPage+"&page="+page+"&search="+search,
                200,
                "Get All Transaction",
                token);
        long responseTime = System.currentTimeMillis() - startTime;

        // Parse response
        JsonPath res = response.jsonPath();

        // Validate top-level key result type and value
        Map<String, Object> result = res.get("result");
        Assert.assertFalse(
                (Boolean) res.get("error"),
                "error is not false");

        // Validate body.result object (pagination props)
        validatePaginationProps(result);

        // Validate body.result.data array
        List<Map<String, Object>> data = res.get("result.data");
        Assert.assertEquals(data.size(), 0, "data should be empty");

        // Validate performance (response time)
        Assert.assertTrue(responseTime < 5000, "Response time exceeds 2000ms");
    }
}
