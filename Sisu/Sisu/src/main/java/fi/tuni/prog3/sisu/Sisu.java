package fi.tuni.prog3.sisu;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import java.io.IOException;


/**
 * JavaFX Sisu
 */
public class Sisu extends Application {

    private Student student;
    private BorderPane root;

    @Override
    public void start(Stage stage) {

        this.root = new BorderPane();
        root.setPadding(new Insets(10, 10, 10, 10));

        Scene scene = new Scene(root, 1000, 700);
        stage.setScene(scene);
        stage.setTitle("Sisu");

        LoginDialog loginDialog = new LoginDialog();
        loginDialog.showAndWait();
        student = loginDialog.getResult();

        root.setCenter(new StudentInfoView(student));
        root.setBottom(getStudentInfoViewButtonBar());

        // Creating the first instance of WebView causes a stutter
        // -> Do it at startup to make it less noticeable
        new WebView();

        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }

    /**
     * Returns a button that quits the program.
     * @return quit button
     */
    private Button getQuitButton() {

        Button button = new Button("Quit");

        button.setOnAction((ActionEvent event) -> {
            Platform.exit();
        });
        
        return button;
    }

    /**
     * Returns a button that saves student's info to a json file.
     * @return save button
     */
    private Button getSaveButton() {
        Button button = new Button("Save");

        button.setOnAction((ActionEvent event) -> {
            student.saveToJson();
        });

        return button;
    }

    /**
     * Returns a button that switches the view back to the student info view.
     * @return back button
     */
    private Button getBackButton() {
        Button button = new Button("Back");

        button.setOnAction((ActionEvent event) -> {
            root.setCenter(new StudentInfoView(student));
            root.setBottom(getStudentInfoViewButtonBar());
        });

        return button;
    }

    /**
     * Returns a button that opens the course selection view.
     * @return a button that opens the course selection view
     */
    private Button getCourseSelectionButton() {
        Button button = new Button("Select courses");

        button.setOnAction((ActionEvent event) -> {
            root.setCenter(new DegreeInspector(student));
            root.setBottom(getDegreeInspectorButtonBar());
        });

        if (student.getDegree() == null) {
            button.setDisable(true);
        }

        return button;
    }

    /**
     * Returns a button that opens the degree selection view.
     * @return a button that opens the degree selection view
     */
    private Button getDegreeSelectionButton() {
        Button button = new Button("Select degree");

        button.setOnAction((ActionEvent event) -> {
            try {
                DegreeSelector selector = new DegreeSelector(student);
                root.setCenter(selector);
                root.setBottom(getDegreeSelectorButtonBar(selector));
            } catch (IOException ignored) {}
        });

        return button;
    }

    /**
     * Returns a button that confirms the selection for the degree selection view.
     * @param selector DegreeSelector that the button is tied to
     * @return select button
     */
    private Button getSelectButton(DegreeSelector selector) {
        Button button = new Button("Select");

        button.setOnAction((ActionEvent event) -> {
            if (selector.select()) {
                root.setCenter(new StudentInfoView(student));
                root.setBottom(getStudentInfoViewButtonBar());
            }
        });

        return button;
    }

    /**
     * Returns the button bar that is displayed when student info view is active.
     * @return button bar that is displayed when student info view is active
     */
    private ButtonBar getStudentInfoViewButtonBar() {
        ButtonBar buttonBar = new ButtonBar();
        buttonBar.getButtons().addAll(getDegreeSelectionButton(), getCourseSelectionButton(), getSaveButton(), getQuitButton());

        buttonBar.setPadding(new Insets(10, 0, 0, 0));

        return buttonBar;
    }

    /**
     * Returns the button bar that is displayed when degree selector is active.
     * @param selector DegreeSelector that the button bar is tied to
     * @return button bar that is displayed when degree selector is active
     */
    private ButtonBar getDegreeSelectorButtonBar(DegreeSelector selector) {
        ButtonBar buttonBar = new ButtonBar();

        buttonBar.getButtons().addAll(getBackButton()
                , getSelectButton(selector)
                , getSaveButton()
                , getQuitButton());

        buttonBar.setPadding(new Insets(10, 0, 0, 0));
        buttonBar.setMinHeight(20);

        return buttonBar;
    }

    /**
     * Returns the button bar that is displayed when degree inspector (a.k.a. course selector) is active.
     * @return button bar that is displayed when degree inspector is active
     */
    private ButtonBar getDegreeInspectorButtonBar() {
        ButtonBar buttonBar = new ButtonBar();
        buttonBar.getButtons().addAll(getBackButton(), getSaveButton(), getQuitButton());

        buttonBar.setPadding(new Insets(10, 0, 0, 0));

        return buttonBar;
    }
}