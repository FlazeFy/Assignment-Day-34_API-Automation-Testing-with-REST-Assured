package utils;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class JSONHelper {
    public static String getJSONValueByKey(String key, String fileName) {
        try (FileReader reader = new FileReader("src/resources/json/"+fileName)) {
            JSONParser parser = new JSONParser();
            JSONObject tokenJson = (JSONObject) parser.parse(reader);

            return (String) tokenJson.get(key);
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static void saveToJSON(String key, String val, String fileName) {
        try (FileWriter file = new FileWriter("src/resources/json/"+fileName)){
            System.out.println(key + " : " + val);

            JSONObject tokenJson = new JSONObject();
            tokenJson.put(key, val);

            file.write(tokenJson.toJSONString());
            file.flush();

            System.out.println(key+" saved to "+fileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}

