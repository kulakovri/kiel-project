import java.util.ArrayList;
import java.util.HashMap;

class ProfileCompiler {
    boolean isFromRimToCore;
    Profile initialProfile;
    Profile continuedProfile;

    HashMap<String, ArrayList<Double>> initialProfileData = new HashMap<>();
    HashMap<String, ArrayList<Double>> continuedProfileData = new HashMap<>();


    ProfileCompiler(boolean isFromRimToCore) {
        this.isFromRimToCore = isFromRimToCore;
    }

    void addProfileToCompile(Profile profile) {
        if (profile.isContinuation) {
            continuedProfile = profile;
        } else {
            initialProfile = profile;
        }
    }

    void getCompiled() {
        if (continuedProfile == null) {
            initialProfileData = initialProfile.getCalculatedValuesByName(null, 0.0);
            return;
        }
        Double overlap = Store.getLineOverlap(continuedProfile.csvFileName);

        Double initialProfileLength = initialProfile.profileLength;
        Double continuedProfileLength = continuedProfile.profileLength;
        if (isFromRimToCore) {
            initialProfileData = initialProfile.getCalculatedValuesByName(null, 0.0);
            continuedProfileData = continuedProfile.getCalculatedValuesByName(null, initialProfileLength-overlap);
        } else {
            initialProfileData = initialProfile.getCalculatedValuesByName(null, continuedProfileLength-overlap);
            continuedProfileData = continuedProfile.getCalculatedValuesByName(null, 0.0);
        }
    }
}
