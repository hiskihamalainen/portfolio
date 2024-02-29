package fi.tuni.prog3.sisu;

import com.google.gson.JsonObject;

import java.io.IOException;

/**
 * A class that depicts a "degree programme" as defined in the Sisu API
 */
public class DegreeProgramme extends Module {
    private final String desc;
    private final String outcomes;
    private int maxCredits;
    private int minCredits;

    /**
     * Constructs a DegreeProgramme object based on a JsonObject containing the relevant data
     * @param json a JSonObject containing degree data
     */
    public DegreeProgramme(JsonObject json) {
        super(json);

        this.desc = getBilingualStringFromJson(json, "contentDescription", true);
        this.outcomes = getBilingualStringFromJson(json, "learningOutcomes", true);

        try {
            this.maxCredits = json.get("targetCredits").getAsJsonObject().get("max").getAsInt();
        } catch (Exception e) {
            this.maxCredits = -1;
        }

        try {
            this.minCredits = json.get("targetCredits").getAsJsonObject().get("min").getAsInt();
        } catch (Exception e) {
            this.minCredits = -1;
        }
    }

    /**
     * Returns a new DegreeProgramme object by fetching data from the Sisu API
     * @param id id or groupId of the DegreeProgramme
     * @param isGroupId whether the id provided is a groupId or a normal id
     * @return a DegreeProgramme object constructed from API data
     * @throws IOException failed to get data from API
     */
    public static DegreeProgramme fromAPI(String id, boolean isGroupId) throws IOException {
        JsonObject json = isGroupId ? SisuAPIExtractor.getModuleByGroupId(id) : SisuAPIExtractor.getModuleById(id);
        return new DegreeProgramme(json);
    }

    @Override
    public String toString() {
        return this.getCode() + " " + this.getName() + " " + this.getMinCredits() + "cr";
    }

    /**
     * Returns a description of the degree programme.
     * @return a description of the degree programme
     */
    public String getDesc() {
        return desc;
    }

    /**
     * Returns maximum credits for the degree programme.
     * @return maximum credits for the degree programme
     */
    public int getMaxCredits() {
        return maxCredits;
    }

    @Override
    public int getMinCredits() {
        return this.minCredits;
    }

    /**
     * Returns learning outcomes of the degree programme.
     * @return learning outcomes of the degree programme
     */
    public String getOutcomes() {
        return outcomes;
    }
}
