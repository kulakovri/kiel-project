import java.util.ArrayList;

public class Templates {
    static void createRatiosForNaMgAl() {
        ProfileChart profileChart = new ProfileChart("csv/1-022-18-5h-x2-1-5L8a.csv");
        ArrayList<String> standards = new ArrayList<>();
        standards.add("csv/1-018-SPH4.csv");
        standards.add("csv/1-031-SPH5.csv");
        for (String element : new String[]{"Na23", "Mg24", "Al27"}) {
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
}
