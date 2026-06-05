package body.auth;

import org.json.simple.JSONObject;
import utils.ConfigReader;

public class PostLoginBody {
    public JSONObject loginValidData(){
        JSONObject loginBody = new JSONObject();

        loginBody.put("email", ConfigReader.getProperty("email"));
        loginBody.put("password", ConfigReader.getProperty("password"));

        return loginBody;
    }

    public JSONObject loginInvalidUnregisteredData(){
        JSONObject loginBody = new JSONObject();

        loginBody.put("email", ConfigReader.getProperty("emailUnregistered"));
        loginBody.put("password", ConfigReader.getProperty("password"));

        return loginBody;
    }
}