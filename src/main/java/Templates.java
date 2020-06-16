import com.google.gson.Gson;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.style.Styler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;

class Templates {


    static void getProfileLenghtsJson() {
        Gson gson = new Gson();
        System.out.println(gson.toJson(Store.getRimToCoreLines()));
    }


    static void buildGrainsWithMedianSliding() {
        String folder = "csv-out/7";
        for (String filePath : CSVLoader.getListOfFilesInFolder(folder)) {
            PostProcessedGrain postProcessedGrain = new PostProcessedGrain(folder + "/"+ filePath);
            postProcessedGrain.buildMedianSlidingWindow(20, filePath.split("\\.")[0]);

        }
    }


    static void buildCompiledChartsWithoutRange() {
        String folder = "csv-out/22";
        for (String filePath : CSVLoader.getListOfFilesInFolder(folder)) {
            CompiledProfile compiledProfile = new CompiledProfile(folder + "/" + filePath);
            if (filePath.contains("3a-1-8")) {
                compiledProfile.buildChartsExcludingRanges(new int[][]{new int[]{-1, 30}, new int[]{445,500}, new int[]{370,400}, new int[]{260,280}, new int[]{875,950}});
            } else if (filePath.contains("5h-1-2")) {
                compiledProfile.buildChartsExcludingRanges(new int[][]{new int[]{-1, 30}, new int[]{130, 148}});
            } else if (filePath.contains("x2-1-27")) {
                compiledProfile.buildChartsExcludingRanges(new int[][]{new int[]{-1, 30}, new int[]{230,233} });
            } else if (filePath.contains("x3-1-10")) {
                compiledProfile.buildChartsExcludingRanges(new int[][]{new int[]{-1, 40}, new int[]{317, 330}, new int[]{425, 440}, new int[]{530, 548}, new int[]{100, 128}});
            } else if (filePath.contains("x3-1-41")) {
                compiledProfile.buildChartsExcludingRanges(new int[][]{new int[]{-1, 18}, new int[]{110,125} });
            } else if (filePath.contains("x3-1-54")) {
                compiledProfile.buildChartsExcludingRanges(new int[][]{new int[]{-1, 10}, new int[]{42, 88} });
            } else if (filePath.contains("5h-1-8")) {
                compiledProfile.buildChartsExcludingRanges(new int[][]{new int[]{-1, 10}, new int[]{302, 305}});
            } else if (filePath.contains("x2-1-13")) {
                compiledProfile.buildChartsExcludingRanges(new int[][]{new int[]{-1, 10}, new int[]{435, 450}});
            } else if (filePath.contains("x2-1-25")) {
                compiledProfile.buildChartsExcludingRanges(new int[][]{new int[]{240, 340}, new int[]{40, 55}});
            } else if (filePath.contains("x2-1-5")) {
                compiledProfile.buildChartsExcludingRanges(new int[][]{new int[]{270, 288}, new int[]{349,360}, new int[]{30,42} });
            } else if (filePath.contains("x2-2-41")) {
                compiledProfile.buildChartsExcludingRanges(new int[][]{new int[]{390, 418}, new int[]{-1, 30}, new int[]{75, 120}});
            } else if (filePath.contains("x2-2-84")) {
                compiledProfile.buildChartsExcludingRanges(new int[][]{new int[]{97, 100}, new int[]{605,626}, new int[]{60,80}});
            } else if (filePath.contains("5h-1-10")) {
                compiledProfile.buildChartsExcludingRanges(new int[][]{new int[]{375, 395}, new int[]{291, 310}});
            } else if (filePath.contains("x3-1-12")) {
                compiledProfile.buildChartsExcludingRanges(new int[][]{new int[]{225, 245}, new int[]{265, 287}, new int[]{-1, 5}});
            } else if (filePath.contains("x3-1-49")) {
                compiledProfile.buildChartsExcludingRanges(new int[][]{new int[]{412, 415}, new int[]{-1, 40}, new int[]{390, 405}});
            } else if (filePath.contains("x3-1-53")) {
                compiledProfile.buildChartsExcludingRanges(new int[][]{new int[]{69, 85}, new int[]{170, 189}, new int[]{405, 431}, new int[]{22, 35}});
            } else if (filePath.contains("3a-2-5")) {
                compiledProfile.buildChartsExcludingRanges(new int[][]{new int[]{250, 535}, new int[]{680, 737}});
            } else if (filePath.contains("3a-2-2")) {
                compiledProfile.buildChartsExcludingRanges(new int[][]{new int[]{0, 40}, new int[]{130, 190}, new int[]{250, 350}, new int[]{600, 710}});
            } else {
                compiledProfile.buildChartsExcludingRanges(new int[][]{});
            }
        }
    }

