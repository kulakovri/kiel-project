import java.util.ArrayList;
import java.util.HashMap;

class Grain {
    String name;
    ArrayList<Profile> profiles = new ArrayList<>();
    HashMap<String, ArrayList<Double>> fromCoreToRimProfileInitial;
    HashMap<String, ArrayList<Double>> fromRimToCoreProfileInitial;
    HashMap<String, ArrayList<Double>> fromCoreToRimProfileContinued;
    HashMap<String, ArrayList<Double>> fromRimToCoreProfileContinued;


    Grain(String name) {
        this.name = name;
        loadProfiles();
        getCalculations();
    }

    void buildGrainCsv() {
        ArrayList<CSVBuilder> csvBuilders = new ArrayList<>();
        for (Profile profile : profiles) {
            if (profile.isFromRimToCore) {
                if (profile.isContinuation) {
                    if (!fromRimToCoreProfileContinued.isEmpty()) {
                        csvBuilders.add(new CSVBuilder(profile.csvFileName, fromRimToCoreProfileContinued));
                    }
                } else {
                    if (!fromRimToCoreProfileInitial.isEmpty()) {
                        csvBuilders.add(new CSVBuilder(profile.csvFileName, fromRimToCoreProfileInitial));
                    }
                }
            } else {
                if (profile.isContinuation) {
                    if (!fromCoreToRimProfileContinued.isEmpty()) {
                        csvBuilders.add(new CSVBuilder(profile.csvFileName, fromCoreToRimProfileContinued));
                    }
                } else {
                    if (!fromCoreToRimProfileInitial.isEmpty()) {
                        csvBuilders.add(new CSVBuilder(profile.csvFileName, fromCoreToRimProfileInitial));
                    }
                }
            }
        }
        CSVBuilder.buildJoinedCsv(name, csvBuilders);
    }

    private void loadProfiles() {

        for (String csvFileName : CSVLoader.getListOfCsvFiles()) {
            if (isForThisGrainProfile(csvFileName)) {
                Profile loadedProfile = new Profile(csvFileName);
                profiles.add(loadedProfile);
            }
        }
    }

    private void getCalculations() {
        if (profiles.size() < 3) {
            for (Profile profile : profiles) {
                if (profile.isFromRimToCore) {
                    fromRimToCoreProfileInitial = profile.getCalculatedValuesByName(null, 0.0);
                } else {
                    fromCoreToRimProfileInitial = profile.getCalculatedValuesByName(null, 0.0);
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
                //System.out.println("Adding from rim to Core line: " + profile.csvFileName);
                fromRimToCoreCompiler.addProfileToCompile(profile);
            } else {
                //System.out.println("Adding from core to rim line: " + profile.csvFileName);
                fromCoreToRimCompiler.addProfileToCompile(profile);
            }
        }
        fromRimToCoreCompiler.getCompiled();
        fromCoreToRimCompiler.getCompiled();

        fromCoreToRimProfileInitial = fromCoreToRimCompiler.initialProfileData;
        fromRimToCoreProfileInitial = fromRimToCoreCompiler.initialProfileData;
        fromCoreToRimProfileContinued = fromCoreToRimCompiler.continuedProfileData;
        fromRimToCoreProfileContinued = fromRimToCoreCompiler.continuedProfileData;
    }

    private boolean isForThisGrainProfile(String csvFileName) {
        return csvFileName.contains(name);
    }
}