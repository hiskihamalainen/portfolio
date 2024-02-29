package fi.tuni.prog3.sisu;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;

/**
 * A class that depicts a Student.
 */
public class Student {

    private final String name;
    private final String studentNumber;
    private final int startingYear;
    private DegreeProgramme degree = null;
    private int credits = 0;
    private final HashSet<String> completedCourses = new HashSet<>();

    /**
     * Constructs a Student object.
     * @param name name of the student
     * @param studentNumber student number of the student
     * @param startingYear starting year of the student
     */
    public Student(String name, String studentNumber, int startingYear) {
        this.name = name;
        this.studentNumber = studentNumber;
        this.startingYear = startingYear;
    }

    /**
     * Returns name.
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns student number.
     * @return student number
     */
    public String getStudentNumber() {
        return studentNumber;
    }

    /**
     * Returns starting year.
     * @return starting year
     */
    public int getStartingYear() {
        return startingYear;
    }

    /**
     * Returns selected degree.
     * @return degree
     */
    public DegreeProgramme getDegree() {
        return degree;
    }

    /**
     * Returns total credits of completed courses.
     * @return credits
     */
    public int getCredits() {
        return credits;
    }

    /**
     * Returns completed courses.
     * @return completed courses
     */
    public HashSet<String> getCompletedCourses() {
        return completedCourses;
    }

    /**
     * Sets changes/sets degree and resets associated variables.
     * Nothing is done if the new degree has the same id as the old one.
     * @param degree DegreeProgramme to set as new degree
     * @return whether a new degree was successfully set
     */
    public boolean setDegree(DegreeProgramme degree) {

        if (this.degree == null || !this.degree.getId().equals(degree.getId())) {
            this.degree = degree;
            this.credits = 0;
            this.completedCourses.clear();
            return true;
        }

        return false;
    }

    /**
     * Adds a new course to completed courses and adds credits accordingly.
     * @param course completed course
     * @return whether the course was successfully added
     */
    public boolean addCourse(Course course) {
        if (!this.completedCourses.contains(course.getId())) {
            this.completedCourses.add(course.getId());
            this.credits += course.getMinCredits();
            return true;
        }

        return false;
    }

    /**
     * Removes a course from completed courses and subtracts credits accordingly.
     * @param course course to remove
     * @return whether course was successfully removed
     */
    public boolean removeCourse(Course course) {
        if (this.completedCourses.contains(course.getId())) {
            this.completedCourses.remove(course.getId());
            this.credits -= course.getMinCredits();
            return true;
        }

        return false;
    }

    /**
     * Saves student info to json. Creates file and folder if they do not exist.
     */
    public void saveToJson() {

        // creates students folder for studentName.json files if the folder doesn't already exist
        String folderName = "students";
        File folder = new File(folderName);
        folder.mkdir();

        String filename = folder.getAbsolutePath() + "/" + this.studentNumber + ".json";

        GsonBuilder gsonBuilder = new GsonBuilder();

        // Rule and DegreeModule need to save extra info on which subclass the object was.
        gsonBuilder.registerTypeAdapter(DegreeModule.class, new AbstractClassAdapter());
        gsonBuilder.registerTypeAdapter(Rule.class, new AbstractClassAdapter());

        Gson gson = gsonBuilder.create();

        try (FileWriter writer = new FileWriter(filename)) {
            writer.write(gson.toJson(this));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