    static void buildCompiledChartsWithinRange() {
        String folder = "csv-out/19";
        for (String filePath : CSVLoader.getListOfFilesInFolder(folder)) {
            CompiledProfile compiledProfile = new CompiledProfile(folder + "/" + filePath);
            if (filePath.contains("3a-2-2")) {
                compiledProfile.buildChartsWithinRange(590, 700);
            } else if (filePath.contains("18-5h-x2-1-5")) {
                compiledProfile.buildChartsWithinRange(300, 430);
            } else if (filePath.contains("18-5h-x2-1-25")) {
                compiledProfile.buildChartsWithinRange(410, 540);
            } else if (filePath.contains("18-5h-x2-2-41")) {
                compiledProfile.buildChartsWithinRange(430, 530);
            } else if (filePath.contains("18-5h-x2-2-84")) {
                compiledProfile.buildChartsWithinRange(560, 700);
            } else if (filePath.contains("18-5h-x3-1-54")) {
                compiledProfile.buildChartsWithinRange(430, 530);
            }
        }
    }

    static void buildCompiledCharts() {
        String folder = "csv-out/19";
        for (String filePath : CSVLoader.getListOfFilesInFolder(folder)) {
            CompiledProfile compiledProfile = new CompiledProfile(folder + "/" + filePath);
            compiledProfile.buildChartsForAllElements();
        }
    }

    static void testOxides() {
        Oxide oxide = new Oxide("CaO");
        System.out.println(oxide.ppmWeightRatio);
        Double percents = 100698.61 / oxide.ppmWeightRatio;
        System.out.println(percents);
    }

    static void getRimPPMProcessedGrains() {
        String folder = "csv-out/7";
        for (String filePath : CSVLoader.getListOfFilesInFolder(folder)) {
            PostProcessedGrain postProcessedGrain = new PostProcessedGrain(folder + "/"+ filePath);
            postProcessedGrain.printRimValues();
        }
    }

    static void sortPostProcessedGrains() {
        String folder = "csv-out/7";
        for (String filePath : CSVLoader.getListOfFilesInFolder(folder)) {
            PostProcessedGrain postProcessedGrain = new PostProcessedGrain(folder + "/"+ filePath);
            postProcessedGrain.buildCsv(filePath.split("\\.")[0]);
        }
    }

    static void loadGrains() {
        for (String grainName : Store.getAnalyzedGrains()) {
            try {
                Grain grain = new Grain(grainName);
                grain.buildGrainCsv();
            } catch (Exception e) {
                System.out.println(grainName + " has failed to be calculated");
            }

        }
    }

    static void testCSVBuilder() {
        Profile profile = new Profile("1-014-18-5h-1-10L6.csv");
        CSVBuilder csvBuilder = new CSVBuilder("L6", profile.getCalculatedValuesByName(null, 0.0));
        csvBuilder.saveCsvString();
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
            String csvFilePath = csvFileName;
            if (isAnalyteProfile(csvFileName)) {
                ChartBuilder chartBuilder = new ChartBuilder(csvFilePath);
                ArrayList<String> standards = new ArrayList<>();
                standards.add(getFirstStandardForAnalyte(csvFileName));
                standards.add(getSecondStandardForAnalyte(csvFileName));
                try {
                    chartBuilder.buildPpmChart(standards, elementName);
                } catch (Exception e) {

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
        ChartBuilder chartBuilder = new ChartBuilder("csv/1-022-18-5h-x2-1-5L8a.csv");
        ArrayList<String> standards = new ArrayList<>();
        standards.add("csv/1-018-SPH4.csv");
        standards.add("csv/1-031-SPH5.csv");
        for (String element : new String[]{"Al27"}) {
            chartBuilder.buildPpmChart(standards, element);
            chartBuilder.buildPpmRatioCharts(standards, element);
        }
    }

    static void createCPSRatio() {
        ChartBuilder chartBuilder = new ChartBuilder("csv/1-015-SPH3.csv");
        chartBuilder.buildCpsChart("Al27");
        chartBuilder.buildCPSRatioChart("Na23" ,"Al27");
    }

    static void createNISTCharts() {
        for (String csvFileName : CSVLoader.getListOfCsvFiles()) {
            if (csvFileName.contains("NIST") || csvFileName.contains("610")) {
                ChartBuilder chartBuilder = new ChartBuilder(csvFileName);
                chartBuilder.buildCpsChart("Al27");
            }
        }
    }

    static void createAnalyzerCharts() {
        Profile profile = new Profile("csv/1-022-18-5h-x2-1-5L8a.csv");
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
            BitmapEncoder.saveBitmap(chart, ChartBuilder.getDirPath() + title, BitmapEncoder.BitmapFormat.GIF);
        } catch (IOException e) {

        }
    }

    static void buildChartWithTreeMap(TreeMap<Integer, Double> treeMap, String title) {
        XYChart chart = new XYChart(2000, 800);
        chart.setTitle(title);
        chart.addSeries("analyte", new ArrayList<>(treeMap.keySet()), new ArrayList<>(treeMap.values()));
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNE);
        try {
            BitmapEncoder.saveBitmap(chart, ChartBuilder.getDirPath() + title, BitmapEncoder.BitmapFormat.GIF);
        } catch (IOException e) {

        }
    }
}
