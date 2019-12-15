import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.markers.SeriesMarkers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ProfileChart {
    String csvFileName;
    Profile profile;

    ProfileChart(String csvAddress) {
        Profile profile = new Profile(CSVLoader.loadCsv(csvAddress));
        this.profile = profile;
        this.csvFileName = csvAddress.substring(4);
    }

    void buildCpsChart(String elementName) {
        ArrayList<Double> cpsValues = profile.getSignalValuesByName(elementName);

        String title = csvFileName + " - " + elementName + " CPS";
        XYChart chart = new XYChart(2000, 800);
        chart.setTitle(title);
        chart.setYAxisTitle(elementName + " CPS");
        chart.addSeries("analyte", getXdataForYValues(cpsValues), cpsValues);
        chart.addSeries("non-analyte", profile.nonAnalyteValueIndexes, getCpsValuesForIndexes(cpsValues, profile.nonAnalyteValueIndexes)).setMarker(SeriesMarkers.CIRCLE).setLineWidth(1);
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNE);
        try {
            BitmapEncoder.saveBitmap(chart, getDirPath(0) + title, BitmapEncoder.BitmapFormat.GIF);
        } catch (IOException e) {

        }
    }

    private ArrayList<Integer> getXdataForYValues(ArrayList<Double> values) {
        ArrayList<Integer> xData = new ArrayList<>();
        for (int i = 1 ; i < values.size()+1 ; i++) {
            xData.add(i);
        }
        return xData;
    }

    private ArrayList<Double> getCpsValuesForIndexes(ArrayList<Double> cpsValues, ArrayList<Integer> indexes) {
        ArrayList<Double> selectedCpsValues = new ArrayList<>();
        for (Integer index : indexes) {
            selectedCpsValues.add(cpsValues.get(index));
        }
        return selectedCpsValues;
    }

    void buildPpmChart(ArrayList<String> standardCsvAddresses, String name) {
        ArrayList<Double> ppmValues = profile.calculatePpmValues(standardCsvAddresses, name);

        String title = csvFileName + " - " + name + " ppm";
        XYChart chart = new XYChart(2000, 800);
        chart.setTitle(title);
        chart.setYAxisTitle(name.replaceAll("[0-9]", "") + " ppm");
        chart.addSeries("analyte", null, ppmValues);
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNE);
        try {
            BitmapEncoder.saveBitmap(chart, getDirPath(0) + title.replaceAll("/", "-"), BitmapEncoder.BitmapFormat.PNG);
        } catch (IOException e) {

        }
    }

    void buildPpmRatioCharts(ArrayList<String> standardCsvAddresses, String secondElementName) {
        for (String firstElementName : profile.getIsotopeHeader()) {
            if (firstElementName.equals("Time [Sec]") || firstElementName.equals(secondElementName)) {
                continue;
            }
            buildPpmRatioChart(standardCsvAddresses, firstElementName, secondElementName);
        }

    }

    void buildPpmRatioChart(ArrayList<String> standardCsvAddresses, String firstElementName, String secondElementName) {
        ArrayList<Double> ratioValues = profile.getPpmRatioValues(standardCsvAddresses, firstElementName, secondElementName);
        String ratioName = firstElementName + "/" + secondElementName;
        String title = csvFileName + " - " + ratioName + " ratio";
        XYChart chart = new XYChart(2000, 800);
        chart.setTitle(title);
        chart.setYAxisTitle(ratioName.replaceAll("[0-9]", ""));
        chart.addSeries("analyte", null, ratioValues);
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNE);
        try {
            BitmapEncoder.saveBitmap(chart, getDirPath(0) + title.replaceAll("/", "-"), BitmapEncoder.BitmapFormat.PNG);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    void buildCPSRatioChart(String firstElementName, String secondElementName) {
        ArrayList<Double> firstCpsValues = profile.getSignalValuesByName(firstElementName);
        ArrayList<Double> secondCpsValues = profile.getSignalValuesByName(secondElementName);
        ArrayList<Double> ratioValues = getRatio(firstCpsValues, secondCpsValues);
        String ratioName = firstElementName + "/" + secondElementName;
        String title = csvFileName + " - " + ratioName + " cps ratio";
        XYChart chart = new XYChart(2000, 800);
        chart.setTitle(title);
        chart.setYAxisTitle(ratioName.replaceAll("[0-9]", ""));
        chart.addSeries("analyte", null, ratioValues);
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNE);
        try {
            BitmapEncoder.saveBitmap(chart, getDirPath(0) + title.replaceAll("/", "-"), BitmapEncoder.BitmapFormat.PNG);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static String transactionDirPath = null;

    static String getDirPath(int dirNumber) {
        if (transactionDirPath != null) {
            return transactionDirPath;
        }
        String dirPath = "./charts/" + dirNumber + "/";
        if (new File(dirPath).exists()) {
            dirNumber++;
            return getDirPath(dirNumber);
        } else {
            new File(dirPath).mkdirs();
            transactionDirPath = dirPath;
            return dirPath;
        }
    }

    private ArrayList<Double> getRatio(ArrayList<Double> firstSetOfValues, ArrayList<Double> secondSetOfValues) {
        ArrayList<Double> ratioValues = new ArrayList<>();
        for (int i = 0 ; i < firstSetOfValues.size() ; i++) {
            Double ratioValue = firstSetOfValues.get(i) / secondSetOfValues.get(i);
            ratioValues.add(ratioValue);
        }
        return ratioValues;
    }
}
