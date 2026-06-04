package body.auth;

import org.json.simple.JSONObject;
import utils.ConfigReader;

public class LoginBody {

    public JSONObject loginData(){
        JSONObject loginBody = new JSONObject();
        loginBody.put("email", ConfigReader.getProperty("email"));
        loginBody.put("password", ConfigReader.getProperty("password"));
        return loginBody;
    }
}