package com.example.fuzzylogicsimulation;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.rule.FuzzyRuleSet;


public class Engine {

    private Stage stage;
    private FIS fis;
    private double currentBrightness;
    private CheckBox checkBox;
    private MySlider batteryLevel;
    private MySlider lightIntensity;
    private MySlider screenBrightness;
    private Rectangle screen;
    private double h = 50;
    private double s = 0.05;
    private double b = 0.5;
    private Label batteryLvl;
    private Label lightIntensityLvl;
    private Label screenBrightnessLvl;


    public Engine(Stage stage){
        this.stage = stage;;
        this.fis = FIS.load("src/main/resources/FuzzyControl.fcl",false);
        FuzzyRuleSet fuzzyRuleSet = fis.getFuzzyRuleSet();
        fuzzyRuleSet.setVariable("poziom_naladowania", 50);
        fuzzyRuleSet.setVariable("poziom_jasnosci", this.b*100);
        fuzzyRuleSet.setVariable("natezenie_swiatla", 50);
        fuzzyRuleSet.evaluate();
        this.currentBrightness = fuzzyRuleSet.getVariable("zmiana_jasnosci").defuzzify();
        fuzzyRuleSet.setVariable("poziom_jasnosci", currentBrightness*100);

        VBox box1 = new VBox();
        this.checkBox = new CheckBox("Auto-adjusting");
        checkBox.setSelected(true);
        checkBox.setOnAction(e -> {
            if(checkBox.isSelected()){
                this.screenBrightness.setDisable(true);
                this.update();
            }
            else{
                this.screenBrightness.setDisable(false);
            }
        });

        checkBox.setLayoutX(45);
        checkBox.setLayoutY(37);

        Rectangle phoneCase = new Rectangle(30,20, 280, 500);
        phoneCase.setFill(Color.rgb(66,71,75));
        this.screen = new Rectangle(40, 28, 260, 440);
        screen.setFill(Color.hsb(50, 0.05, this.currentBrightness));
        Circle button = new Circle(15, Color.rgb(73, 79, 85));
        Circle edge = new Circle(16, Color.rgb(146,146,146));
        button.setCenterX(170);
        button.setCenterY(495);
        edge.setCenterX(170);
        edge.setCenterY(495);
        Pane pane = new Pane();
        pane.getChildren().addAll(phoneCase, screen, checkBox, edge, button);

        this.batteryLevel = new MySlider("poziom_naladowania",0, 100, 50, this, fis);
        this.lightIntensity = new MySlider("natezenie_swiatla", 0, 100, 50, this, fis);
        this.screenBrightness = new MySlider("poziom_jasnosci", 20, 100, this.b*100, this, fis);
        this.screenBrightness.setDisable(true);
        this.screenBrightness.setMinWidth(250);

        this.batteryLvl = new Label(Integer.toString((int) this.batteryLevel.getValue()));
        this.lightIntensityLvl = new Label(Integer.toString((int) this.lightIntensity.getValue()));
        this.screenBrightnessLvl = new Label(Integer.toString((int) ((this.screenBrightness.getValue()-20)*1.25)));
        this.batteryLvl.setMinWidth(20);
        box1.setSpacing(20);
        box1.setMinWidth(300);
        box1.setMaxWidth(300);

        String currDirectory = System.getProperty("user.dir");
        Image img1 = new Image(currDirectory + "\\src\\main\\resources\\icons\\battery_h_25.png");
        Image img2 = new Image(currDirectory + "\\src\\main\\resources\\icons\\brightness_25.png");
        Image img3 = new Image(currDirectory + "\\src\\main\\resources\\icons\\luminescense_25.png");

        ImageView image1 = new ImageView(img1);
        ImageView image2 = new ImageView(img2);
        ImageView image3 = new ImageView(img3);

        image1.setFitHeight(17);
        image1.setFitWidth(30);
        image2.setFitHeight(34);
        image2.setFitWidth(31);
        image3.setFitHeight(34);
        image3.setFitWidth(31);

        GridPane gridpane = new GridPane();
        gridpane.add(batteryLvl, 2,0);
        gridpane.add(lightIntensityLvl, 2, 1);
        gridpane.add(screenBrightnessLvl, 2, 2);
        gridpane.add(this.batteryLevel, 1,0);
        gridpane.add(this.lightIntensity, 1, 1);
        gridpane.add(this.screenBrightness, 1, 2);
        gridpane.add(image1, 0, 0);
        gridpane.add(image2, 0, 1);
        gridpane.add(image3, 0, 2);
        gridpane.setMinWidth(300);
        gridpane.setMaxWidth(300);
        gridpane.setVgap(20);
        gridpane.setHgap(20);
        box1.getChildren().addAll(pane, gridpane);
        box1.setPadding(new Insets(10, 20, 10, 20));
        Scene sc = new Scene(box1, 400, 700);
        stage.setTitle("Fuzzy Logic Simulator");
        stage.setScene(sc);
    }


    public void update(){
        if(this.checkBox.isSelected()){
            while(true){
                this.fis.getFuzzyRuleSet().evaluate();
                double brightnessChange = this.fis.getFuzzyRuleSet().getVariable("zmiana_jasnosci").defuzzify();
                if (Math.abs(brightnessChange - 0.5) > 0.01) {
                    if (brightnessChange < 0.5) {
                        this.b -= (1 - this.b) * (0.5 - brightnessChange);
                    } else if (brightnessChange > 0.5) {
                        this.b += (1 - this.b) * (brightnessChange - 0.5);
                    }
                    this.screenBrightness.setActive(false);
                    this.screenBrightness.setValue(this.b * 100);
                    this.screenBrightness.setActive(true);
                    this.screen.setFill(Color.hsb(this.h, this.s, this.b));
                }
                else {
                    break;
                }
            }
        }
        else{
            this.screen.setFill(Color.hsb(this.h, this.s, this.screenBrightness.getValue()/100));
        }
        this.batteryLvl.setText(Integer.toString((int) this.batteryLevel.getValue()));
        this.lightIntensityLvl.setText(Integer.toString((int) this.lightIntensity.getValue()));
        this.screenBrightnessLvl.setText(Integer.toString((int) ((this.screenBrightness.getValue()-20)*1.25)));
    }

    public CheckBox getCheckBox(){
        return this.checkBox;
    }

    public void run(){
        stage.show();
    }
}
