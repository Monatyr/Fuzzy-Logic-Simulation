package com.example.fuzzylogicsimulation;

import javafx.application.Application;
import javafx.stage.Stage;
import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        stage.setResizable(false);
        Engine engine = new Engine(stage);
        engine.run();
    }

    public static void main(String[] args) {
        launch();
    }
}