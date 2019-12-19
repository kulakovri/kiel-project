import java.util.ArrayList;

class Grain {
    String name;
    ArrayList<Profile> rimToCoreProfiles = new ArrayList<>();
    ArrayList<Profile> coreToRimProfiles = new ArrayList<>();

    Grain(String name) {
        this.name = name;
    }

    void addRimToCoreProfile(Profile profile) {
        rimToCoreProfiles.add(profile);
    }

    void addCoreToRimProfile(Profile profile) {
        coreToRimProfiles.add(profile);
    }
}