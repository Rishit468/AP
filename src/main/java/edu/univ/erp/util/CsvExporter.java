package edu.univ.erp.util;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CsvExporter {

    /**
     * Exports a list of string arrays (each representing one row) to a CSV file.
     *
     * @param headers  Array of column headers.
     * @param rows     List of string arrays for each row.
     * @param filePath Full output file path (e.g., "exports/students.csv").
     * @return true if export succeeded.
     */
    public static boolean exportToCsv(String[] headers, List<String[]> rows, String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {

            // Write headers
            writer.append(String.join(",", headers)).append("\n");

            // Write rows
            for (String[] row : rows) {
                for (int i = 0; i < row.length; i++) {
                    writer.append(escapeCsv(row[i]));
                    if (i < row.length - 1) writer.append(",");
                }
                writer.append("\n");
            }

            writer.flush();
            System.out.println("✅ CSV exported: " + filePath);
            return true;

        } catch (IOException e) {
            System.err.println("❌ Error exporting CSV: " + e.getMessage());
            return false;
        }
    }

    /**
     * Escapes special characters for valid CSV format.
     */
    private static String escapeCsv(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            value = value.replace("\"", "\"\"");
            return "\"" + value + "\"";
        }
        return value;
    }
}
