package utils;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

public class TokenHelper {
    public static String getToken() {
        try (FileReader reader = new FileReader("src/resources/json/token.json")) {
            JSONParser parser = new JSONParser();
            JSONObject tokenJson = (JSONObject) parser.parse(reader);
            return (String) tokenJson.get("token");

        } catch (IOException | ParseException e) {
            throw new RuntimeException("Failed to read token from token.json", e);
        }
    }
}

