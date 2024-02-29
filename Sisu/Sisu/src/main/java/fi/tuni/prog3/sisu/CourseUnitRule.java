package fi.tuni.prog3.sisu;

import com.google.gson.JsonObject;

/**
 * A class that depicts a "CourseUnitRule" as defined in the Sisu API.
 */
public class CourseUnitRule extends Rule {
    private final String courseUnitGroupId;

    /**
     * Constructs a CourseUnitRule based a JsonObject depicting one.
     * @param json a JsonObject depicting a CourseUnitRule
     */
    public CourseUnitRule(JsonObject json) {
        super("CourseUnitRule");
        courseUnitGroupId = json.get("courseUnitGroupId").getAsString();
    }

    /**
     * Returns group id of the CourseUnit this rule requires to be completed.
     * @return group id of the CourseUnit this rule requires to be completed
     */
    public String getGroupId() {
        return courseUnitGroupId;
    }
}
