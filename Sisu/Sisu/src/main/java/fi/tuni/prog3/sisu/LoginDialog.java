package fi.tuni.prog3.sisu;

import com.google.gson.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.Year;


/**
 * A Dialog class for login view
 */
public class LoginDialog extends Dialog<Student> {

    private final Button okButton = getOkButton();
    private final ButtonBar backButtonBar = getBackButtonBar();
    private final GridPane grid = new GridPane();
    private final Label yearLabel = new Label("Starting year:");
    private final TextField numberTextField = new TextField();
    private final Label nameLabel = new Label("Student name:");
    private final TextField nameTextField = new TextField();
    private final ComboBox<Integer> yearComboBox = new ComboBox<>();

    /**
     * Constructs initial view for LoginDialog
     */
    public LoginDialog() {

        this.setTitle("Student information");
        this.getDialogPane().setPrefSize(400,400);

        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);

        grid.add(new Label("Student number:"), 0, 0);
        numberTextField.setPrefSize(250,20);
        numberTextField.setId("numberTextField");
        grid.add(numberTextField, 1, 0);

        this.getDialogPane().setContent(grid);

        okButton.setOnAction(event -> handleOkButton());
        ButtonBar okButtonBar = new ButtonBar();
        okButtonBar.getButtons().add(okButton);
        okButtonBar.setLayoutX(350);
        okButtonBar.setLayoutY(380);
        this.getDialogPane().getChildren().add(okButtonBar);

        ButtonBar quitButtonBar = getQuitButtonBar();
        this.getDialogPane().getChildren().add(quitButtonBar);

        EventHandler<KeyEvent> textEventHandler = e -> {
            okButton.setDisable(numberTextField.getText().isEmpty());
        };

        numberTextField.setOnKeyTyped(textEventHandler);

        this.getDialogPane().addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            if (e.getCode() == KeyCode.ENTER && !okButton.isDisabled()) {
                okButton.fire();
                e.consume();
            }
        });

    }

    /**
     * @return okButton initially disabled
     */
    private Button getOkButton() {

        Button okButton = new Button("OK");
        okButton.setId("okButton");
        okButton.setDisable(true);

        return okButton;

    }

    /**
     * @return ButtonBar containing Quit-button
     */
    private ButtonBar getQuitButtonBar() {

        Button quitButton = new Button("Quit");
        quitButton.setId("quitButton");
        ButtonBar quitButtonBar = new ButtonBar();
        quitButtonBar.getButtons().add(quitButton);
        quitButtonBar.setLayoutX(40);
        quitButtonBar.setLayoutY(380);

        quitButton.setOnAction(e -> {
            this.setResult(new Student(null, null, 0));
            this.close();
            Platform.exit();
        });

        return quitButtonBar;
    }

    /**
     * @return ButtonBar containing Back-button
     */
    private ButtonBar getBackButtonBar() {

        Button backButton = new Button("Back");
        backButton.setId("backButton");
        ButtonBar backButtonBar = new ButtonBar();
        backButtonBar.getButtons().add(backButton);
        backButtonBar.setLayoutX(120);
        backButtonBar.setLayoutY(380);

        backButton.setOnAction(e -> switchBack());

        return backButtonBar;

    }

    /**
     * Changes the view to ask more information
     * if the student number entered in initial
     * view doesn't exist
     */
    private void switchContent() {

        okButton.setDisable(true);
        numberTextField.setDisable(true);

        setupYearComboBox();
        nameTextField.setPrefSize(250,20);
        nameTextField.setId("nameTextField");

        grid.add(nameLabel, 0, 1);
        grid.add(nameTextField, 1, 1);
        grid.add(yearLabel, 0, 2);
        grid.add(yearComboBox, 1, 2);

        this.getDialogPane().getChildren().add(backButtonBar);
        nameTextField.requestFocus();
        okButton.setOnAction(event -> handleOKButton2());

        EventHandler<KeyEvent> textEventHandler = e -> {
            okButton.setDisable(nameTextField.getText().isEmpty());
        };
        nameTextField.setOnKeyTyped(textEventHandler);

    }


    /**
     * Sets up YearComboBox for LoginDialog
     */
    private void setupYearComboBox() {

        ObservableList<Integer> years = FXCollections.observableArrayList();
        for (int year = Year.now().getValue(); year >= 1950 ; year--) {
            years.add(year);
        }

        yearComboBox.setItems(years);
        yearComboBox.setValue(Year.now().getValue());
        yearComboBox.setMaxSize(70,20);
        yearComboBox.setMaxHeight(10);
        yearComboBox.setId("yearComboBox");

    }

    /**
     * Switches the view back to initial view
     */
    private void switchBack() {

        grid.getChildren().remove(nameTextField);
        grid.getChildren().remove(yearComboBox);
        grid.getChildren().remove(yearLabel);
        grid.getChildren().remove(nameLabel);
        this.getDialogPane().getChildren().remove(backButtonBar);
        numberTextField.setDisable(false);
        okButton.setDisable(false);
        nameTextField.setText("");
        okButton.setOnAction(event -> handleOkButton());

    }

    /**
     * An EventHandler for OkButton in the initial view
     */
    private void handleOkButton() {

        File folder = new File(System.getProperty("user.dir") + "/students/");
        File[] filelist = folder.listFiles();

        // Loops through "students"-folder and checks if the entered student number exists
        for (File file : filelist != null ? filelist : new File[0]) {
            if (file.getName().equals(numberTextField.getText() + ".json")) {

                GsonBuilder gsonb = new GsonBuilder();
                gsonb.registerTypeAdapter(Rule.class, new AbstractClassAdapter());
                gsonb.registerTypeAdapter(DegreeModule.class, new AbstractClassAdapter());

                Gson gson = gsonb.create();
                try (FileReader reader = new FileReader(file)) {
                    this.setResult(gson.fromJson(reader, Student.class));
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                this.close();
                return;
            }
        }

        switchContent();
    }

    /**
     * An EventHanlder for OkButton in second view
     */
    private void handleOKButton2() {

        Student student = new Student(nameTextField.getText(), numberTextField.getText(), yearComboBox.getValue());
        this.setResult(student);
        student.saveToJson();
        this.close();
    }

}
