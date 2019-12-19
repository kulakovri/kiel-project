import java.util.ArrayList;

class Grain {
    String name;
    ArrayList<Profile> profiles = new ArrayList<>();

    Grain(String name) {
        this.name = name;
        loadProfiles();
    }

    private void loadProfiles() {
        for (String csvFileName : CSVLoader.getListOfCsvFiles()) {
            if (isForThisGrainProfile(csvFileName)) {
                Profile loadedProfile = new Profile(CSVLoader.loadCsv("csv/"+csvFileName));
                loadedProfile.isFromRimToCore = isFromRimToCoreProfile(csvFileName);
                loadedProfile.isContinuation = isContinuationProfile(csvFileName);
                profiles.add(loadedProfile);
            }
        }
    }

    private boolean isForThisGrainProfile(String csvFileName) {
        return csvFileName.contains(name);
    }

    private boolean isFromRimToCoreProfile(String csvFileName) {
        for (String rimToCoreLine : Store.getRimToCoreLines()) {
            if (csvFileName.contains(rimToCoreLine)) {
                return true;
            }
        }
        return false;
    }

    private boolean isContinuationProfile(String csvFileName) {
        return csvFileName.contains("a.csv");
    }
}