package fi.tuni.prog3.sisu;

import com.google.gson.*;

/**
 * An abstract class that depicts a "rule" from the Sisu API.
 */
public abstract class Rule {

    private final String type;

    /**
     * Constructor that initializes the type attribute.
     * @param type type of the rule.
     */
    public Rule(String type) {
        this.type = type;
    }

    /**
     * Returns a new rule based on a json object depicting a rule.
     * @param json a json object that depicts a rule
     * @return a new Rule
     */
    public static Rule fromJsonObject(JsonObject json) {

        String type = json.get("type").getAsString();

        switch (type) {
            case "CreditsRule":
                return new CreditsRule(json);

            case "CompositeRule":
                return new CompositeRule(json);

            case "ModuleRule":
                return new ModuleRule(json);

            case "CourseUnitRule":
                return new CourseUnitRule(json);

            case "AnyModuleRule":
                return new AnyModuleRule();

            case "AnyCourseUnitRule":
                return new AnyCourseUnitRule();
        }

        return null;
    }

    /**
     * Returns the type of the rule.
     * @return type of the rule
     */
    public String getType() {
        return type;
    }

    /**
     * A small data class for storing minimum and maximum.
     */
    public static class MinMax {
        public final int min;
        public final int max;

        /**
         * A constructor for MinMax.
         * @param min minimum
         * @param max maximum
         */
        public MinMax(int min, int max) {
            this.min = min;
            this.max = max;
        }
    }


}
