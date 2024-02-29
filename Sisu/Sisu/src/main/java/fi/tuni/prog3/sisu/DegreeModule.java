/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fi.tuni.prog3.sisu;

import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * An abstract class for storing information on Modules and Courses.
 */
public abstract class DegreeModule {
    private final String name;
    private String code;
    private final String id;
    private final String groupId;

    /**
     * Initializes attributes based on a JsonObject containing the relevant data
     * @param json JSonObject containing data for either a Course or a Module
     */
    public DegreeModule(JsonObject json) {

        this.name = getBilingualStringFromJson(json, "name", true);

        try {
            this.code = json.get("code").getAsString();
        } catch (UnsupportedOperationException e) {
            this.code = "";
        }

        this.id = json.get("id").getAsString();
        this.groupId = json.get("groupId").getAsString();
    }

    protected static String getBilingualStringFromJson(JsonObject json, String fieldName, boolean primaryLangEnglish) {
        String lang = primaryLangEnglish ? "en" : "fi";
        String lang2 = primaryLangEnglish ? "fi" : "en";

        String res;

        try {
            res = json.get(fieldName).getAsJsonObject().get(lang).getAsString();
        } catch (IllegalStateException | NullPointerException e) {
            try {
                res = json.get(fieldName).getAsJsonObject().get(lang2).getAsString();
            } catch (IllegalStateException | NullPointerException e1) {
                res = "";;
            }
        }

        return res;
    }

    /**
     * Returns the name of the Module or Course.
     * @return name of the Module or Course.
     */
    public String getName() {
        return this.name;
    }

    /**
     *
     * @return code of the Module or Course
     */
    public String getCode() {
        return code;
    }

    /**
     * Returns the id of the Module or Course.
     * @return id of the Module or Course.
     */
    public String getId() {
        return this.id;
    }
    
    /**
     * Returns the group id of the Module or Course.
     * @return group id of the Module or Course.
     */
    public String getGroupId() {
        return this.groupId;
    }
    
    /**
     * Returns the minimum credits of the Module or Course.
     * @return minimum credits of the Module or Course.
     */
    public abstract int getMinCredits();

    /**
     * Returns the "children" as in the (ids and types of) Modules or Courses that can be selected to complete this module.
     * @return groupIds and types of the "children" of the Module or Course.
     */
    public abstract ArrayList<Id> getChildren();

    /**
     * Returns the groupIds of the "children" of the Module or Course.
     * @return groupIds and types of the "children" of the Module or Course.
     */
    public abstract ArrayList<String> getGroupIdsOfChildren();

    /**
     * Returns the top-level Rule that governs which "children" must be selected.
     * @return rule that governs which "children" must be selected.
     */
    public abstract Rule getRule();

    /**
     * Returns the Courses and Modules the user has selected from among children, mapped to their groupIds.
     * @return key: groupId, value: selected Course or Module.
     */
    public abstract HashMap<String, DegreeModule> getSelected();

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    /**
     * A small static class for storing ids. Exists mostly because there
     * was a problem serialising Pairs with Gson.
     */
    public static class Id {
        private final String id;
        private final String type;

        /**
         * Constructs an Id
         * @param id an id, usually a groupId of a Course or Module
         * @param type the type of entity which the id corresponds to; in practise "Course" or "Module"
         */
        public Id(String id, String type) {
            this.id = id;
            this.type = type;
        }

        /**
         *
         * @return id, usually a groupId of a Course or Module.
         */
        public String getId() {
            return id;
        }

        /**
         *
         * @return the type of entity which the id corresponds to; in practise "Course" or "Module"
         */
        public String getType() {
            return type;
        }
    }
}
