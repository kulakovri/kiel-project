import java.util.ArrayList;
import java.util.HashMap;

class ProfileCompiler {
    boolean isFromRimToCore;
    HashMap<String, ArrayList<Double>> initialProfile;
    HashMap<String, ArrayList<Double>> continuedProfile;

    ProfileCompiler(boolean isFromRimToCore) {
        this.isFromRimToCore = isFromRimToCore;
    }

    void addProfileToCompile(HashMap<String, ArrayList<Double>> profile, boolean isContinuation) {
        if (isContinuation) {
            continuedProfile = profile;
        } else {
            initialProfile = profile;
        }
    }

    HashMap<String, ArrayList<Double>> getCompiled() {
        HashMap<String, ArrayList<Double>> compiledPrifile = new HashMap<>();


        return compiledPrifile;
    }
}
