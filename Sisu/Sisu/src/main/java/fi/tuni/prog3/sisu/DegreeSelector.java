package fi.tuni.prog3.sisu;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;

import static fi.tuni.prog3.sisu.CourseInfoView.getWebviewAccordion;

public class DegreeSelector extends HBox {

    /**
     * A small class that pairs a degree's id and name together and overrides toString for use by the ListView class.
     */
    private static class DegreeLabel {
        private final String id;
        private final String name;

        /**
         * Constructs a DegreeLabel object.
         * @param id id of the DegreeProgramme
         * @param name name of the DegreeProgramme
         */
        public DegreeLabel(String id, String name) {
            this.id = id;
            this.name = name;
        }

        /**
         * Returns id.
         * @return id
         */
        public String getId() {
            return id;
        }

        /**
         * Returns name.
         * @return name
         */
        @Override
        public String toString() {
            return name;
        }
    }

    DegreeProgramme currentSelected = null;
    Student student;

    /**
     * Constructs a DegreeSelector.
     * @param student student whose degree is to be selected
     * @throws IOException failed to get list of degree from Sisu API
     */
    public DegreeSelector(Student student) throws IOException {

        this.student = student;

        VBox vbox = new VBox();
        vbox.setSpacing(5);
        TextField textField = new TextField();

        VBox degreeInfoWrapper = new VBox();
        degreeInfoWrapper.setPrefSize(5000, 3500);

        ListView<DegreeLabel> lv = new ListView<>();
        lv.setPrefSize(5000, 3500);

        vbox.getChildren().addAll(textField, lv);

        ArrayList<Pair<String, String>> degrees = SisuAPIExtractor.getDegreeProgrammeIdsAndNames();

        ArrayList<DegreeLabel> degreeLabels = new ArrayList<>();

        for (var a : degrees) {
            degreeLabels.add(new DegreeLabel(a.getKey(), a.getValue()));
        }

        lv.getItems().addAll(degreeLabels);

        lv.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {

            if (newValue == null) {
                currentSelected = null;
                return;
            }

            try {
                DegreeProgramme deg = DegreeProgramme.fromAPI(newValue.getId(), false);
                currentSelected = deg;
                degreeInfoWrapper.getChildren().clear();
                degreeInfoWrapper.getChildren().add(getDegreeInfoGrid(deg));
            } catch (IOException e) {
                System.out.println("Couldn't find DegreeProgramme with id " + newValue.getId());
            }
        });

        textField.setOnAction(actionEvent -> {
            lv.getItems().clear();
            lv.getItems().addAll(degreeLabels.stream().filter(
                    elem -> elem.name.toLowerCase().contains(textField.getCharacters().toString().toLowerCase())
            ).collect(Collectors.toList()));
        });

        this.getChildren().addAll(vbox, degreeInfoWrapper);
    }

    /**
     * Sets the current selected degree from the list as the student's current selected degree.
     * @return whether the student's degree was changed
     */
    public boolean select() {

        if (currentSelected != null) {
            return student.setDegree(currentSelected);
        }

        return false;
    }


    /**
     * Returns a GridPane displaying information about the given degree.
     * @param deg DegreeProgramme to display information about
     * @return GridPane displaying information a degree
     */
    public GridPane getDegreeInfoGrid(DegreeProgramme deg) {

        GridPane grid = new GridPane();

        VBox.setMargin(grid, new Insets(0, 10, 0, 10));

        Label nameLabel = new Label("Name: ");
        Label codeLabel = new Label("Code: ");
        Label creditsLabel = new Label("Credits: ");
        Label outcomesLabel = new Label("Outcomes: ");
        Label contentLabel = new Label("Content: ");

        nameLabel.setMinWidth(65);
        codeLabel.setMinWidth(65);
        creditsLabel.setMinWidth(65);
        outcomesLabel.setMinWidth(65);
        contentLabel.setMinWidth(65);

        grid.add(nameLabel, 0, 0);
        grid.add(new Label(deg.getName()), 1, 0);

        grid.add(codeLabel, 0, 1);
        grid.add(new Label(deg.getCode()), 1, 1);

        grid.add(creditsLabel, 0, 2);
        grid.add(new Label(String.valueOf(deg.getMinCredits())), 1, 2);

        grid.add(outcomesLabel, 0, 3);
        if (!deg.getOutcomes().isEmpty()) {
            grid.add(getWebviewAccordion("Click to view", deg.getOutcomes()), 1, 3);
        }

        grid.add(contentLabel, 0, 4);
        if (!deg.getDesc().isEmpty()) {
            grid.add(getWebviewAccordion("Click to view", deg.getDesc()), 1, 4);
        }

        return grid;
    }
}
