import java.util.ArrayList;

class Grain {
    String name;
    ArrayList<Profile> profiles = new ArrayList<>();

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

    private boolean isForThisGrainProfile(String csvFileName) {
        return csvFileName.contains(name);
    }
}