package tests.sportcategory;

import core.TestUtils;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.JSONHelper;

import java.util.List;
import java.util.Map;

public class GetCategory {
    @Test(priority = 2, description = "TC-INT-SC-007 : User Can See Sport Category With Valid Data")
    public void userCanSeeSportCategoryWithValidData() {
        // Get token
        String token = JSONHelper.getJSONValueByKey("token", "token.json");
        boolean isPaginate = true;
        int perPage = 10;
        int page = 1;

        // Hit endpoint & calculate response time
        long startTime = System.currentTimeMillis();
        Response response = TestUtils.templateResponseGet(
                "v1/sport-categories?is_paginate="+isPaginate+"&per_page="+perPage+"&page="+page,
                200,
                "Get Sport Category",
                token);
        long responseTime = System.currentTimeMillis() - startTime;

        // Parse response
        JsonPath res = response.jsonPath();

        // Validate top-level key result type and value
        Map<String, Object> data = res.get("result");

        Assert.assertFalse(
                (Boolean) res.get("error"),
                "error is not false");
        Assert.assertFalse(
                data instanceof java.util.List,
                "result should not be an array");

        // Validate body.result object (pagination props)
        List<String> stringPaginationFields = List.of("path");
        List<String> stringNullablePaginationFields = List.of("next_page_url", "prev_page_url");
        List<String> intPaginationFields = List.of("per_page","to","total","current_page");
        TestUtils.validateColumn(data, stringPaginationFields, "string", false);
        TestUtils.validateColumn(data, stringNullablePaginationFields, "string", true);
        TestUtils.validateColumn(data, intPaginationFields, "number", false);

        // Validate body.result.data array
        Assert.assertTrue(
                data.get("data") instanceof java.util.List,
                "result should be an array");
        List<String> stringFields = List.of("name", "created_at", "updated_at");
        List<String> intFields = List.of("id");
        TestUtils.validateColumn(data.get("data"), stringFields, "string", false);
        TestUtils.validateColumn(data.get("data"), intFields, "number", false);

        // Validate performance (response time)
        Assert.assertTrue(responseTime < 2000, "Response time exceeds 2000ms");
    }
}
