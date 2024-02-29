package fi.tuni.prog3.sisu;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * An abstract class that depicts a module as defined in the Sisu API.
 */
public abstract class Module extends DegreeModule {

    private final ArrayList<Id> children;
    private Rule rule;
    private final HashMap<String, DegreeModule> selected = new HashMap<>();

    /**
     * Initializes attributes based on a JsonObject containing the relevant data.
     * @param json JSonObject containing data for a Module.
     */
    public Module(JsonObject json) {
        super(json);

        this.children = getChildrenFromJson(json);

        try {
            this.rule = Rule.fromJsonObject(json.get("rule").getAsJsonObject());
        } catch (NullPointerException e) {
            this.rule = null;
        }
    }

    @Override
    public ArrayList<Id> getChildren() {
        return children;
    }

    @Override
    public ArrayList<String> getGroupIdsOfChildren() {
        ArrayList<String> res = new ArrayList<>();

        for (var elem : children) {
            res.add(elem.getId());
        }

        return res;
    }

    @Override
    public Rule getRule() {
        return rule;
    }

    @Override
    public HashMap<String, DegreeModule> getSelected() {
        return selected;
    }

    /**
     * Returns the "children" as in the (ids and types of) Modules or Courses that can be selected to complete this module.
     * @param json JSonObject containing data for a Module.
     * @return ids and types of children
     */
    private static ArrayList<Id> getChildrenFromJson(JsonObject json) {

        if (!json.has("type")) {
            return new ArrayList<>();
        }

        String type = json.get("type").getAsString();

        if (type.equals("AnyModuleRule") || type.equals("AnyCourseUnitRule")) {
            return new ArrayList<>();
        }

        if (type.equals("CourseUnitRule")) {
            Id course = new Id(json.get("courseUnitGroupId").getAsString(), "Course");
            ArrayList<Id> list = new ArrayList<>();
            list.add(course);
            return list;
        }

        if (type.equals("ModuleRule")) {
            Id course = new Id(json.get("moduleGroupId").getAsString(), "Module");
            ArrayList<Id> list = new ArrayList<>();
            list.add(course);
            return list;
        }

        if (type.equals("CompositeRule")) {
            ArrayList<Id> list = new ArrayList<>();
            JsonArray rules = json.get("rules").getAsJsonArray();

            for (var rule : rules) {
                list.addAll(getChildrenFromJson(rule.getAsJsonObject()));
            }

            return list;
        }

        return getChildrenFromJson(json.get("rule").getAsJsonObject());
    }

    /**
     * Returns a new Module object by fetching data from the Sisu API
     * @param id id or groupId of the module
     * @param isGroupId whether the id provided is a groupId or a normal id
     * @return a Module object constructed from API data
     * @throws IOException failed to get data from API
     */
    public static Module fromAPI(String id, boolean isGroupId) throws IOException {

        JsonObject json = isGroupId ? SisuAPIExtractor.getModuleByGroupId(id) : SisuAPIExtractor.getModuleById(id);

        String type = json.get("type").getAsString();

        if (type.equals("StudyModule")) {
            return new StudyModule(json);
        } else if (type.equals("DegreeProgramme")) {
            return new DegreeProgramme(json);
        } else {
            return new GroupingModule(json);
        }
    }
}
