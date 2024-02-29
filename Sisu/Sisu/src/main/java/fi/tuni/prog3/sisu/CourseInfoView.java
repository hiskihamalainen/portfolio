package fi.tuni.prog3.sisu;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebView;

/**
 * A GUI element that displays information pertaining to a course.
 */
public class CourseInfoView extends ScrollPane {

    /**
     * Constructs a CourseInfoView.
     * @param course course whose info to show
     */
    public CourseInfoView(Course course) {

        GridPane grid = new GridPane();

        grid.add(new Label("Name: "), 0, 0);
        grid.add(new Label(course.getName()), 1, 0);

        grid.add(new Label("Code: "), 0, 1);
        grid.add(new Label(course.getCode()), 1, 1);

        grid.add(new Label("Credits: "), 0, 2);
        grid.add(new Label(String.valueOf(course.getMinCredits())), 1, 2);

        grid.add(new Label("Outcomes: "), 0, 3);
        if (!course.getOutcomes().isEmpty()) {
            grid.add(getWebviewAccordion("Click to view", course.getOutcomes()), 1, 3);
        }

        grid.add(new Label("Content: "), 0, 4);
        if (!course.getContent().isEmpty()) {
            grid.add(getWebviewAccordion("Click to view", course.getContent()), 1, 4);
        }

        grid.add(new Label("Pre-requisites: "), 0, 5);
        if (!course.getPrereq().isEmpty()) {
            grid.add(getWebviewAccordion("Click to view", course.getPrereq()), 1, 5);
        }

        grid.add(new Label("Materials: "), 0, 6);
        if (!course.getMaterials().isEmpty()) {
            grid.add(getWebviewAccordion("Click to view", course.getMaterials()), 1, 6);
        }

        grid.add(new Label("Additional info: "), 0, 7);
        if (!course.getAdditional().isEmpty()) {
            grid.add(getWebviewAccordion("Click to view", course.getAdditional()), 1, 7);
        }

        this.setPrefSize(5000, 3000);

        var box = new HBox(grid);
        HBox.setMargin(grid, new Insets(0, 10, 0, 10));
        this.setContent(box);

    }

    /**
     * Returns an accordion that contains a WebView with specified content.
     * @param title title of the accordion
     * @param html content of the WebView
     * @return accordion that contains a WebView with specified content
     */
    public static Accordion getWebviewAccordion(String title, String html) {

        WebView view = new WebView();
        view.getEngine().loadContent("<style>body { font-size: 12px }</style>" + html);
        view.setStyle("");
        Accordion acc = new Accordion();
        view.setMaxSize(800, 800);
        view.setMinSize(200, 150);
        view.setPrefSize(300, 200);
        TitledPane titledPane1 = new TitledPane(title, view);
        acc.getPanes().add(titledPane1);

        return acc;
    }
}
