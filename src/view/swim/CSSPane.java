/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.swim;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.converter.LongStringConverter;

import static main.StyleSheetConstants.*;
import tritools.models.css.CSSModel;
import tritools.zones.RunDistance;
/**
 *
 * @author user
 */
public class CSSPane extends HBox {

    private static final int CHAR_LIMIT = 3;
    
    private final ObservableMap<RunDistance, Duration> observableResultsMap = FXCollections.observableHashMap();    
    private final ObservableList<String> observableDurationList = FXCollections.observableArrayList();            
    
    private final CSSModel model;
    
    public CSSPane(CSSModel model) {
        this.model = model;
        setupView();
        //setUpListeners();
    }
            
    private void setupView() {    
        VBox leftBox = new VBox();
        this.getChildren().add(leftBox);
        
        final Label titleLabel = new Label("Splits");
        titleLabel.getStyleClass().add(LABELS);
        final HBox topHBox= new HBox(titleLabel);        
        //firstLineHBox.setPadding(Insets.EMPTY);
        topHBox.getStyleClass().add(BOXLAYOUTS);
        
        final Label label400 = new Label("400m timetrial");
        label400.getStyleClass().add(LABELS);
        final TextField min400Field = new TextField();        
        min400Field.getStyleClass().add(TEXTFIELDS);
        min400Field.setPromptText("min");
        min400Field.setMaxWidth(50);        
        //min400Field.setId("min400Field");                
        final TextField sec400Field = new TextField();
        sec400Field.getStyleClass().add(TEXTFIELDS);
        sec400Field.setPromptText("sec");
        sec400Field.setMaxWidth(50);        
        final HBox m400HBox = new HBox(label400, min400Field, sec400Field);
        m400HBox.getStyleClass().add(BOXLAYOUTS);
        
        final Label label200 = new Label("200m timetrial");
        label200.getStyleClass().add(LABELS);
        final TextField min200Field = new TextField();
        min200Field.getStyleClass().add(TEXTFIELDS);
        min200Field.setPromptText("min");
        min200Field.setMaxWidth(50);
        min200Field.getOnKeyTyped();
        final TextField sec200Field = new TextField();
        sec200Field.getStyleClass().add(TEXTFIELDS);
        sec200Field.setPromptText("sec");
        sec200Field.setMaxWidth(50);
                            
        final Button calculateButton = new Button("Calculate");
        calculateButton.getStyleClass().add(BUTTONS);        
        final HBox m200Box = new HBox(label200, min200Field, sec200Field,
            calculateButton);
        m200Box.getStyleClass().add(BOXLAYOUTS);
                
        leftBox.getChildren().addAll(topHBox, m400HBox, m200Box);
        leftBox.getStyleClass().add(MAINNODE);
        
        
        final Label cssLabel = new Label("CSS Threshold: ");
        cssLabel.getStyleClass().add(LABELS);
        final Label valueLabel = new Label();
        valueLabel.getStyleClass().add(LABELS);                
        final HBox cssBox = new HBox(cssLabel, valueLabel);
                
        final Label dropoffLabel = new Label("Dropoff: ");
        dropoffLabel.getStyleClass().add(LABELS);
        final Label dropoffValueLabel = new Label();
        dropoffValueLabel.getStyleClass().add(LABELS);
        final HBox dropoffBox = new HBox(dropoffLabel, dropoffValueLabel);
        
        leftBox.getChildren().addAll(cssBox, dropoffBox);
        
        StringProperty cssProperty = new SimpleStringProperty(this, "CSS Pace");
        //valueLabel.textProperty().bindBidirectional(cssProperty);
        valueLabel.textProperty().bind(cssProperty);
        
        cssProperty.addListener(listener -> {
            System.out.println(listener);
        });

        StringProperty dropoffProperty = new SimpleStringProperty(this, "Dropoff");
        dropoffValueLabel.textProperty().bind(dropoffProperty);
        
        dropoffProperty.addListener(listener -> {
            System.out.println(listener);
        });
        
        List<TextField> textFields = new ArrayList<>(4);
        textFields.add(min400Field);
        textFields.add(min200Field);
        textFields.add(sec400Field);
        textFields.add(sec200Field);
            
        VBox rightBox = new VBox();
        this.getChildren().add(rightBox);
        CSSPacesPane pacesPane = new CSSPacesPane();
        rightBox.getChildren().add(pacesPane);
        /*
        ChangeListener listener = (obsVal, oldVal, newVal) -> {                        
            TextField textField = (TextField)((StringProperty)obsVal).getBean();
            String id = textField.getId();
            switch(id) {        
            };  
        };*/        
        textFields.stream()
            .forEach(field -> {
                field.textProperty().addListener((obsVal, oldVal, newVal) -> 
                    checkFieldInput(field, oldVal, newVal));
                field.lengthProperty().addListener((observable, oldValue, newValue) -> 
                    checkFieldLength(field));
                //field.textProperty().bindBidirectional(property?, new NumberStringConverter());
            });
        
        
        calculateButton.setOnMouseReleased(value -> {                                    
            Duration duration400 = getDurationFromFields(min400Field, sec400Field);
            Duration duration200 = getDurationFromFields(min200Field, sec200Field);
            
            double css = model.calculate((int)duration400.getSeconds(),(int)duration200.getSeconds());            
            cssProperty.set(model.formatPace(css) + " min/100m");
            double drop = model.dropOff((int)duration400.getSeconds(),(int)duration200.getSeconds());
            dropoffProperty.set(model.formatPercentage(drop) + "%");
            }
        );
    }
    
    /*
    * Accept only numerical inputs
    */
    private void checkFieldInput(TextField field, String oldValue, String newValue ) {            
            if (newValue.matches("[0-9]*")) {                
                field.setText(newValue);
            } else {
                field.setText(oldValue);
            }
    }
    
    private void checkFieldLength(TextField field) {
        if (field.getText().length() > CHAR_LIMIT) {
            field.setText(field.getText().substring(0, CHAR_LIMIT));
        }
    }
    
    private Duration getDurationFromFields(TextField minField, TextField secField) {
        
       if ( minField.getText().equals("")) {
           minField.setText("0");
       }
       if ( secField.getText().equals("")) {
           secField.setText("0");
       }

       //long min = Long.parseLong(minField.getText());
       //long sec = Long.parseLong(secField.getText());
       long min = new LongStringConverter().fromString(minField.getText());
       long sec = new LongStringConverter().fromString(secField.getText());
       return Duration.ofMinutes(min).plusSeconds(sec);
    };
    
}
