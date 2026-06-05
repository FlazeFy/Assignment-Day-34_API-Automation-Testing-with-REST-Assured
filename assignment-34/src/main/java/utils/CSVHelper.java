package utils;

import org.json.simple.JSONObject;

import java.io.*;
import java.util.*;

public class CSVHelper {
    public static List<JSONObject> getDataset(String fileName) {
        List<JSONObject> dataset = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("src/resources/datasets/" + fileName + ".csv"))) {
            String line;
            String[] headers = null;

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");

                // First row = header
                if (headers == null) {
                    headers = values;
                    continue;
                }

                JSONObject row = new JSONObject();
                for (int i = 0; i < values.length; i++) {
                    String key;

                    // Single column case
                    if (headers.length == 1 && headers[0].trim().isEmpty()) {
                        key = "col" + (i + 1);
                    } else {
                        key = (i < headers.length) ? headers[i].trim() : "col" + (i + 1);
                    }

                    row.put(key, values[i].trim());
                }

                dataset.add(row);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read dataset from CSV: " + fileName, e);
        }

        return dataset;
    }
}

