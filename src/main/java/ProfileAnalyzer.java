import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

class ProfileAnalyzer {
    Profile profile;
    HashMap<String, ArrayList<Double>> ppmValuesByName;
    HashMap<String, ArrayList<Double>> ppmAlRatiosByName;
    HashMap<Integer, HashMap<Integer, Double>> peakRatioValuesByName;

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

    HashMap<Integer, Double> getAngleValues(String name) {
        ArrayList<Double> sequenceValues = ppmAlRatiosByName.get(name);
        AngleFinder angleFinder = new AngleFinder(sequenceValues);
        angleFinder.addExtremes();
        return new HashMap<>();
    }
}
