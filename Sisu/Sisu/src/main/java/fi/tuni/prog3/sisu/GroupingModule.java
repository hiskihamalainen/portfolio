package fi.tuni.prog3.sisu;

import com.google.gson.JsonObject;

/**
 * A class that depicts a "grouping module" as defined in the Sisu API
 */
public class GroupingModule extends Module {

    /**
     * Constructs a GroupingModule object based on a JsonObject containing the relevant data
     * @param json JSonObject containing grouping module data
     */
    public GroupingModule(JsonObject json) {
        super(json);
    }

    @Override
    public int getMinCredits() {
        return -1;
    }

    @Override
    public String toString() {
        return this.getName();
    }

}
