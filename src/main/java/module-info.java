module com.example.fuzzylogicsimulation {
    requires javafx.controls;
    requires javafx.fxml;
    requires jFuzzyLogic;


    opens com.example.fuzzylogicsimulation to javafx.fxml;
    exports com.example.fuzzylogicsimulation;
}