module fi.tuni.prog3.sisu {
    requires javafx.controls;
    exports fi.tuni.prog3.sisu;
    requires com.google.gson;
    requires javafx.web;
    opens fi.tuni.prog3.sisu to com.google.gson;

}
