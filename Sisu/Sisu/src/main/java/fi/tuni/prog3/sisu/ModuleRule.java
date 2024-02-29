package fi.tuni.prog3.sisu;

import com.google.gson.JsonObject;

/**
 * A class that depicts a "ModuleRule" as defined in the Sisu API.
 */
public class ModuleRule extends Rule {
    private final String moduleGroupId;

    /**
     * Constructs a ModuleRule based a JsonObject depicting one.
     * @param json a JsonObject depicting a ModuleRule
     */
    public ModuleRule(JsonObject json) {
        super("ModuleRule");

        moduleGroupId = json.get("moduleGroupId").getAsString();
    }

    /**
     * Returns group id of the Module this rule requires to be completed.
     * @return group id of the Module this rule requires to be completed
     */
    public String getGroupId() {
        return moduleGroupId;
    }
}
