import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

class CSVBuilder {
    String fileName;
    HashMap<String, ArrayList<Double>> columns;
    String dirpath = null;

    CSVBuilder(String fileName, HashMap<String, ArrayList<Double>> columns) {
        this.fileName = fileName;
        this.columns = columns;
    }

    void buildCsvWithColumns() {
        TreeMap<Integer, ArrayList<String>> rowValuesByIndex = generateCsvTreeMap();
        String csvString = convertTreeMapToCsvString(rowValuesByIndex);
        saveCsvString(csvString);
    }

    private TreeMap<Integer, ArrayList<String>> generateCsvTreeMap() {
        TreeMap<Integer, ArrayList<String>> rowValuesByIndex = new TreeMap<>();
        for (String columnName : columns.keySet()) {
            rowValuesByIndex.computeIfAbsent(0, k -> new ArrayList<>()).add(columnName);
            ArrayList<Double> columnValues = columns.get(columnName);
            for (int i = 1 ; i < columnValues.size() ; i++) {
                int rowIndex = i + 1;
                String printedValue = columnValues.get(i).toString();
                rowValuesByIndex.computeIfAbsent(rowIndex, k -> new ArrayList<>()).add(printedValue);
            }
        }
        return rowValuesByIndex;
    }

    private String convertTreeMapToCsvString(TreeMap<Integer, ArrayList<String>> rowValuesByIndex) {
        String csvString = "";
        for (Integer rowIndex : rowValuesByIndex.keySet()) {
            ArrayList<String> rowValues = rowValuesByIndex.get(rowIndex);
            csvString += rowIndex + "," + String.join( ",", rowValues) + "\n";
        }
        return csvString;
    }

    void saveCsvString(String csvString){
        try {
            FileWriter fileWriter = new FileWriter(getDirPath() + fileName, false);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.print(csvString);
            printWriter.close();
        } catch (IOException e){

        }
    }

    String getDirPath() {
        if (dirpath == null) {
            dirpath = new IO().getDirPath("csv-out", 0);
        }
        return dirpath;
    }
}
