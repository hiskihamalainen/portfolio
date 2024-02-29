package fi.tuni.prog3.sisu;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import java.text.DecimalFormat;


/**
 * A class for viewing student's information after logging in
 */
public class StudentInfoView extends VBox {

    /**
     * Constructs a grid containing all student information
     * @param student Currently logged student
     */
    public StudentInfoView(Student student) {

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(6);

        String degreeName = student.getDegree() == null ? "None" : student.getDegree().getName() + " " + student.getDegree().getMinCredits() + " cr";
        double degreeCredits = student.getDegree() == null ? 0 : student.getDegree().getMinCredits();
        DecimalFormat decimalFormat = new DecimalFormat("#");

        grid.add(getHBox("Name: ", student.getName()), 0, 0);
        grid.add(getHBox("Student number: ", student.getStudentNumber()), 0, 1);
        grid.add(getHBox("Starting year: ", String.valueOf(student.getStartingYear())), 0, 2);
        grid.add(getHBox("Degree: ", degreeName), 0, 3);
        grid.add(getHBox("Credits: ", String.valueOf(student.getCredits()) + " / " + decimalFormat.format(degreeCredits)), 0, 4);
        grid.add(getProgressHBox(student), 0, 5);

        this.setAlignment(Pos.CENTER);
        this.getChildren().add(grid);
    }

    /**
     * @param text A string - left label's text
     * @param value A string - right label's text
     * @return HBox containing both labels
     */
    private HBox getHBox(String text, String value) {

        Label label = new Label(text);
        Label label2 = new Label(value);
        label.setFont(new Font(20));
        label2.setFont(new Font(20));
        label.setStyle("-fx-font-weight: bold;");
        label2.setMaxSize(400, 500);
        label2.setWrapText(true);

        return new HBox(label, label2);
    }

    /**
     * @param student Currently logged student object
     * @return hBox containing ProgressBar and Label
     */
    private HBox getProgressHBox(Student student) {

        double degreeCredits = student.getDegree() == null ? 0 : student.getDegree().getMinCredits();
        double percentage = student.getCredits() / degreeCredits * 100;
        String percentageString = String.format("%.0f %%", percentage);

        ProgressBar progressBar = new ProgressBar();
        progressBar.setStyle("-fx-accent: limegreen;");
        progressBar.setProgress(student.getCredits() / degreeCredits);
        progressBar.setPrefSize(300, 30);

        Label progressLabel = new Label();
        if (student.getDegree() == null) {
            progressLabel.setText("0 %");
        } else {
            progressLabel.setText(percentageString);
        }
        progressLabel.setFont(new Font(20));

        HBox hBox = new HBox(progressBar, progressLabel);
        hBox.setSpacing(10);

        return hBox;

    }
}
