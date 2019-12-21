import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.style.Styler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;

class Templates {
    static void testCSVBuilder() {
        CSVBuilder.saveCsvString("hi.csv", "a,b\n1,2");
    }

    static void buildChartsForAllProfilesForAllElements() {
        for (String element : new String[]{
                "Li7","Na23","Mg24","Al27","Si29",
                "P31","K39","Ca43","Ca44","Ti47",
                "Mn55","Fe57","Cu65","Ga71","Sr88",
                "Y89","Ba138","Ce140","Pb208"}) {
            buildChartsForAllProfiles(element);
        }
    }

    static void buildChartsForAllProfiles(String elementName) {
        for (String csvFileName : CSVLoader.getListOfCsvFiles()) {
            String csvFilePath = "csv/" + csvFileName;
            if (isAnalyteProfile(csvFileName)) {
                System.out.println("Analyte: " + csvFileName);
                ProfileChart profileChart = new ProfileChart(csvFilePath);
                ArrayList<String> standards = new ArrayList<>();
                standards.add(getFirstStandardForAnalyte(csvFileName));
                standards.add(getSecondStandardForAnalyte(csvFileName));
                try {
                    profileChart.buildPpmChart(standards, elementName);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

            }
        }
    }

    private static String getFirstStandardForAnalyte(String analyteCsvFileName) {
        String sphCsvName = "";
        for (String csvFileName : CSVLoader.getListOfCsvFiles()) {
            if (isSPHProfile(csvFileName) && isReliableSPH(csvFileName)) {
                sphCsvName = csvFileName;
            }
            if (csvFileName.equals(analyteCsvFileName)) {
                break;
            }
        }
        System.out.println("1) used standard: " + sphCsvName);
        return "csv/" + sphCsvName;
    }

    private static String getSecondStandardForAnalyte(String analyteCsvFileName) {
        String sphCsvName = "";
        boolean analytePassed = false;
        for (String csvFileName : CSVLoader.getListOfCsvFiles()) {
            if (csvFileName.equals(analyteCsvFileName)) {
                analytePassed = true;
            }
            if (isSPHProfile(csvFileName) && isReliableSPH(csvFileName) && analytePassed) {
                sphCsvName = csvFileName;
                break;
            }
        }
        System.out.println("2) used standard: " + sphCsvName);
        return "csv/" + sphCsvName;
    }

    private static boolean isAnalyteProfile(String csvFileName) {
        return csvFileName.contains("L");
    }

    private static boolean isSPHProfile(String csvFileName) {
        return csvFileName.contains("SPH");
    }

    private static boolean isReliableSPH(String csvFileName) {
        return !Store.getUnreliableSPHes().contains(csvFileName);
    }

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
            BitmapEncoder.saveBitmap(chart, ProfileChart.getDirPath() + title, BitmapEncoder.BitmapFormat.GIF);
        } catch (IOException e) {

        }
    }

    static void buildChartWithTreeMap(TreeMap<Integer, Double> treeMap, String title) {
        XYChart chart = new XYChart(2000, 800);
        chart.setTitle(title);
        chart.addSeries("analyte", new ArrayList<>(treeMap.keySet()), new ArrayList<>(treeMap.values()));
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNE);
        try {
            BitmapEncoder.saveBitmap(chart, ProfileChart.getDirPath() + title, BitmapEncoder.BitmapFormat.GIF);
        } catch (IOException e) {

        }
    }
}
