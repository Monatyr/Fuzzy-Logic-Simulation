package com.example.fuzzylogicsimulation;

import javafx.scene.control.Slider;
import javafx.scene.paint.Color;
import net.sourceforge.jFuzzyLogic.FIS;

public class MySlider extends Slider {

    private String name;
    private boolean active = true;

    public MySlider(String name, int minVal, int maxVal, double value, Engine engine, FIS fis){
        this.name = name;
        this.setMax(maxVal);
        this.setMin(minVal);
        if(this.name.equals("poziom_jasnosci"))
            this.setValue(value + 10);
        else
            this.setValue(value);
        this.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(this.name.equals("poziom_jasnosci")) {
                if(newValue.intValue() < 40)
                    engine.getCheckBox().setTextFill(Color.WHITE);
                else
                    engine.getCheckBox().setTextFill(Color.BLACK);
                fis.getFuzzyRuleSet().getVariable(this.name).setValue((newValue.intValue() - 20) * 1.25);
            }
            else
                fis.getFuzzyRuleSet().getVariable(this.name).setValue(newValue.intValue());
            if(this.active) {
                engine.update();
            }
        });
    }

    public void setActive(boolean state){
        this.active = state;
    }
}
