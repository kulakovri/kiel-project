import java.util.ArrayList;
import java.util.HashMap;

class Grain {
    String name;
    ArrayList<Profile> profiles = new ArrayList<>();
    HashMap<String, ArrayList<Double>> fromCoreToRimProfile;
    HashMap<String, ArrayList<Double>> fromRimToCoreProfile;

    Grain(String name) {
        this.name = name;
        loadProfiles();
    }

    private void loadProfiles() {
        System.out.println(name + ": " );
        for (String csvFileName : CSVLoader.getListOfCsvFiles()) {
            if (isForThisGrainProfile(csvFileName)) {
                System.out.println(csvFileName);
                Profile loadedProfile = new Profile(csvFileName);
                profiles.add(loadedProfile);
            }
        }
    }

    void getCalculations() {
        if (profiles.size() < 3) {
            for (Profile profile : profiles) {
                if (profile.isFromRimToCore) {
                    fromRimToCoreProfile = profile.getCalculatedValuesByName(null);
                } else {
                    fromCoreToRimProfile = profile.getCalculatedValuesByName(null);
                }
            }
        } else {
            compileCalculations();
        }
    }

    void compileCalculations() {
        ProfileCompiler fromRimToCoreCompiler = new ProfileCompiler(true);
        ProfileCompiler fromCoreToRimCompiler = new ProfileCompiler(false);

        for (Profile profile : profiles) {
            if (profile.isFromRimToCore) {
                fromRimToCoreCompiler.addProfileToCompile(profile.getCalculatedValuesByName(null), profile.isContinuation);
            } else {
                fromCoreToRimCompiler.addProfileToCompile(profile.getCalculatedValuesByName(null), profile.isContinuation);
            }
        }
        fromRimToCoreProfile = fromRimToCoreCompiler.getCompiled();
        fromCoreToRimProfile = fromCoreToRimCompiler.getCompiled();
    }

    private boolean isForThisGrainProfile(String csvFileName) {
        return csvFileName.contains(name);
    }
}