import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

class CSVBuilder {
    String fileName;
    HashMap<String, ArrayList<Double>> columns;
    String dirpath = null;
    String csvString;

    static void buildJoinedCsv(String fileName, ArrayList<CSVBuilder> csvBuilders) {
        ArrayList<String> csvStrings = new ArrayList<>();
        for (CSVBuilder csvBuilder : csvBuilders) {
            csvStrings.add(csvBuilder.csvString);
        }
        String joinedCsvString = String.join("\n", csvStrings);
        CSVBuilder joinedBuileder = new CSVBuilder(fileName, joinedCsvString);
        joinedBuileder.saveCsvString();
    }

    CSVBuilder(String fileName, HashMap<String, ArrayList<Double>> columns) {
        this.fileName = fileName;
        this.columns = columns;
        setCsvString();
    }

    CSVBuilder(String fileName, String csvString) {
        this.csvString = csvString;
        this.fileName = fileName;
    }

    private void setCsvString() {
        TreeMap<Integer, ArrayList<String>> rowValuesByIndex = generateCsvTreeMap();
        String csvString = convertTreeMapToCsvString(rowValuesByIndex);
        this.csvString = csvString;
    }

    private TreeMap<Integer, ArrayList<String>> generateCsvTreeMap() {
        TreeMap<Integer, ArrayList<String>> rowValuesByIndex = new TreeMap<>();
        rowValuesByIndex.computeIfAbsent(0, k -> new ArrayList<>()).add("Line Number");
        for (String columnName : new String[]{
                "Dist from rim","Li7","Na23","Mg24","Al27",
                "Si29","P31","K39","Ca43","Ca44","Ti47",
                "Mn55","Fe57","Cu65","Ga71","Sr88","Y89",
                "Ba138","Ce140","Pb208"}) {
            rowValuesByIndex.computeIfAbsent(0, k -> new ArrayList<>()).add(columnName);
            ArrayList<Double> columnValues = columns.get(columnName);
            //System.out.println(fileName + ": " + columns.size());
            for (int i = 0 ; i < columnValues.size() ; i++) {
                int rowIndex = i + 1;
                String printedValue = leaveTwoDecimals(columnValues.get(i).toString());
                if (columnName.equals("Dist from rim")) {
                    rowValuesByIndex.computeIfAbsent(rowIndex, k -> new ArrayList<>()).add(fileName.replaceAll("\\.csv", ""));
                }
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

    void saveCsvString(){
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
