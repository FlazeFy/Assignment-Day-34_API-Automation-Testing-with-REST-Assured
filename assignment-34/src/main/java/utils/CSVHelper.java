package utils;

import org.json.simple.JSONObject;

import java.io.*;
import java.util.*;

public class CSVHelper {
    public static List<JSONObject> getDataset(String fileName) {
        List<JSONObject> dataset = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(
            new FileReader("src/resources/datasets/" + fileName + ".csv"))) {

            String line;
            String[] headers = null;

            while ((line = br.readLine()) != null) {
                // Handle CSV
                List<String> valuesList = parseCSVLine(line);
                String[] values = valuesList.toArray(new String[0]);

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

                    row.put(key, values[i].trim().replace("\"", ""));
                }

                dataset.add(row);
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to read dataset from CSV: " + fileName, e);
        }

        return dataset;
    }

    private static List<String> parseCSVLine(String line) {
        List<String> result = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (char c : line.toCharArray()) {
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                result.add(current.toString());
                current.setLength(0);
            } else {
                current.append(c);
            }
        }

        result.add(current.toString());
        return result;
    }
}

