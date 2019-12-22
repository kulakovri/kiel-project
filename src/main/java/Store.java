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
    static TreeMap<String, Double> closestDistancesFromRim = new TreeMap<>();

    static Double getStartingDistanceFromRim(String csvFileName) {
        if (closestDistancesFromRim.isEmpty()) {
            TreeMap<String, Double> initClosestDistancesFromRim = new TreeMap<>();
            initClosestDistancesFromRim.put("1-007-18-5h-1-2L1.csv", 450.0);
            initClosestDistancesFromRim.put("1-010-18-5h-1-2L2a.csv", 460.0);
            initClosestDistancesFromRim.put("1-019-18-5h-x2-1-5L7.csv", 460.0);
            initClosestDistancesFromRim.put("1-022-18-5h-x2-1-5L8a.csv", 450.0);
            initClosestDistancesFromRim.put("1-025-18-5h-x2-1-25L11.csv", 250.0);
            initClosestDistancesFromRim.put("1-028-18-5h-x2-1-25L12a.csv", 460.0);
            initClosestDistancesFromRim.put("1-032-VK18-5h-x3-1-10L15.csv", 300.0);
            initClosestDistancesFromRim.put("1-035-18-5h-x3-1-10L16a.csv", 450.0);
            initClosestDistancesFromRim.put("1-036-18-5h-x3-1-12L17.csv", 290.0);
            initClosestDistancesFromRim.put("1-039-18-5h-x3-1-12L18a.csv", 460.0);
            initClosestDistancesFromRim.put("2-005-VK18-3a-1-8L19.csv", 400.0);
            initClosestDistancesFromRim.put("2-008-VK18-3a-1-8L20a.csv", 380.0);
            initClosestDistancesFromRim.put("2-009-VK18-3a-2-2L21.csv", 380.0);
            initClosestDistancesFromRim.put("2-012-VK18-3a-2-2L22a.csv", 460.0);
            initClosestDistancesFromRim.put("2-013-VK18-3a-2-5L23.csv", 470.0);
            initClosestDistancesFromRim.put("2-016-VK18-3a-2-5L24a.csv", 465.0);
            initClosestDistancesFromRim.put("2-029-VK18-5h-x3-1-53L31a.csv", 700.0);
            initClosestDistancesFromRim.put("2-030-VK18-5h-x3-1-54L32.csv", 430.0);
            initClosestDistancesFromRim.put("2-035-VK18-5h-x2-2-41L34.csv", 460.0);
            initClosestDistancesFromRim.put("2-038-VK18-5h-x2-2-41L35a.csv", 430.0);
            initClosestDistancesFromRim.put("2-039-VK18-5h-x2-2-43L36.csv", 200.0);
            initClosestDistancesFromRim.put("2-042-VK18-5h-x2-2-43L37a.csv", 470.0);
            initClosestDistancesFromRim.put("2-043-VK18-5h-x2-2-84L38.csv", 500.0);
            initClosestDistancesFromRim.put("2-046-VK18-5h-x2-2-84L39a.csv", 400.0);
            closestDistancesFromRim = initClosestDistancesFromRim;
        }
        Double distanceFromRim = closestDistancesFromRim.get(csvFileName);
        if (distanceFromRim == null) {
            return 0.0;
        } else {
            return distanceFromRim;
        }
    }

    static TreeMap<String, Double> profileLengths = new TreeMap<>();

    static Double getProfileLength(String csvFileName) {
        if (profileLengths.isEmpty()) {
            TreeMap<String, Double> initProfileLengths = new TreeMap<>();
            initProfileLengths.put("1-007-18-5h-1-2L1.csv", 212.4);
            initProfileLengths.put("1-008-18-5h-1-2L1a.csv", 247.8);
            initProfileLengths.put("1-009-18-5h-1-2L2.csv", 250.16);
            initProfileLengths.put("1-010-18-5h-1-2L2a.csv", 210.04);
            initProfileLengths.put("1-011-18-5h-1-8L3.csv", 236.0);
            initProfileLengths.put("1-012-18-5h-1-8L4.csv", 291.46);
            initProfileLengths.put("1-013-18-5h-1-10L5.csv", 263.14);
            initProfileLengths.put("1-014-18-5h-1-10L6.csv", 265.5);
            initProfileLengths.put("1-019-18-5h-x2-1-5L7.csv", 211.22);
            initProfileLengths.put("1-020-18-5h-x2-1-5L7a.csv", 218.3);
            initProfileLengths.put("1-021-18-5h-x2-1-5L8.csv", 195.88);
            initProfileLengths.put("1-022-18-5h-x2-1-5L8a.csv", 224.2);
            initProfileLengths.put("1-023-18-5h-x2-1-13L9.csv", 265.5);
            initProfileLengths.put("1-024-18-5h-x2-1-13L10.csv", 279.66);
            initProfileLengths.put("1-025-18-5h-x2-1-25L11.csv", 273.76);
            initProfileLengths.put("1-026-18-5h-x2-1-25L11a.csv", 130.98);
            initProfileLengths.put("1-027-18-5h-x2-1-25L12.csv", 230.1);
            initProfileLengths.put("1-028-18-5h-x2-1-25L12a.csv", 174.64);
            initProfileLengths.put("1-029-18-5h-x2-1-27L13.csv", 245.44);
            initProfileLengths.put("1-030-18-5h-x2-1-27L14.csv", 250.16);
            initProfileLengths.put("1-032-VK18-5h-x3-1-10L15.csv", 194.7);
            initProfileLengths.put("1-033-VK18-5h-x3-1-10L15a.csv", 139.24);
            initProfileLengths.put("1-034-VK18-5h-x3-1-10L16.csv", 185.26);
            initProfileLengths.put("1-035-18-5h-x3-1-10L16a.csv", 153.4);
            initProfileLengths.put("1-036-18-5h-x3-1-12L17.csv", 212.4);
            initProfileLengths.put("1-037-18-5h-x3-1-12L17a.csv", 122.72);
            initProfileLengths.put("1-038-18-5h-x3-1-12L18.csv", 210.04);
            initProfileLengths.put("1-039-18-5h-x3-1-12L18a.csv", 121.54);
            initProfileLengths.put("2-005-VK18-3a-1-8L19.csv", 243.88);
            initProfileLengths.put("2-006-VK18-3a-1-8L19a.csv", 238.78);
            initProfileLengths.put("2-007-VK18-3a-1-8L20.csv", 282.464);
            initProfileLengths.put("2-008-VK18-3a-1-8L20a.csv", 223.496);
            initProfileLengths.put("2-009-VK18-3a-2-2L21.csv", 325.36);
            initProfileLengths.put("2-010-VK18-3a-2-2L21a.csv", 249.0);
            initProfileLengths.put("2-011-VK18-3a-2-2L22.csv", 330.34);
            initProfileLengths.put("2-012-VK18-3a-2-2L22a.csv", 244.02);
            initProfileLengths.put("2-013-VK18-3a-2-5L23.csv", 229.908);
            initProfileLengths.put("2-014-VK18-3a-2-5L23a.csv", 269.0);
            initProfileLengths.put("2-015-VK18-3a-2-5L24.csv", 276.83);
            initProfileLengths.put("2-016-VK18-3a-2-5L24a.csv", 226.78);
            initProfileLengths.put("2-023-VK18-5h-x3-1-41L25.csv", 208.86);
            initProfileLengths.put("2-024-VK18-5h-x3-1-41L26.csv", 208.86);
            initProfileLengths.put("2-025-VK18-5h-x3-1-49L27.csv", 310.34);
            initProfileLengths.put("2-026-VK18-5h-x3-1-49L28.csv", 300.9);
            initProfileLengths.put("2-027-VK18-5h-x3-1-53L29.csv", 265.5);
            initProfileLengths.put("2-028-VK18-5h-x3-1-53L30.csv", 247.8);
            initProfileLengths.put("2-029-VK18-5h-x3-1-53L31a.csv", 59.0);
            initProfileLengths.put("2-030-VK18-5h-x3-1-54L32.csv", 208.59);
            initProfileLengths.put("2-031-VK18-5h-x3-1-54L32a.csv", 193.87);
            initProfileLengths.put("2-032-VK18-5h-x3-1-54L33.csv", 400.0);
            initProfileLengths.put("2-035-VK18-5h-x2-2-41L34.csv", 189.98);
            initProfileLengths.put("2-036-VK18-5h-x2-2-41L34a.csv", 181.72);
            initProfileLengths.put("2-037-VK18-5h-x2-2-41L35.csv", 215.94);
            initProfileLengths.put("2-038-VK18-5h-x2-2-41L35a.csv", 153.4);
            initProfileLengths.put("2-039-VK18-5h-x2-2-43L36.csv", 309.16);
            initProfileLengths.put("2-040-VK18-5h-x2-2-43L36a.csv", 118.0);
            initProfileLengths.put("2-041-VK18-5h-x2-2-43L37.csv", 244.26);
            initProfileLengths.put("2-042-VK18-5h-x2-2-43L37a.csv", 118.0);
            initProfileLengths.put("2-043-VK18-5h-x2-2-84L38.csv", 257.24);
            initProfileLengths.put("2-044-VK18-5h-x2-2-84L38a.csv", 246.62);
            initProfileLengths.put("2-045-VK18-5h-x2-2-84L39.csv", 186.44);
            initProfileLengths.put("2-046-VK18-5h-x2-2-84L39a.csv", 276.12);
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
            initLineOverlaps.put("2-008-VK18-3a-1-8L20a.csv", 23.66);
            initLineOverlaps.put("2-029-VK18-5h-x3-1-53L31a.csv", 23.6);
            initLineOverlaps.put("2-040-VK18-5h-x2-2-43L36a.csv", 61.36);
            initLineOverlaps.put("2-044-VK18-5h-x2-2-84L38a.csv", 35.4);
            lineOverlaps = initLineOverlaps;
        }
        Double distanceFromRim = lineOverlaps.get(csvFileName);
        if (distanceFromRim == null) {
            return 0.0;
        } else {
            return distanceFromRim;
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
}
