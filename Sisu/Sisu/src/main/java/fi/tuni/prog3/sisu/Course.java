package fi.tuni.prog3.sisu;

import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A class that depicts a TUNI Course
 */
public class Course extends DegreeModule {

    private final String content;
    private final String outcomes;
    private final String additional;
    private final String prereq;
    private final String materials;
    private int credits;

    /**
     * Constructs a Course object based on a JsonObject containing the relevant data
     * @param json JSonObject containing course data
     */
    public Course(JsonObject json) {

        super(json);

        this.outcomes = getBilingualStringFromJson(json, "outcomes", true);
        this.additional = getBilingualStringFromJson(json, "additional", true);
        this.prereq = getBilingualStringFromJson(json, "prerequisites", true);
        this.content = getBilingualStringFromJson(json, "content", true);
        this.materials = getBilingualStringFromJson(json, "learningMaterial", true);

        try {
            this.credits = json.get("credits").getAsJsonObject().get("min").getAsInt();
        } catch (Exception e) {
            this.credits = 0;
        }
    }


    /**
     * Courses do not have children, but having this method for all DegreeModules is convenient.
     * @return an empty ArrayList
     */
    @Override
    public ArrayList<Id> getChildren() {
        return new ArrayList<>();
    }

    /**
     * Courses do not have children, but having this method for all DegreeModules is convenient.
     * @return an empty ArrayList
     */
    @Override
    public ArrayList<String> getGroupIdsOfChildren() {
        return new ArrayList<>();
    }

    /**
     * Courses do not have rules, but having this method for all DegreeModules is convenient.
     * @return null
     */
    @Override
    public Rule getRule() {
        return null;
    }

    /**
     * Courses do not have children to select from, but having this method for all DegreeModules is convenient.
     * @return an empty ArrayList
     */
    @Override
    public HashMap<String, DegreeModule> getSelected() {
        return new HashMap<>();
    }

    /**
     * Returns a new Course object by fetching data from the Sisu API
     * @param id id or groupId of the course
     * @param isGroupId whether the id provided is a groupId or a normal id
     * @return a Course object constructed from API data
     * @throws IOException failed to get data from API
     */
    public static Course fromAPI(String id, boolean isGroupId) throws IOException {
        JsonObject json = isGroupId ? SisuAPIExtractor.getCourseUnitByGroupId(id) : SisuAPIExtractor.getCourseUnitById(id);
        return new Course(json);
    }

    @Override
    public String toString() {
        return this.getCode() + " " + this.getName() + " " + this.getMinCredits() + "cr";
    }

    /**
     *Returns the pre-requirements for the course.
     * @return pre-requirements for the course
     */
    public String getPrereq() {
        return prereq;
    }

    /**
     * Returns materials for the course.
     * @return materials for the course
     */
    public String getMaterials() {
        return materials;
    }

    /**
     * Returns course content.
     * @return course content
     */
    public String getContent() {
        return content;
    }

    /**
     * Returns outcomes of the course.
     * @return outcomes of the course
     */
    public String getOutcomes() {
        return outcomes;
    }

    /**
     * Returns additional information on the course.
     * @return additional information on the course
     */
    public String getAdditional() {
        return additional;
    }

    @Override
    public int getMinCredits() {
        return this.credits;
    }
}
