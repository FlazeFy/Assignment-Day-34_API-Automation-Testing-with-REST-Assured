package body.sportcategory;

import org.json.simple.JSONObject;

public class SportCategoryBody {
    public JSONObject createSportCategoryData(String name) {
        JSONObject body = new JSONObject();
        body.put("name", name);
        return body;
    }

    public JSONObject updateSportCategoryData(String name) {
        JSONObject body = new JSONObject();
        body.put("name", name);
        return body;
    }
}