package fi.tuni.prog3.sisu;

import com.google.gson.JsonObject;


/**
 * A class that depicts a "study module" as defined in the Sisu API
 */
public class StudyModule extends Module {
    private int maxCredits;
    private int minCredits;

    /**
     * Constructs a StudyModule object based on a JsonObject containing the relevant data
     * @param json JSonObject containing study module data
     */
    public StudyModule(JsonObject json) {
        super(json);

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

    @Override
    public String toString() {
        String end = this.getMinCredits() > 0 ? " " + this.getMinCredits() + "cr" : "";
        return this.getCode() + " " + this.getName() + end;
    }

    /**
     * Returns maximum credits for the module.
     * @return maximum credits for the module
     */
    public Integer getMaxCredits() {
        return maxCredits;
    }

    @Override
    public int getMinCredits() {
        return this.minCredits;
    }

}
