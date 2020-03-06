import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.Styler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class CompiledProfile {
    String csvFileName;
    String[] csvData;
    ArrayList<String> lineNumbers = new ArrayList<>();

    CompiledProfile(String csvFileAddress) {
        this.csvFileName = csvFileAddress.split("/")[2];
        csvData = readCsv(csvFileAddress);
        setLineNumbers();
    }

    private void setLineNumbers() {
        System.out.println("\n" + csvFileName);
        for (int i = 1 ; i < csvData.length ; i++) {
            String[] rowData = csvData[i].split(",");
            String lineNumber = rowData[0];
            if (!lineNumbers.contains(lineNumber)) {
                lineNumbers.add(lineNumber);
                System.out.println(lineNumber);
            }
        }
    }

    void buildChartsForAllElements() {
        String[] headerValues = getRowData(0);
        for (int i = 2 ; i < headerValues.length ; i++) {
            buildChart(headerValues[i]);
        }
    }

    void buildChart(String elementName) {
        String title = csvFileName + " - " + elementName;
        XYChart chart = new XYChartBuilder().width(2000).height(800).title(title).xAxisTitle("Distance From Rim, mkm").yAxisTitle(elementName + ", Concentration").build();

        chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Scatter);
        chart.getStyler().setLegendPosition(Styler.LegendPosition.OutsideE);
        chart.getStyler().setMarkerSize(6);

        for (String lineNumber : lineNumbers) {
            ArrayList<String> lineRows = getLineRows(lineNumber);
            chart.addSeries(lineNumber, getColumnValues("Dist from rim", lineRows), getColumnValues(elementName, lineRows));
        }
        try {
            BitmapEncoder.saveBitmap(chart,new IO().getDirPath("charts", 0) + title, BitmapEncoder.BitmapFormat.GIF);
        } catch (IOException e) {

        }
    }

    private ArrayList<String> getLineRows(String lineNumber) {
        ArrayList<String> lineRows = new ArrayList<>();
        for (String row : csvData) {
            if (row.contains(lineNumber)) {
                lineRows.add(row);
            }
        }
        return lineRows;
    }

    private ArrayList<Double> getColumnValues(String columnName, ArrayList<String> rows) {
        ArrayList<Double> columnValues = new ArrayList<>();
        int columnIndex = getColumnIndex(columnName);
        for (String row : rows) {
            String value = row.split(",")[columnIndex];
            columnValues.add(Double.valueOf(value));
        }
        return columnValues;
    }

    private int getColumnIndex(String columnName) {
        String[] headerValues = getRowData(0);
        for (int i = 0 ; i < headerValues.length ; i++) {
            if (headerValues[i].equals(columnName)) {
                return i;
            }
        }
        return 0;
    }

    private String[] getRowData(int rowNumber) {
        return csvData[rowNumber].split(",");
    }

    private String[] readCsv(String csvFileAddress) {
        try {
            String csvContent = new String(Files.readAllBytes(Paths.get(csvFileAddress)));
            return csvContent.split("\n");
        } catch (IOException e){
            return null;
        }
    }
}
