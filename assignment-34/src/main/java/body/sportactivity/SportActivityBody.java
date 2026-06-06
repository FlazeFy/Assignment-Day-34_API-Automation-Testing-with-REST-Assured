package body.sportactivity;

import org.json.simple.JSONObject;
import utils.CSVHelper;
import utils.JSONHelper;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

public class SportActivityBody {
    public List<JSONObject> createSportActivityValidData(String fileName) {
        // Call dataset from CSV
        List<JSONObject> dataset = CSVHelper.getDataset(fileName);

        // Add dynamic future activity_date
        Random random = new Random();

        for (JSONObject row : dataset) {
            // Take category id
            String id = JSONHelper.getJSONValueByKey("id", "sport-category.json");

            // Add required static fields
            row.put("sport_category_id", id);
            row.put("city_id", 3172);

            // Convert int
            try {
                row.put("slot", Integer.parseInt(row.get("slot").toString()));
            } catch (Exception e) {
                row.put("slot", 0);
            }

            try {
                row.put("price", Integer.parseInt(row.get("price").toString()));
            } catch (Exception e) {
                row.put("price", 0);
            }

            // Get random days
            int randomDays = 50 + random.nextInt(51); // 50 - 100 days
            LocalDate futureDate = LocalDate.now().plusDays(randomDays);
            row.put("activity_date", futureDate.toString());
        }

        return dataset;
    }
}