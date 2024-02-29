package fi.tuni.prog3.sisu;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.util.Comparator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * A GUI element which allows the user to examine the structure of a
 * degree, select courses and modules and mark them completed.
 */
public class DegreeInspector extends HBox {

    /**
     * A class that overrides TreeItem's isLeaf() to only consider courses as leaves.
     */
    private static class DegreeModuleTreeItem extends TreeItem<DegreeModule> {

        public DegreeModuleTreeItem(DegreeModule degreeModule) {
            super(degreeModule);
        }

        @Override
        public boolean isLeaf() {
            return this.getValue().getClass() == Course.class;
        }
    }

    private final Student student;
    private VBox selectorWrapper;
    private final ConcurrentHashMap<String, DegreeModule> loadedModules = new ConcurrentHashMap<>();
    private final Image checkmarkImg = new Image("file:" + System.getProperty("user.dir") + "/images/checkmark.png");

    /**
     * Construct a DegreeInspector object.
     * @param student student whose selected degree is to be used
     */
    DegreeInspector(Student student) {

        this.student = student;
        DegreeModule module = student.getDegree();

        loadSelected(module);
        preloadChildren(module);

        TreeView<DegreeModule> view = new TreeView<>();
        view.setId("view");
        view.setPrefSize(5000, 3000);

        // add event listener that fires when a TreeItem is clicked
        view.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    // newValue is the clicked TreeItem
                    if (newValue.getValue().getClass() == Course.class) {
                        selectorWrapper.getChildren().clear();
                        selectorWrapper.getChildren().add(getCourseView((DegreeModuleTreeItem) newValue));
                    } else {
                        selectorWrapper.getChildren().clear();
                        selectorWrapper.getChildren().add(getSelector((DegreeModuleTreeItem) newValue));
                    }
                });

        DegreeModuleTreeItem root = new DegreeModuleTreeItem(module);
        view.setRoot(root);

        this.getChildren().add(view);

        selectorWrapper = new VBox();
        selectorWrapper.setId("selectorWrapper");
        selectorWrapper.getChildren().add(getSelector(root));

        this.getChildren().add(selectorWrapper);

    }

    /**
     * Returns a VBox containing a selector that allows one to select courses and modules.
     * @param item tree item whose children are to be selected from among
     * @return VBox containing a selector that allows one to select courses and modules
     */
    private ScrollPane getSelector(DegreeModuleTreeItem item) {

        ScrollPane scrollPane = new ScrollPane();

        VBox vbox = new VBox();
        scrollPane.setPrefSize(5000, 3000);
        vbox.setSpacing(3);
        vbox.setPadding(new Insets(0, 10, 0, 10));

        handleRule(item.getValue().getRule(), vbox, item, true);

        scrollPane.setContent(vbox);

        return scrollPane;
    }

    /**
     * Returns a VBox containing information about a course as well as
     * the ability to mark the course as completed or not completed.
     * @param item a TreeItem that contains the course
     * @return VBox containing information about a course
     */
    private VBox getCourseView(DegreeModuleTreeItem item) {
        Course course = (Course) item.getValue();

        VBox vbox = new VBox();

        vbox.getChildren().add(new CourseInfoView(course));

        ButtonBar buttonBar = new ButtonBar();
        Button completedButton = new Button("Completed");
        Button notCompletedButton = new Button("Not completed");

        completedButton.setOnAction(actionEvent -> {
            if (student.addCourse(course)) {
                item.setGraphic(new ImageView(checkmarkImg));
            }
        });

        notCompletedButton.setOnAction(actionEvent -> {
            if (student.removeCourse(course)) {
                item.setGraphic(null);
            }
        });

        buttonBar.setPadding(new Insets(10, 10, 10, 10));

        buttonBar.getButtons().addAll(completedButton, notCompletedButton);
        vbox.getChildren().add(buttonBar);

        return vbox;
    }

    /**
     * Returns a CheckBox which allows user to select a course or module.
     * @param item TreeItem containing course or module to be selected
     * @param parent TreeItem that "item" will be added under when selected
     * @param mandatory whether this course or module is mandatory
     * @return a CheckBox which allows user to select a course or module
     */
    private CheckBox getCheckBox(DegreeModuleTreeItem item, DegreeModuleTreeItem parent, boolean mandatory) {

        CheckBox checkBox = new CheckBox(item.getValue().toString());

        // if parent's children contain the DegreeModule inside "item", this was already previously selected
        if (parent.getChildren().stream().anyMatch(t -> t.getValue().getId().equals(item.getValue().getId()))) {
            checkBox.setSelected(true);
        }

        checkBox.setOnAction(actionEvent -> {
            if (checkBox.isSelected()) {
                parent.getChildren().add(item);

                // keep order of children consistent regardless of selection order
                parent.getChildren().sort(Comparator.comparingInt(
                        elem -> parent.getValue().getGroupIdsOfChildren().indexOf(elem.getValue().getGroupId())));

                parent.setExpanded(true);

                // add DegreeModule to parent's selected DegreeModules
                parent.getValue().getSelected().put(item.getValue().getGroupId(), item.getValue());

                preloadChildren(item.getValue());
            } else {
                parent.getChildren().removeIf(t -> t.getValue().getId().equals(item.getValue().getId()));
                parent.getValue().getSelected().remove(item.getValue().getGroupId());
            }
        });

        // if mandatory, force selection
        if (mandatory) {
            if (!checkBox.isSelected()) {
                checkBox.fire();
            }
            checkBox.setDisable(true);
        }

        // select if this is in parent DegreeModule's selections
        if (parent.getValue().getSelected().containsKey(item.getValue().getGroupId())) {
            if (!checkBox.isSelected()) {
                checkBox.fire();
            }
        }

        return checkBox;
    }

    /**
     * Preloads the children of a DegreeModule from Sisu API on a separate thread to make the experience smoother.
     * @param module module whose children to preload
     */
    private void preloadChildren(DegreeModule module) {

        Thread t = new Thread(() -> {
            var ids = module.getChildren();

            for (var a : ids) {

                if (loadedModules.containsKey(a.getId())) {
                    continue;
                }

                if (a.getType().equals("Course")) {
                    try {
                        Course c = Course.fromAPI(a.getId(), true);
                        loadedModules.put(c.getGroupId(), c);
                    } catch (IOException e) {
                        System.out.println("Couldn't find course with group id " + a.getId());
                    }

                } else if (a.getType().equals("Module")) {
                    try {
                        DegreeModule m = Module.fromAPI(a.getId(), true);
                        loadedModules.put(m.getGroupId(), m);
                    } catch (IOException e) {
                        System.out.println("Couldn't find module with group id " + a.getId());
                    }
                }

            }
        });

        t.start();
    }

    /**
     * Represents a ModuleRule in the GUI.
     * @param moduleRule ModuleRule to be handled
     * @param wrapper wrapper to put the GUI elements in
     * @param item TreeItem which contains the DegreeModule containing the ModuleRule
     * @param mandatory whether satisfying this rule is mandatory
     */
    private void handleModuleRule(ModuleRule moduleRule, VBox wrapper, DegreeModuleTreeItem item, boolean mandatory) {

        DegreeModule m;

        if (loadedModules.containsKey(moduleRule.getGroupId())) {
            m = loadedModules.get(moduleRule.getGroupId());
        } else {
            try {
                m = Module.fromAPI(moduleRule.getGroupId(), true);
                loadedModules.put(m.getGroupId(), m);
            } catch (IOException e) {
                System.out.println("Couldn't find module with group id " + moduleRule.getGroupId());
                return;
            }
        }

        wrapper.getChildren().add(getCheckBox(new DegreeModuleTreeItem(m), item, mandatory));
    }

    /**
     * Represents a CourseUnitRule in the GUI.
     * @param courseUnitRule CourseUnitRule to be handled
     * @param wrapper wrapper to put the GUI elements in
     * @param item TreeItem which contains the DegreeModule containing the CourseUnitRule
     * @param mandatory whether satisfying this rule is mandatory
     */
    private void handleCourseUnitRule(CourseUnitRule courseUnitRule, VBox wrapper, DegreeModuleTreeItem item, boolean mandatory) {

        DegreeModule m;

        if (loadedModules.containsKey(courseUnitRule.getGroupId())) {
            m = loadedModules.get(courseUnitRule.getGroupId());
        } else {
            try {
                m = Course.fromAPI(courseUnitRule.getGroupId(), true);
                loadedModules.put(m.getGroupId(), m);
            } catch (IOException e) {
                System.out.println("Couldn't find module with group id " + courseUnitRule.getGroupId());
                return;
            }
        }

        DegreeModuleTreeItem courseItem = new DegreeModuleTreeItem(m);

        if (student.getCompletedCourses().contains(m.getId())) {
            courseItem.setGraphic(new ImageView(checkmarkImg));
        }

        wrapper.getChildren().add(getCheckBox(courseItem, item, mandatory));
    }

    /**
     * Represents a CompositeRule in the GUI.
     * @param compositeRule CompositeRule to be handled
     * @param wrapper wrapper to put the GUI elements in
     * @param item TreeItem which contains the DegreeModule containing the CompositeRule
     * @param mandatory whether satisfying this rule is mandatory
     */
    private void handleCompositeRule(CompositeRule compositeRule, VBox wrapper, DegreeModuleTreeItem item, boolean mandatory) {

        VBox vbox = new VBox();
        vbox.setSpacing(3);

        if (compositeRule.isAllMandatory() && compositeRule.getRules().size() > 1) {
            wrapper.getChildren().add(new Label("All mandatory"));
            vbox.setPadding(new Insets(0, 10, 10, 10));
            vbox.setStyle("-fx-border-width: 0 0 0 1;-fx-border-color: black;-fx-border-style: dashed");

        } else if (compositeRule.getRequire() != null) {

            String msg = "Select";

            if (compositeRule.getRequire().min > 0) {
                msg += String.format(" at least %d", compositeRule.getRequire().min);
            }

            if (compositeRule.getRequire().max > 0) {
                if (msg.length() > 6) {
                    msg += " and";
                }
                msg += String.format(" at most %d", compositeRule.getRequire().max);
            }

            // there are some stupid non-null requires where for example min = 0 and max = null
            if (msg.length() > 6) {
                wrapper.getChildren().add(new Label(msg));

                vbox.setPadding(new Insets(0, 10, 10, 10));
                vbox.setStyle("-fx-border-width: 0 0 0 1;-fx-border-color: black;-fx-border-style: dashed");
            }

        } else {
            System.out.println(item.getValue().getId());
        }

        boolean mandatoryCase1 = mandatory && compositeRule.getRules().size() == 1;
        boolean mandatoryCase2 = mandatory && compositeRule.isAllMandatory();

        wrapper.getChildren().add(vbox);

        for (Rule rule : compositeRule.getRules()) {
            handleRule(rule, vbox, item, mandatoryCase1 || mandatoryCase2);
        }
    }

    /**
     * Represents a CreditsRule in the GUI.
     * @param creditsRule CreditsRule to be handled
     * @param wrapper wrapper to put the GUI elements in
     * @param item TreeItem which contains the DegreeModule containing the CreditsRule
     * @param mandatory whether satisfying this rule is mandatory
     */
    private void handleCreditsRule(CreditsRule creditsRule, VBox wrapper, DegreeModuleTreeItem item, boolean mandatory) {

        VBox vbox = new VBox();
        vbox.setSpacing(3);

        if (creditsRule.getCredits() != null) {

            String msg = "";

            if (creditsRule.getCredits().min > 0) {
                msg += String.format("Minimum credits %d", creditsRule.getCredits().min);
            }

            if (creditsRule.getCredits().max > 0) {
                if (msg.length() > 0) {
                    msg += " and ";
                }
                msg += String.format("Maximum credits %d", creditsRule.getCredits().max);
            }

            wrapper.getChildren().add(new Label(msg));

            vbox.setPadding(new Insets(0, 10, 10, 10));
            vbox.setStyle("-fx-border-width: 0 0 0 1;-fx-border-color: black;-fx-border-style: dashed");
        }

        wrapper.getChildren().add(vbox);

        handleRule(creditsRule.getRule(), vbox, item, mandatory);
    }

    /**
     * Represents an AnyModuleRule in the GUI.
     * @param wrapper wrapper to put the GUI elements in
     * @param item TreeItem which contains the DegreeModule containing the AnyModuleRule
     */
    private void handleAnyModuleRule(VBox wrapper, DegreeModuleTreeItem item) {
        VBox vbox = new VBox();
        Label label = new Label("Input group id of any module");

        vbox.getChildren().addAll(label, getAnyDegreeModuleCheckBox(item, false));

        wrapper.getChildren().add(vbox);
    }


    /**
     * Represents an AnyCourseUnitRule in the GUI.
     * @param wrapper wrapper to put the GUI elements in
     * @param item TreeItem which contains the DegreeModule containing the AnyCourseUnitRule
     */
    private void handleAnyCourseUnitRule(VBox wrapper, DegreeModuleTreeItem item) {
        VBox vbox = new VBox();
        Label label = new Label("Input group id of any course");

        vbox.getChildren().addAll(label, getAnyDegreeModuleCheckBox(item, true));

        wrapper.getChildren().add(vbox);
    }

    /**
     * Represent a Rule in the GUI.
     * @param rule Rule to be handled
     * @param wrapper wrapper to put the GUI elements in
     * @param item TreeItem which contains the DegreeModule containing the Rule
     * @param mandatory whether satisfying this rule is mandatory
     */
    private void handleRule(Rule rule, VBox wrapper, DegreeModuleTreeItem item, boolean mandatory) {

        if (rule == null) {
            return;
        }

        var type = rule.getClass();

        if (type == ModuleRule.class) {
            handleModuleRule((ModuleRule) rule, wrapper, item, mandatory);
        } else if (type == CourseUnitRule.class) {
            handleCourseUnitRule((CourseUnitRule) rule, wrapper, item, mandatory);
        } else if (type == CompositeRule.class) {
            handleCompositeRule((CompositeRule) rule, wrapper, item, mandatory);
        } else if (type == CreditsRule.class) {
            handleCreditsRule((CreditsRule) rule, wrapper, item, mandatory);
        } else if (type == AnyModuleRule.class) {
            handleAnyModuleRule(wrapper, item);
        } else if (type == AnyCourseUnitRule.class) {
            handleAnyCourseUnitRule(wrapper, item);
        }
    }

    /**
     * Recursively loads all modules and courses selected by the student.
     * @param mod DegreeModule to load
     */
    private void loadSelected(DegreeModule mod) {
        for (var m : mod.getSelected().entrySet()) {
            this.loadedModules.put(m.getKey(), m.getValue());
            loadSelected(m.getValue());
        }
    }

    /**
     * Returns a GridPane containing a selector that allows one to choose any Course or Module.
     * @param item TreeItem containing the DegreeModule whose child the new Course or Module will be
     * @param isCourse true if selection is from among courses, false if from among Modules
     * @return a GridPane containing a selector that allows one to choose any Course or Module
     */
    private GridPane getAnyDegreeModuleCheckBox(DegreeModuleTreeItem item, boolean isCourse) {
        GridPane grid = new GridPane();
        CheckBox checkBox = new CheckBox();
        checkBox.setDisable(true);
        TextField textField = new TextField();

        checkBox.setOnAction(actionEvent -> {

            DegreeModule module = loadedModules.get(textField.getText());

            if (checkBox.isSelected()) {
                textField.setDisable(true);

                if (item.getChildren().stream().anyMatch(t -> t.getValue().getId().equals(module.getId()))) {
                    return;
                }

                DegreeModuleTreeItem degreeModuleTreeItem = new DegreeModuleTreeItem(module);

                if (student.getCompletedCourses().contains(module.getId())) {
                    degreeModuleTreeItem.setGraphic(new ImageView(checkmarkImg));
                }

                item.getChildren().add(degreeModuleTreeItem);

                // keep order of children consistent regardless of selection order
                item.getChildren().sort(Comparator.comparingInt(
                        elem -> item.getValue().getGroupIdsOfChildren().indexOf(elem.getValue().getGroupId())));

                item.setExpanded(true);

                // add DegreeModule to parent's selected DegreeModules
                item.getValue().getSelected().put(module.getGroupId(), module);

                preloadChildren(module);
            } else {
                textField.setDisable(false);
                item.getChildren().removeIf(t -> t.getValue().getId().equals(module.getId()));
                item.getValue().getSelected().remove(module.getGroupId());
                checkBox.setDisable(true);
            }

        });

        textField.setOnAction(actionEvent -> {
            try {
                DegreeModule mod = isCourse ? Course.fromAPI(textField.getText(), true)
                        : Module.fromAPI(textField.getText(), true);

                if (!loadedModules.containsKey(mod.getGroupId())) {
                    loadedModules.put(mod.getGroupId(), mod);
                }

                checkBox.setDisable(false);
                checkBox.fire();

            } catch (Exception e) {
                checkBox.setDisable(true);
            }
        });

        var ids = item.getValue().getSelected().keySet().stream().filter(
                a -> !item.getValue().getGroupIdsOfChildren().contains(a)
        ).collect(Collectors.toList());

        if (ids.size() > 0) {
            if (!checkBox.isSelected()) {
                textField.setText(ids.get(0));
                checkBox.setDisable(false);
                checkBox.fire();
            }
        }

        grid.add(checkBox, 0, 0);
        grid.add(textField, 1, 0);
        grid.setHgap(5);

        return grid;
    }
}
