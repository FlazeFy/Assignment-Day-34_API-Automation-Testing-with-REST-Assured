package body.sportcategory;

import org.json.simple.JSONObject;
import utils.CSVHelper;
import utils.ConfigReader;

import java.util.List;

public class SportCategoryBody {
    public List<JSONObject> createSportCategoryValidData(String fileName) {
        return CSVHelper.getDataset(fileName);
    }

    public JSONObject createSportCategoryInvalidNameDataType() {
        JSONObject body = new JSONObject();

        int invalidName = Integer.parseInt(ConfigReader.getProperty("categoryNameInvalidDataType"));

        body.put("name", invalidName);

        return body;
    }

    public JSONObject updateSportCategoryData(String name) {
        JSONObject body = new JSONObject();

        body.put("name", name);

        return body;
    }
}