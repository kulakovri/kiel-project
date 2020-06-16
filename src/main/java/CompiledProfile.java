import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.Styler;

import java.awt.*;
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

    void buildChartsWithinRange(int min, int max) {
        String[] headerValues = getRowData(0);
        for (int i = 2 ; i < headerValues.length ; i++) {
            String elementName = headerValues[i];
            String title = elementName + " - " + csvFileName;
            XYChart chart = new XYChartBuilder().width(2000).height(800).title(title).xAxisTitle("Distance From Rim, mkm").yAxisTitle(elementName + ", Concentration").build();

            chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Scatter);
            chart.getStyler().setMarkerSize(6);
            chart.getStyler().setBaseFont(new Font("Serif", Font.PLAIN, 20));

            // chart.getStyler().setYAxisMax(getYAxisMax(elementName));

            for (String lineNumber : lineNumbers) {
                ArrayList<String> lineRows = getLineRows(lineNumber);
                try {
                    chart.addSeries(lineNumber, getColumnValuesWithinRange("Dist from rim", lineRows, min, max), getColumnValuesWithinRange(elementName, lineRows, min, max));
                } catch (Exception e) {

                }
            }
            try {
                BitmapEncoder.saveBitmap(chart,new IO().getDirPath("charts", 0) + title, BitmapEncoder.BitmapFormat.GIF);
            } catch (IOException e) {

            }
        }
    }

    void buildChartsExcludingRanges(int[][] ranges) {
        String[] headerValues = getRowData(0);
        for (int i = 2 ; i < headerValues.length ; i++) {
            String elementName = headerValues[i];
            String title = getChartTitle(elementName);
            String yAxisTitle = getYAxisTitle(elementName);
            XYChart chart = new XYChartBuilder().width(2000).height(800).title(title).xAxisTitle("Distance From Rim, mkm").yAxisTitle(yAxisTitle).build();

            chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Scatter);

            chart.getStyler().setAxisTickLabelsFont(new Font("Serif", Font.PLAIN, 30));
            chart.getStyler().setAxisTitleFont(new Font("Serif", Font.PLAIN, 30));
            chart.getStyler().setChartTitleFont(new Font("Serif", Font.BOLD, 36));
            chart.getStyler().setYAxisDecimalPattern("#");

            chart.getStyler().setMarkerSize(6);
            chart.getStyler().setLegendVisible(false);
            chart.getStyler().setSeriesColors(new Color[]{Color.BLACK});
            Color grey = new Color(220, 220, 220);
            chart.getStyler().setPlotBackgroundColor(grey);
            chart.getStyler().setChartBackgroundColor(grey);
            chart.getStyler().setPlotGridLinesColor(Color.BLACK);

            for (String lineNumber : lineNumbers) {
                ArrayList<String> lineRows = getLineRows(lineNumber);
                try {
                    chart.addSeries(lineNumber, getColumnValuesWithoutRange("Dist from rim", lineRows, ranges), getColumnValuesWithoutRange(elementName, lineRows, ranges));
                } catch (Exception e) {

                }
            }
            try {
                BitmapEncoder.saveBitmap(chart,new IO().getDirPath("charts", 0) + title, BitmapEncoder.BitmapFormat.GIF);
            } catch (IOException e) {

            }
        }
    }

    String getYAxisTitle(String elementName) {
        String yAxisTitle = "";
        if (elementName.contains("O")) {
            yAxisTitle += elementName + ", Concentration, %";
        } else if (elementName.equals("An")) {
            yAxisTitle += "An";
        } else {
            yAxisTitle += elementName.replaceAll("[0-9]", "") + ", Concentration, ppm";
        }
        return yAxisTitle;
    }

    String getChartTitle(String elementName) {
        String csvName = csvFileName;
        csvName = csvName.replaceAll("\\.csv", "");
        return elementName + " - " + csvName;
    }

    void buildChartsForAllElements() {
        String[] headerValues = getRowData(0);
        for (int i = 2 ; i < headerValues.length ; i++) {
            buildChart(headerValues[i]);
        }
    }

    void buildChart(String elementName) {
        String title = elementName + " - " + csvFileName;
        XYChart chart = new XYChartBuilder().width(2000).height(800).title(title).xAxisTitle("Distance From Rim, mkm").yAxisTitle(elementName + ", Concentration").build();

        chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Scatter);
        chart.getStyler().setLegendPosition(Styler.LegendPosition.OutsideE);
        chart.getStyler().setMarkerSize(6);

        // chart.getStyler().setYAxisMax(getYAxisMax(elementName));

        for (String lineNumber : lineNumbers) {
            ArrayList<String> lineRows = getLineRows(lineNumber);
            chart.addSeries(lineNumber, getColumnValues("Dist from rim", lineRows), getColumnValues(elementName, lineRows));
        }
        try {
            BitmapEncoder.saveBitmap(chart,new IO().getDirPath("charts", 0) + title, BitmapEncoder.BitmapFormat.GIF);
        } catch (IOException e) {

        }
    }

    private Double getYAxisMax(String elementName) {
        if (elementName.equals("TiO2")) {
            return 0.07;
        } else if (elementName.equals("MnO")) {
            return 0.06;
        } else if (elementName.equals("P2O5")) {
            return 0.25;
        } else if (elementName.equals("MgO")) {
            return 0.2;
        } else if (elementName.equals("K2O")) {
            return 0.7;
        } else if (elementName.equals("Na2O")) {
            return 10.0;
        } else if (elementName.equals("FeO")) {
            return 1.1;
        } else if (elementName.equals("Ce140")) {
            return 20.0;
        } else if (elementName.equals("Ba138")) {
            return 600.0;
        } else if (elementName.equals("Y89")) {
            return 2.0;
        } else if (elementName.equals("Cu65")) {
            return 20.0;
        } else if (elementName.equals("Fe57")) {
            return 10000.0;
        } else if (elementName.equals("Mn55")) {
            return 500.0;
        } else if (elementName.equals("Ti47")) {
            return 500.0;
        } else if (elementName.equals("K39")) {
            return 4000.0;
        } else if (elementName.equals("P31")) {
            return 1000.0;
        } else if (elementName.equals("Mg24")) {
            return 1000.0;
        } else if (elementName.equals("Na23")) {
            return 60000.0;
        } else {
            return 0.0;
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

    private ArrayList<Double> getColumnValuesWithinRange(String columnName, ArrayList<String> rows, int min, int max) {
        ArrayList<Double> columnValues = new ArrayList<>();
        int columnIndex = getColumnIndex(columnName);
        for (String row : rows) {
            Double distanceFromRim = Double.valueOf(row.split(",")[1]);
            if (distanceFromRim > min && distanceFromRim < max) {
                String value = row.split(",")[columnIndex];
                columnValues.add(Double.valueOf(value));
            }
        }
        return columnValues;
    }

    private ArrayList<Double> getColumnValuesWithoutRange(String columnName, ArrayList<String> rows, int[][] ranges) {
        ArrayList<Double> columnValues = new ArrayList<>();
        int columnIndex = getColumnIndex(columnName);
        for (String row : rows) {
            Double distanceFromRim = Double.valueOf(row.split(",")[1]);
            if (shouldExclude(ranges, distanceFromRim)) {
                continue;
            } else {
                String value = row.split(",")[columnIndex];
                columnValues.add(Double.valueOf(value));
            }
        }
        return columnValues;
    }

    private boolean shouldExclude(int[][] ranges, Double distanceFromRim) {
        if (ranges.length == 0) {
            return false;
        }
        for (int[] range : ranges) {
            int min = range[0];
            int max = range[1];
            if (distanceFromRim > min && distanceFromRim < max) {
                return true;
            }
        }
        return false;
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
