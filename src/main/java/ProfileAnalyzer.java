import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

class ProfileAnalyzer {
    Profile profile;
    HashMap<String,ArrayList<Double>> ppmValuesByName;
    HashMap<String,ArrayList<Double>> ppmAlRatiosByName;
    HashMap<Integer, HashMap<Integer, Double>> peakRatioValuesByName;

    ProfileAnalyzer(Profile profile, ArrayList<String> standardCsvAddresses) {
        this.profile = profile;
        this.ppmValuesByName = profile.getPpmValuesByName(standardCsvAddresses);
        this.ppmAlRatiosByName = getPpmAlValuesByName();
    }

    private HashMap<String,ArrayList<Double>> getPpmAlValuesByName() {
        HashMap<String,ArrayList<Double>> ppmAlRatiosByName = new HashMap<>();
        ArrayList<Double> alPpmValues = ppmValuesByName.get("Al27");
        for (String name : ppmValuesByName.keySet()) {
            ArrayList<Double> analyzedPpmValues = ppmValuesByName.get(name);
            ppmAlRatiosByName.put(name, profile.getPpmRatio(analyzedPpmValues, alPpmValues));
        }
        return ppmAlRatiosByName;
    }

    HashMap<Integer, Double> getPeakRatioValues(HashMap<Integer, Double> peakRatioValues, String name) {
        ArrayList<Double> ppmAlRatioValues = ppmAlRatiosByName.get(name);
        peakRatioValues = initPeakRatioValuesIfEmpty(peakRatioValues, ppmAlRatioValues);

        ArrayList<ArrayList<Double>> splitPpmAlRatioValues = getSplitPpmAlRatioValues(peakRatioValues, ppmAlRatioValues);

        for (ArrayList<Double> ratioArray : splitPpmAlRatioValues) {
            peakRatioValues = putMinAndMaxValues(peakRatioValues, ratioArray);
        }


        return peakRatioValues;
    }

    private HashMap<Integer, Double> initPeakRatioValuesIfEmpty(HashMap<Integer, Double> peakRatioValues, ArrayList<Double> ppmAlRatioValues) {
        if (peakRatioValues.isEmpty()) {
            peakRatioValues.put(0, ppmAlRatioValues.get(0));
            peakRatioValues.put(ppmAlRatioValues.size()-1, ppmAlRatioValues.get(ppmAlRatioValues.size()-1));
        }
        return peakRatioValues;
    }

    private ArrayList<ArrayList<Double>> getSplitPpmAlRatioValues(HashMap<Integer, Double> peakRatioValues, ArrayList<Double> ppmAlRatioValues) {
        ArrayList<ArrayList<Double>> splitPpmAlRatioValues = new ArrayList<>();
        Integer previousKey = null;
        for (Integer i : peakRatioValues.keySet()) {
            if (previousKey != null) {
                ppmAlRatioValues.subList(previousKey, i);
            }
            previousKey = i;
        }
        return splitPpmAlRatioValues;
    }

    private HashMap<Integer, Double> putMinAndMaxValues(HashMap<Integer, Double> peakRatioValues, ArrayList<Double> ratioArray) {
        Double maxValue = Collections.max(ratioArray);
        Double minValue = Collections.min(ratioArray);
        for (Integer i = 0 ; i < ratioArray.size()-1 ; i++) {
            Double value = ratioArray.get(i);
            if (value == maxValue) {
                System.out.println("max value: " + value + " || index: " + i);
                peakRatioValues.put(i, value);
            }
            if (value == minValue) {
                System.out.println("min value: " + value + " || index: " + i);
                peakRatioValues.put(i, value);
            }
        }
        return peakRatioValues;
    }
}
