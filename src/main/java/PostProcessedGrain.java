import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

class PostProcessedGrain {
    String[] csvData;
    ArrayList<PostProcessedRow> postProcessedRows = new ArrayList<>();
    String dirpath = null;
    String csvString = "Line Number,Dist from rim,Li7,Na23,Mg24,Al27,Si29,P31,K39,Ca43,Ca44,Ti47,Mn55,Fe57,Cu65,Ga71,Sr88,Y89,Ba138,Ce140,Pb208,SiO2,Al2O3,FeO,CaO,Na2O,K2O,MgO,SrO,P2O5,MnO,TiO2,An";
    String[] columnNames = csvString.split(",");

    PostProcessedGrain(String csvFileAddress) {
        System.out.println(csvFileAddress);
        csvData = readCsv(csvFileAddress);
        for (String row : csvData) {
            PostProcessedRow postProcessedRow = new PostProcessedRow(row);
            postProcessedRows.add(postProcessedRow);
        }
        Collections.sort(postProcessedRows);
    }

    void buildMedianSlidingWindow(int window, String fileName) {
        HashMap<String, ArrayList<Double>> valuesByColumnName = getColumnsMap();
        valuesByColumnName = fillMapWithMedianValues(valuesByColumnName, window);
        buildMedianCsv(valuesByColumnName, fileName);
    }

    private HashMap<String, ArrayList<Double>> getColumnsMap()  {
        HashMap<String, ArrayList<Double>> valuesByColumnName = new HashMap<>();
        for (PostProcessedRow postProcessedRow : postProcessedRows) {
            if (postProcessedRow.isNotValueRow) {
                continue;
            }
            String[] rowValues = postProcessedRow.fullRow.split(",");
            for (int i = 1 ; i < columnNames.length ; i++) {
                String elementName = columnNames[i];
                Double value = Double.valueOf(rowValues[i]);
                valuesByColumnName.computeIfAbsent(elementName, k -> new ArrayList<>()).add(value);
            }
        }
        return valuesByColumnName;
    }

    private HashMap<String, ArrayList<Double>> fillMapWithMedianValues(HashMap<String, ArrayList<Double>> valuesByColumnName, int window) {
        for (String columnName : valuesByColumnName.keySet()) {
            if (columnName == "Dist from rim") {
                continue;
            }
            Double[] values = valuesByColumnName.get(columnName).toArray(new Double[0]);
            ArrayList<Double> medianValues = GFG.findMedian(values, window);
            valuesByColumnName.put(columnName, medianValues);
        }
        return valuesByColumnName;
    }

    void buildMedianCsv(HashMap<String, ArrayList<Double>> valuesByColumnName, String fileName) {
        Integer columnSize = valuesByColumnName.get("Dist from rim").size();
        ArrayList<String> rows = new ArrayList<>();
        for (int i = -1 ; i < columnSize ; i++) {
            ArrayList<String> rowValues = new ArrayList<>();
            for (String columnName : columnNames) {
                if (i == -1) {
                    rowValues.add(columnName);
                    continue;
                }
                ArrayList<Double> columnValues = valuesByColumnName.get(columnName);
                if (columnValues == null) {
                    rowValues.add(fileName);
                    continue;
                }
                rowValues.add(String.valueOf(columnValues.get(i)));

            }
            rows.add(String.join(",", rowValues));
        }
        try {
            FileWriter fileWriter = new FileWriter(getDirPath() + fileName + ".csv", false);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.print(String.join("\n", rows));
            printWriter.close();
        } catch (IOException e){

        }
    }

    String[] readCsv(String csvFileAddress) {
        try {
            String csvContent = new String(Files.readAllBytes(Paths.get(csvFileAddress)));
            return csvContent.split("\n");
        } catch (IOException e){
            return null;
        }
    }

    void buildCsv(String fileName) {
        for (PostProcessedRow postProcessedRow : postProcessedRows) {
            if (postProcessedRow.isNotValueRow) {
                continue;
            }
            csvString += "\n" + postProcessedRow.fullRow;
        }
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

    void printRimValues() {
        String elementName = "Na23";
        Double sum = 0.0;
        int count = 0;
        int distanceFromRim = 10;
        for (PostProcessedRow processedRow : postProcessedRows) {
            if (processedRow.distanceFromRim > distanceFromRim) {
                continue;
            }
            String ppmValue = processedRow.ppmValueByElementName.get(elementName);
            if (ppmValue == null) {
                continue;
            }
            sum += Double.valueOf(ppmValue);
            count++;
        }
        Double average = sum / count;
        System.out.println( "For " + elementName + " average rim (in " + distanceFromRim + "mkm) value is " + average + " ppm for " + count + " measures");
    }
}
