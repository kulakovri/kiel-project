import java.util.ArrayList;
import java.util.HashMap;

public class ProfileAnalyzer {
    Profile profile;
    HashMap<String,ArrayList<Double>> ppmValuesByName;
    HashMap<String,ArrayList<Double>> ppmAlRatiosByName;

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

    void getPeakValues(ArrayList<String> standardCsvAddresses) {
        profile.getPpmValuesByName(standardCsvAddresses);

    }
}
