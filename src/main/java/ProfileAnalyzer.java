import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.TreeMap;

class ProfileAnalyzer {
    Profile profile;
    HashMap<String, ArrayList<Double>> ppmValuesByName;
    HashMap<String, ArrayList<Double>> ppmAlRatiosByName;

    ProfileAnalyzer(Profile profile, ArrayList<String> standardCsvAddresses) {
        this.profile = profile;
        this.ppmValuesByName = profile.getPpmValuesByName(standardCsvAddresses);
        this.ppmAlRatiosByName = getPpmAlValuesByName();
    }

    private HashMap<String, ArrayList<Double>> getPpmAlValuesByName() {
        HashMap<String, ArrayList<Double>> ppmAlRatiosByName = new HashMap<>();
        ArrayList<Double> alPpmValues = ppmValuesByName.get("Al27");
        for (String name : ppmValuesByName.keySet()) {
            ArrayList<Double> analyzedPpmValues = ppmValuesByName.get(name);
            ppmAlRatiosByName.put(name, profile.getPpmRatio(analyzedPpmValues, alPpmValues));
        }
        return ppmAlRatiosByName;
    }

    TreeMap<Integer, Double> getAngleValues(String name) {
        ArrayList<Double> sequenceValues = ppmAlRatiosByName.get(name);
        AngleFinder angleFinder = new AngleFinder(sequenceValues);
        Integer angleIndexesSize = angleFinder.angleIndexes.size();

        Integer count = 0;
        for (int i = 0 ; i < 5 ; i++) {
            count++;
            angleFinder.addExtremes();
            Integer newAngleIndexesSize = angleFinder.angleIndexes.size();
            if (newAngleIndexesSize == angleIndexesSize) {
                break;
            } else {
                angleIndexesSize = newAngleIndexesSize;
            }
        }

        ArrayList<Integer> angleIndexes = angleFinder.angleIndexes;
        Collections.sort(angleIndexes);

        System.out.println("Iteration count: " + count);
        TreeMap<Integer, Double> outputHashMap = new TreeMap<>();

        for (Integer extreme : angleFinder.angleIndexes) {
            System.out.println(extreme+","+sequenceValues.get(extreme));
            outputHashMap.put(extreme, sequenceValues.get(extreme));
        }

        return outputHashMap;
    }
}
