package tests.sportcategory;

import base.BaseTest;
import body.sportcategory.SportCategoryBody;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.TokenHelper;
import utils.Utils;

public class SportCategoryTest extends BaseTest {
    private String categoryId;

    @Test(priority = 2, description = "TC-INT-AU-002 : User Cant Login With Invalid Unregistered Account")
    public void createSportCategoryTest() {
        // Get token
        SportCategoryBody sportCategoryBody = new SportCategoryBody();
        String token = TokenHelper.getToken();
        String randomName = Utils.getCategoryName();

        // Hit endpoit
        Response response = RestAssured.given()
                .header("Authorization","Bearer " + token)
                .header("Content-Type","application/json")
                .body(sportCategoryBody.createSportCategoryData("12345").toString())
                .when()
                .post("v1/sport-categories/create")
                .then()
                .extract().response();

        categoryId = response.jsonPath().getString("result.id");
        Assert.assertNotNull(categoryId,"Category id is not to be null");
        System.out.println("Create Category Id");
    }

    @Test
    public void getSportCategoryTest() {

    }

    @Test
    public void updateSportCategoryTest() {

    }

    @Test
    public void deleteSportCategoryTest() {

    }

    //CRUD
}