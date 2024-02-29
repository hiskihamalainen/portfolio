package fi.tuni.prog3.sisu;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

/**
 * A class that depicts a "CompositeRule" as defined in the Sisu API.
 */
public class CompositeRule extends Rule {

    private final ArrayList<Rule> rules = new ArrayList<>();
    private MinMax require;
    private String description;
    private final boolean allMandatory;

    /**
     * Constructs a CompositeRule based a JsonObject depicting one.
     * @param json a JsonObject depicting a CompositeRule
     */
    public CompositeRule(JsonObject json) {
        super("CompositeRule");

        try {
            JsonObject cred = json.get("require").getAsJsonObject();

            int min = -1;

            try {
                min = cred.get("min").getAsInt();
            } catch (UnsupportedOperationException ignored) {}

            int max = -1;

            try {
                max = cred.get("max").getAsInt();
            } catch (UnsupportedOperationException ignored) {}

            require = new MinMax(min, max);

        } catch (IllegalStateException e) {
            require = null;
        }

        try {
            description = json.get("description").getAsString();
        } catch (UnsupportedOperationException e) {
            description = "";
        }

        allMandatory = json.get("allMandatory").getAsBoolean();

        JsonArray arr = json.get("rules").getAsJsonArray();

        for (var elem : arr) {
            rules.add(Rule.fromJsonObject(elem.getAsJsonObject()));
        }
    }

    /**
     * Returns the sub-rules of this rule.
     * @return sub-rules
     */
    public ArrayList<Rule> getRules() {
        return rules;
    }

    /**
     * Returns the minimum and maximum number of sub-rules that need to be satisfied.
     * @return minimum and maximum number of sub-rules that need to be satisfied
     */
    public MinMax getRequire() {
        return require;
    }

    /**
     * Returns the description of the rule.
     * @return description of the rule
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns whether all sub-rules are mandatory.
     * @return whether all sub-rules are mandatory
     */
    public boolean isAllMandatory() {
        return allMandatory;
    }
}
