import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.markers.SeriesMarkers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

class Templates {
    static void createRatiosForNaMgAl() {
        ProfileChart profileChart = new ProfileChart("csv/1-022-18-5h-x2-1-5L8a.csv");
        ArrayList<String> standards = new ArrayList<>();
        standards.add("csv/1-018-SPH4.csv");
        standards.add("csv/1-031-SPH5.csv");
        for (String element : new String[]{"Al27"}) {
            profileChart.buildPpmChart(standards, element);
            profileChart.buildPpmRatioCharts(standards, element);
        }
    }

    static void createCPSRatio() {
        ProfileChart profileChart = new ProfileChart("csv/1-015-SPH3.csv");
        profileChart.buildCpsChart("Al27");
        profileChart.buildCPSRatioChart("Na23" ,"Al27");
    }

    static void createSPHCharts() {
        for (String csvFileName : CSVLoader.getListOfCsvFiles()) {
            if (csvFileName.contains("SPH")) {
                ProfileChart profileChart = new ProfileChart("csv/" + csvFileName);
                profileChart.buildCpsChart("Al27");
            }
        }
    }

    static void createAnalyzerCharts() {
        Profile profile = new Profile(CSVLoader.loadCsv("csv/1-022-18-5h-x2-1-5L8a.csv"));
        ArrayList<String> standards = new ArrayList<>();
        standards.add("csv/1-018-SPH4.csv");
        standards.add("csv/1-031-SPH5.csv");
        ProfileAnalyzer profileAnalyzer = new ProfileAnalyzer(profile, standards);

        TreeMap<Integer, Double> peakRatioValues = profileAnalyzer.getAngleValues("Ba138");

        buildChartWithTreeMap(peakRatioValues, "Ba138 - Al27 ratios");

        //buildChartWithDoubleArray(ba138Al27Ratios, "Ba138 - Al27 ratios");


    }

    static void buildChartWithDoubleArray(ArrayList<Double> doubleArray, String title) {
        XYChart chart = new XYChart(2000, 800);
        chart.setTitle(title);
        chart.addSeries("analyte", null, doubleArray);
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNE);
        try {
            BitmapEncoder.saveBitmap(chart, ProfileChart.getDirPath(0) + title, BitmapEncoder.BitmapFormat.GIF);
        } catch (IOException e) {

        }
    }

    static void buildChartWithTreeMap(TreeMap<Integer, Double> treeMap, String title) {
        XYChart chart = new XYChart(2000, 800);
        chart.setTitle(title);
        chart.addSeries("analyte", new ArrayList<>(treeMap.keySet()), new ArrayList<>(treeMap.values()));
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNE);
        try {
            BitmapEncoder.saveBitmap(chart, ProfileChart.getDirPath(0) + title, BitmapEncoder.BitmapFormat.GIF);
        } catch (IOException e) {

        }
    }
}
