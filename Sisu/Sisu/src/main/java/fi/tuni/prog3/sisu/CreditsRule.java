package fi.tuni.prog3.sisu;

import com.google.gson.JsonObject;

/**
 * A class that depicts a "CreditsRule" as defined in the Sisu API.
 */
public class CreditsRule extends Rule {

    private final Rule rule;
    private MinMax credits;

    /**
     * Constructs a CreditsRule based a JsonObject depicting one.
     * @param json a JsonObject depicting a CreditsRule
     */
    public CreditsRule(JsonObject json) {
        super("CreditsRule");

        try {
            JsonObject cred = json.get("credits").getAsJsonObject();

            int min = -1;

            try {
                min = cred.get("min").getAsInt();
            } catch (UnsupportedOperationException ignored) {}

            int max = -1;

            try {
                max = cred.get("max").getAsInt();
            } catch (UnsupportedOperationException ignored) {}

            credits = new MinMax(min, max);

        } catch (NullPointerException e) {
            credits = null;
        }

        JsonObject obj = json.get("rule").getAsJsonObject();

        rule = Rule.fromJsonObject(obj);
    }

    /**
     * Returns the sub-rule of this rule.
     * @return the sub-rule of this rule
     */
    public Rule getRule() {
        return rule;
    }

    /**
     * Returns minimum credits required of the sub-rule.
     * @return minimum credits required of the sub-rule
     */
    public MinMax getCredits() {
        return credits;
    }
}
