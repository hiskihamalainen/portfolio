package fi.tuni.prog3.sisu;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.service.query.EmptyNodeQueryException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Contains tests for DegreeInspector
 */
public class DegreeInspectorTest extends ApplicationTest {

    Student student;

    @Override
    public void start(Stage stage) {

        student = new Student("Matti Meikäläinen", "123456ABC", 2020);

        try {
            student.setDegree(DegreeProgramme.fromAPI("otm-d729cfc3-97ad-467f-86b7-b6729c496c82", false));
        } catch (IOException e) {
            throw new RuntimeException("a");
        }

        DegreeInspector degreeInspector = new DegreeInspector(student);
        degreeInspector.setMaxSize(1000, 700);

        Scene scene = new Scene(degreeInspector);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Tests adding and removing a module.
     */
    @Test
    public void testAddingAndRemovingModule() {
        clickOn(lookup("#selectorWrapper").lookup("TTEA Information Technology 180cr").queryAs(CheckBox.class));
        assertFalse(student.getDegree().getSelected().isEmpty());
        lookup("#view").lookup("TTEA Information Technology 180cr").query();

        clickOn(lookup("#selectorWrapper").lookup("TTEA Information Technology 180cr").queryAs(CheckBox.class));
        assertTrue(student.getDegree().getSelected().isEmpty());

        assertThrows(EmptyNodeQueryException.class, () -> {
            lookup("#view").lookup("TTEA Information Technology 180cr").query();
        });
    }

    /**
     * Tests selecting a course and marking it completed and not completed.
     */
    @Test
    public void testAddingCourse() {
        clickOn(lookup("#selectorWrapper").lookup("TTEA Information Technology 180cr").queryAs(CheckBox.class));
        clickOn(lookup("#view").lookup("TTEA Information Technology 180cr").queryAs(Node.class));
        clickOn(lookup("#view").lookup("COMP-P01 Joint studies in Information Technology 65cr").queryAs(Node.class));
        clickOn(lookup("#view").lookup("COMM.100 Basic Course on Communications Engineering 5cr").queryAs(Node.class));

        clickOn(lookup("#selectorWrapper").lookup("Completed").queryAs(Button.class));
        assertEquals(1, student.getCompletedCourses().size());
        assertEquals(5, student.getCredits());

        clickOn(lookup("#selectorWrapper").lookup("Completed").queryAs(Button.class));
        assertEquals(1, student.getCompletedCourses().size());
        assertEquals(5, student.getCredits());

        clickOn(lookup("#selectorWrapper").lookup("Not completed").queryAs(Button.class));
        assertTrue(student.getCompletedCourses().isEmpty());
        assertEquals(0, student.getCredits());

        clickOn(lookup("#selectorWrapper").lookup("Not completed").queryAs(Button.class));
        assertTrue(student.getCompletedCourses().isEmpty());
        assertEquals(0, student.getCredits());
    }
}