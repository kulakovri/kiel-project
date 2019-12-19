import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Store {
    public static Map<String, Double> getElementWeight() {
        Map<String, Double> molarWeightsElements = new HashMap<>();
        String unparsedMolarWeights =
                "'O': 15.9994, 'Na': 22.99, 'Mg': 24.305, 'Al': 26.9815, " +
                        "'Si': 28.086, 'P': 30.9738, 'K': 39.098, 'Ca': 40.078," +
                        " 'Ti': 47.867, 'V': 50.9415, 'Cr': 51.996, 'Mn': 54.938, " +
                        "'Fe': 55.845, 'Ni': 58.693, 'Sr': 103.62";
        String[] elementsAndWeights = unparsedMolarWeights.split(", ");
        for (String onePair : elementsAndWeights) {
            String elementName = StringUtils.substringBetween(onePair, "'", "'");
            Double elementMolarWeight = Double.valueOf(StringUtils.substringAfter(onePair, " "));
            molarWeightsElements.put(elementName, elementMolarWeight);
        }
        return molarWeightsElements;
    }

    static Map<String, Double> getSphPpmMap() {
        Map<String, Double> sphValues = new HashMap<>();
        String ppmValuesPlain = "Al: 156129.0 ppm Ba: 121.9 ppm Ca: 87621.0 ppm " +
                "Cr: 0.1 ppm Ni: 0.0 ppm Cu: 3.2 ppm Dy: 0.1 ppm Eu: 0.4 ppm Fe: 2876.0 ppm " +
                "Ce: 2.5 ppm Co: 0.5 ppm Ga: 14.9 ppm Gd: 0.1 ppm K: 2324.0 ppm La: 1.4 ppm Li: 8.2 ppm " +
                "Mg: 663.0 ppm Mn: 77.0 ppm Na: 33235.0 ppm Nd: 1.1 ppm P: 48.7 ppm " +
                "Pb: 0.3 ppm Pr: 0.3 ppm Rb: 0.35 ppm Sc: 0.2 ppm Si: 248911.0 ppm " +
                "Sm: 0.2 ppm Sr: 1024.0 ppm Ti: 295.13 ppm V: 1.7 ppm Y: 0.26 ppm Zn: 1.4";
        String[] ppmValuesList = ppmValuesPlain.split(" ppm ");
        for (String ppmValue : ppmValuesList) {
            String elementName = StringUtils.substringBefore(ppmValue, ":");
            String weightString = StringUtils.substringAfterLast(ppmValue, " ");
            sphValues.put(elementName, Double.valueOf(weightString));
        }
        return sphValues;
    }

    static ArrayList<String> unreliableSPHes = new ArrayList<>();

    static ArrayList<String> getUnreliableSPHes() {
        if (unreliableSPHes.isEmpty()) {
            ArrayList<String> initUnreliableSPHes = new ArrayList<>();
            initUnreliableSPHes.add("1-005-SPH.csv");
            initUnreliableSPHes.add("1-015-SPH3.csv");
            initUnreliableSPHes.add("2-003-SPH1.csv");
            initUnreliableSPHes.add("2-047-SPH.csv");
            unreliableSPHes = initUnreliableSPHes;
        }
        return unreliableSPHes;
    }

    static ArrayList<String> analyzedGrains = new ArrayList<>();

    static ArrayList<String> getAnalyzedGrains() {
        if (analyzedGrains.isEmpty()) {
            ArrayList<String> initAnalyzedGrains = new ArrayList<>();
            initAnalyzedGrains.add("18-5h-1-2");
            initAnalyzedGrains.add("18-5h-1-8");
            initAnalyzedGrains.add("18-5h-1-10");
            initAnalyzedGrains.add("18-5h-x2-1-5");
            initAnalyzedGrains.add("18-5h-x2-1-13");
            initAnalyzedGrains.add("18-5h-x2-1-25");
            initAnalyzedGrains.add("18-5h-x2-1-27");
            initAnalyzedGrains.add("18-5h-x3-1-10");
            initAnalyzedGrains.add("18-5h-x3-1-12");
            initAnalyzedGrains.add("18-3a-1-8");
            initAnalyzedGrains.add("18-3a-2-2");
            initAnalyzedGrains.add("18-3a-2-5");
            initAnalyzedGrains.add("18-5h-x3-1-41");
            initAnalyzedGrains.add("18-5h-x3-1-49");
            initAnalyzedGrains.add("18-5h-x3-1-53");
            initAnalyzedGrains.add("18-5h-x3-1-54");
            initAnalyzedGrains.add("18-5h-x2-2-41");
            initAnalyzedGrains.add("18-5h-x2-2-43");
            initAnalyzedGrains.add("18-5h-x2-2-84");
            analyzedGrains = initAnalyzedGrains;
        }
        return analyzedGrains;
    }

    static ArrayList<String> rimToCoreLines = new ArrayList<>();

    static ArrayList<String> getRimToCoreLines() {
        if (rimToCoreLines.isEmpty()) {
            ArrayList<String> initRimToCoreLines = new ArrayList<>();
            for (String rimToCoreLineNumber : new String[]{
                    "2", "4", "5", "8", "10", "12", "14", "16", "18", "20",
                    "22", "24", "26", "28", "30", "31", "33", "35", "37", "39"}){
                initRimToCoreLines.add("L" + rimToCoreLineNumber);
            }
            rimToCoreLines = initRimToCoreLines;
        }
        return rimToCoreLines;
    }
}