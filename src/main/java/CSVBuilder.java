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
        for (String columnName : new String[]{
                "Dist from rim","Li7","Na23","Mg24","Al27",
                "Si29","P31","K39","Ca43","Ca44","Ti47",
                "Mn55","Fe57","Cu65","Ga71","Sr88","Y89",
                "Ba138","Ce140","Pb208"}) {
            rowValuesByIndex.computeIfAbsent(0, k -> new ArrayList<>()).add(columnName);
            ArrayList<Double> columnValues = columns.get(columnName);
            for (int i = 0 ; i < columnValues.size() ; i++) {
                int rowIndex = i + 1;
                String printedValue = leaveTwoDecimals(columnValues.get(i).toString());
                rowValuesByIndex.computeIfAbsent(rowIndex, k -> new ArrayList<>()).add(printedValue);
            }
        }
        return rowValuesByIndex;
    }

    private String leaveTwoDecimals(String decimalValue) {
        String[] partedValues = decimalValue.split("\\.");
        if (!decimalValue.contains(".") || partedValues[1].length() < 2) {
            return decimalValue;
        }
        return partedValues[0] + "." + partedValues[1].substring(0, 2);
    }

    private String convertTreeMapToCsvString(TreeMap<Integer, ArrayList<String>> rowValuesByIndex) {
        String csvString = "";
        for (Integer rowIndex : rowValuesByIndex.keySet()) {
            ArrayList<String> rowValues = rowValuesByIndex.get(rowIndex);
            csvString += String.join( ",", rowValues) + "\n";
        }
        return csvString;
    }

    private void saveCsvString(String csvString){
        try {
            FileWriter fileWriter = new FileWriter(getDirPath() + fileName + ".csv", false);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.print(csvString);
            printWriter.close();
        } catch (IOException e){

        }
    }

    private String getDirPath() {
        if (dirpath == null) {
            dirpath = new IO().getDirPath("csv-out", 0);
        }
        return dirpath;
    }
}
