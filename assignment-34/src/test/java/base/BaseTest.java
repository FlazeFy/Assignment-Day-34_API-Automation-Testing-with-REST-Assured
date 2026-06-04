package base;

import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import utils.ConfigReader;

public class BaseTest {
    @BeforeClass
    public void setUp() {
        String baseUrl = ConfigReader.getProperty("baseUrl");
        RestAssured.baseURI = baseUrl;

        RestAssured.filters(
            new RequestLoggingFilter(),
            new ResponseLoggingFilter()
        );
    }
}