package core;

import io.restassured.response.Response;
import org.testng.Assert;
import java.util.List;
import java.util.Map;
import static io.restassured.RestAssured.given;

public class TestUtils {
    public static void validateColumn(Object data, List<String> fields, String dataType, boolean nullable) {
        // Convert object to list
        List<Map<String, Object>> dataArray;
        dataArray = data instanceof List ? (List<Map<String, Object>>) data : List.of((Map<String, Object>) data);

        // Loop item
        for (Map<String, Object> item : dataArray) {
            // Validate object
            Assert.assertNotNull(item);

            // Loop fields
            for (String field : fields) {
                // Validate field exists
                Assert.assertTrue(item.containsKey(field), "Missing field: " + field);

                Object value = item.get(field);

                // Nullable validation
                if (nullable && value == null) {
                    Assert.assertNull(value);
                    continue;
                }

                // Validate datatype
                switch (dataType) {
                    case "string":
                        Assert.assertTrue(value instanceof String, field + " is not String");
                        break;

                    case "number":
                        Assert.assertTrue(value instanceof Number, field + " is not Number");

                        // Validate integer or decimal
                        if (value instanceof Integer || value instanceof Long) {
                            Assert.assertEquals(((Number) value).doubleValue() % 1, 0.0);
                        } else {
                            Assert.assertNotEquals(((Number) value).doubleValue() % 1, 0.0);
                        }
                        break;

                    default:
                        Assert.fail("Unsupported data type: " + dataType);
                }
            }
        }
    }

    public static Response templateResponsePost(String endpoint, int expectedStatusCode, String endpointName, Object payload, String token) {
        String contentType = "application/json";
        Response response;

        if (token != null && payload != null) {
            response = given()
                    .contentType(contentType)
                    .header("Authorization", "Bearer " + token)
                    .body(payload)
                    .when()
                    .post(endpoint)
                    .then()
                    .statusCode(expectedStatusCode)
                    .extract().response();
        } else if (token != null) {
            response = given()
                    .contentType(contentType)
                    .header("Authorization", "Bearer " + token)
                    .when()
                    .post(endpoint)
                    .then()
                    .statusCode(expectedStatusCode)
                    .extract().response();
        } else if (payload != null) {
            response = given()
                    .contentType(contentType)
                    .body(payload)
                    .when()
                    .post(endpoint)
                    .then()
                    .statusCode(expectedStatusCode)
                    .extract().response();
        } else {
            response = given()
                    .contentType(contentType)
                    .when()
                    .post(endpoint)
                    .then()
                    .statusCode(expectedStatusCode)
                    .extract().response();
        }

        System.out.println("==== POST : " + endpointName + " ====");
        System.out.println("Status Code : " + response.getStatusCode());
        System.out.println("Response : ");
        System.out.println(response.asPrettyString());

        return response;
    }

    public static Response templateResponseDelete(String endpoint, int expectedStatusCode, String endpointName, String token) {
        String contentType = "application/json";
        Response response;

        response = given()
                .contentType(contentType)
                .header("Authorization", "Bearer " + token)
                .when()
                .delete(endpoint)
                .then()
                .statusCode(expectedStatusCode)
                .extract().response();

        System.out.println("==== DELETE : " + endpointName + " ====");
        System.out.println("Status Code : " + response.getStatusCode());
        System.out.println("Response : ");
        System.out.println(response.asPrettyString());

        return response;
    }
}
