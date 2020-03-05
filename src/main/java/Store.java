import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

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

    static TreeMap<String, Double> profileLengths = new TreeMap<>();

    static Double getProfileLength(String csvFileName) {
        if (profileLengths.isEmpty()) {
            TreeMap<String, Double> initProfileLengths = new TreeMap<>();
            initProfileLengths.put("1-007-18-5h-1-2L1.csv", 380.9);
            initProfileLengths.put("1-008-18-5h-1-2L1a.csv", 444.4);
            initProfileLengths.put("1-009-18-5h-1-2L2.csv", 448.6);
            initProfileLengths.put("1-010-18-5h-1-2L2a.csv", 376.7);
            initProfileLengths.put("1-011-18-5h-1-8L3.csv", 423.2);
            initProfileLengths.put("1-012-18-5h-1-8L4.csv", 522.7);
            initProfileLengths.put("1-013-18-5h-1-10L5.csv", 471.9);
            initProfileLengths.put("1-014-18-5h-1-10L6.csv", 476.2);
            initProfileLengths.put("1-019-18-5h-x2-1-5L7.csv", 378.8);
            initProfileLengths.put("1-020-18-5h-x2-1-5L7a.csv", 391.5);
            initProfileLengths.put("1-021-18-5h-x2-1-5L8.csv", 351.3);
            initProfileLengths.put("1-022-18-5h-x2-1-5L8a.csv", 402.1);
            initProfileLengths.put("1-023-18-5h-x2-1-13L9.csv", 476.1);
            initProfileLengths.put("1-024-18-5h-x2-1-13L10.csv", 501.5);
            initProfileLengths.put("1-025-18-5h-x2-1-25L11.csv", 491.0);
            initProfileLengths.put("1-026-18-5h-x2-1-25L11a.csv", 234.9);
            initProfileLengths.put("1-027-18-5h-x2-1-25L12.csv", 412.6);
            initProfileLengths.put("1-028-18-5h-x2-1-25L12a.csv", 313.2);
            initProfileLengths.put("1-029-18-5h-x2-1-27L13.csv", 440.2);
            initProfileLengths.put("1-030-18-5h-x2-1-27L14.csv", 448.6);
            initProfileLengths.put("1-032-VK18-5h-x3-1-10L15.csv", 349.2);
            initProfileLengths.put("1-033-VK18-5h-x3-1-10L15a.csv", 249.7);
            initProfileLengths.put("1-034-VK18-5h-x3-1-10L16.csv", 332.2);
            initProfileLengths.put("1-035-18-5h-x3-1-10L16a.csv", 275.1);
            initProfileLengths.put("1-036-18-5h-x3-1-12L17.csv", 380.9);
            initProfileLengths.put("1-037-18-5h-x3-1-12L17a.csv", 220.1);
            initProfileLengths.put("1-038-18-5h-x3-1-12L18.csv", 376.7);
            initProfileLengths.put("1-039-18-5h-x3-1-12L18a.csv", 217.9);
            initProfileLengths.put("2-005-VK18-3a-1-8L19.csv", 465.2);
            initProfileLengths.put("2-006-VK18-3a-1-8L19a.csv", 455.5);
            initProfileLengths.put("2-007-VK18-3a-1-8L20.csv", 538.8);
            initProfileLengths.put("2-008-VK18-3a-1-8L20a.csv", 426.3);
            initProfileLengths.put("2-009-VK18-3a-2-2L21.csv", 471.1);
            initProfileLengths.put("2-010-VK18-3a-2-2L21a.csv", 360.5);
            initProfileLengths.put("2-011-VK18-3a-2-2L22.csv", 478.3);
            initProfileLengths.put("2-012-VK18-3a-2-2L22a.csv", 353.3);
            initProfileLengths.put("2-013-VK18-3a-2-5L23.csv", 376.9);
            initProfileLengths.put("2-014-VK18-3a-2-5L23a.csv", 441.0);
            initProfileLengths.put("2-015-VK18-3a-2-5L24.csv", 453.8);
            initProfileLengths.put("2-016-VK18-3a-2-5L24a.csv", 371.7);
            initProfileLengths.put("2-023-VK18-5h-x3-1-41L25.csv", 374.6);
            initProfileLengths.put("2-024-VK18-5h-x3-1-41L26.csv", 374.6);
            initProfileLengths.put("2-025-VK18-5h-x3-1-49L27.csv", 556.6);
            initProfileLengths.put("2-026-VK18-5h-x3-1-49L28.csv", 539.6);
            initProfileLengths.put("2-027-VK18-5h-x3-1-53L29.csv", 476.1);
            initProfileLengths.put("2-028-VK18-5h-x3-1-53L30.csv", 444.4);
            initProfileLengths.put("2-029-VK18-5h-x3-1-53L31a.csv", 105.8);
            initProfileLengths.put("2-030-VK18-5h-x3-1-54L32.csv", 305.7);
            initProfileLengths.put("2-031-VK18-5h-x3-1-54L32a.csv", 284.1);
            initProfileLengths.put("2-032-VK18-5h-x3-1-54L33.csv", 586.2);
            initProfileLengths.put("2-035-VK18-5h-x2-2-41L34.csv", 340.7);
            initProfileLengths.put("2-036-VK18-5h-x2-2-41L34a.csv", 325.9);
            initProfileLengths.put("2-037-VK18-5h-x2-2-41L35.csv", 387.3);
            initProfileLengths.put("2-038-VK18-5h-x2-2-41L35a.csv", 275.1);
            initProfileLengths.put("2-039-VK18-5h-x2-2-43L36.csv", 554.4);
            initProfileLengths.put("2-040-VK18-5h-x2-2-43L36a.csv", 211.6);
            initProfileLengths.put("2-041-VK18-5h-x2-2-43L37.csv", 438.0);
            initProfileLengths.put("2-042-VK18-5h-x2-2-43L37a.csv", 211.6);
            initProfileLengths.put("2-043-VK18-5h-x2-2-84L38.csv", 461.3);
            initProfileLengths.put("2-044-VK18-5h-x2-2-84L38a.csv", 442.3);
            initProfileLengths.put("2-045-VK18-5h-x2-2-84L39.csv", 334.3);
            initProfileLengths.put("2-046-VK18-5h-x2-2-84L39a.csv", 495.2);
            profileLengths = initProfileLengths;
        }
        Double distanceFromRim = profileLengths.get(csvFileName);
        if (distanceFromRim == null) {
            return 0.0;
        } else {
            return distanceFromRim;
        }
    }

    static TreeMap<String, Double> lineOverlaps = new TreeMap<>();

    static Double getLineOverlap(String csvFileName) {
        if (lineOverlaps.isEmpty()) {
            TreeMap<String, Double> initLineOverlaps = new TreeMap<>();
            initLineOverlaps.put("2-008-VK18-3a-1-8L20a.csv", 45.1);
            initLineOverlaps.put("2-029-VK18-5h-x3-1-53L31a.csv", 42.3);
            initLineOverlaps.put("2-040-VK18-5h-x2-2-43L36a.csv", 110.0);
            initLineOverlaps.put("2-044-VK18-5h-x2-2-84L38a.csv", 63.4);
            lineOverlaps = initLineOverlaps;
        }
        Double lineOverlap = lineOverlaps.get(csvFileName);
        if (lineOverlap == null) {
            return 0.0;
        } else {
            return lineOverlap;
        }
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
    
    static Double getMolarWeight(String element) {
        HashMap<String, Double> molarWeightsByName = new HashMap<>();
        molarWeightsByName.put("O", 15.9994);
        molarWeightsByName.put("Na", 22.99);
        molarWeightsByName.put("Mg", 24.305);
        molarWeightsByName.put("Al", 26.9815);
        molarWeightsByName.put("Si", 28.086);
        molarWeightsByName.put("P", 30.9738);
        molarWeightsByName.put("K", 39.098);
        molarWeightsByName.put("Ca", 40.078);
        molarWeightsByName.put("Ti", 47.867);
        molarWeightsByName.put("V", 50.9415);
        molarWeightsByName.put("Cr", 51.996);
        molarWeightsByName.put("Mn", 54.938);
        molarWeightsByName.put("Fe", 55.845);
        molarWeightsByName.put("Ni", 58.693);
        molarWeightsByName.put("Sr", 103.62);
        return molarWeightsByName.get(element);
    }
}
