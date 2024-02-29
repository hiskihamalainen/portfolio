package fi.tuni.prog3.sisu;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationTest;
import static org.junit.jupiter.api.Assertions.*;


/**
 * TestFX class for LoginDialog
 */
public class LoginDialogTest extends ApplicationTest {

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(new BorderPane());
        stage.setScene(scene);
        stage.show();
        LoginDialog loginDialog = new LoginDialog();
        loginDialog.show();
    }


    /**
     * Tests if okButton is enabled
     */
    @Test
    public void testOKButtonEnabled() {
        FxRobot robot = new FxRobot();
        TextField numberTextField = robot.lookup("#numberTextField").queryAs(TextField.class);
        robot.clickOn(numberTextField).write("123");
        Button okButton = robot.lookup("#okButton").queryButton();
        clickOn("#okButton");
        assertTrue(okButton.isDisabled());
    }

    /**
     * Tests if okButton is disabled
     */
    @Test
    public void testOKButtonDisabled() {
        FxRobot robot = new FxRobot();
        Button okButton = robot.lookup("#okButton").queryButton();
        assertTrue(okButton.isDisabled());
    }

    /**
     * Tests studentNumber TextField
     */
    @Test
    public void testStudentNumberInput() {
        FxRobot robot = new FxRobot();
        TextField numberTextField = robot.lookup("#numberTextField").queryAs(TextField.class);
        robot.clickOn(numberTextField).write("5678");
        assertEquals("5678", numberTextField.getText());
    }

    /**
     * Tests studentName TextField
     */
    @Test
    public void testStudentNameInput() {
        FxRobot robot = new FxRobot();
        TextField numberTextField = robot.lookup("#numberTextField").queryAs(TextField.class);
        robot.clickOn(numberTextField).write("5");
        //Button okButton = robot.lookup("#okButton").queryButton();
        clickOn("#okButton");
        TextField nameTextField = robot.lookup("#nameTextField").queryAs(TextField.class);
        robot.clickOn(nameTextField).write("abcd");
        assertEquals("abcd", nameTextField.getText());
    }

    /**
     * Tests startingYear ComboBox
     */
    @Test
    public void testStartingYearInput() {
        FxRobot robot = new FxRobot();
        TextField numberTextField = robot.lookup("#numberTextField").queryAs(TextField.class);
        robot.clickOn(numberTextField).write(String.valueOf("5"));
        Button okButton = robot.lookup("#okButton").queryButton();
        clickOn(okButton);
        ComboBox yearComboBox = robot.lookup("#yearComboBox").queryAs(ComboBox.class);
        robot.clickOn(yearComboBox);
        robot.type(KeyCode.DOWN);
        robot.type(KeyCode.ENTER);
        assertEquals(2022, yearComboBox.getValue());

    }

    /**
     * Tests if backButton is showing
     */
    @Test
    public void testOKBackButtonShowing() {
        FxRobot robot = new FxRobot();
        TextField numberTextField = robot.lookup("#numberTextField").queryAs(TextField.class);
        robot.clickOn(numberTextField).write("123");
        clickOn("#okButton");
        Button backButton = robot.lookup("#backButton").queryButton();
        assertTrue(backButton.isVisible());
    }

}
