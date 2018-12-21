/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.info;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import static main.StyleSheetConstants.BUTTONS;
import static main.StyleSheetConstants.LABELS;
import static main.StyleSheetConstants.MAINNODE;
import static main.StyleSheetConstants.SPINNERS;
import static main.StyleSheetConstants.TEXTFIELDS;
import tritools.util.TriFunction;
import tritools.util.Util;

/**
 *
 * @author user
 */
public class ProgressionPane extends VBox {
    private static final int CHAR_LIMIT = 3;
    
    private final ObservableList<String> observableList = FXCollections.observableArrayList();
    private final ListView<String> listView = new ListView<>();
    private final TriFunction model;
    
    public ProgressionPane(TriFunction model) {
        this.model = model;
        this.setSpacing(10);
        HBox inputLayout = new HBox(10);
        
        Label startLabel = new Label("Start");
        startLabel.getStyleClass().add(LABELS); 
        TextField startTextField = new TextField();
        startTextField.getStyleClass().add(TEXTFIELDS);
        startTextField.setPromptText("min");
        startTextField.setMaxWidth(50);       
                
        Label endLabel = new Label("Finish");
        endLabel.getStyleClass().add(LABELS);
        TextField endTextField = new TextField();
        endTextField.getStyleClass().add(TEXTFIELDS);
        endTextField.setPromptText("min");
        endTextField.setMaxWidth(50);
        
        Label weeksLabel = new Label("Weeks");
        weeksLabel.getStyleClass().add(LABELS);
        Spinner<Integer> weeksSpinner = new Spinner<>(3, 12, 3);
        weeksSpinner.getStyleClass().add(SPINNERS);
        weeksSpinner.setMaxWidth(80);
        
        weeksSpinner.valueProperty().addListener((listener, oldValue, newValue) -> {
                    //System.out.println("Listener(weeks) " + newValue);
                    
                });
        
        List<TextField> textFields = new ArrayList<>(2);
        textFields.add(startTextField);
        textFields.add(endTextField);
        
        textFields.stream()
            .forEach(field -> {
                field.textProperty().addListener((listener, oldValue, newValue) -> 
                    checkField(field, oldValue, newValue));
                field.lengthProperty().addListener((observable, oldValue, newValue) -> 
                    checkFieldLength(field));
                }
            );
        
        ToggleButton timeToggle = new ToggleButton("Time");        
        ToggleButton distanceToggle = new ToggleButton("Distance");
        ToggleGroup toggleGroup = new ToggleGroup();
        timeToggle.setToggleGroup(toggleGroup);
        distanceToggle.setToggleGroup(toggleGroup);
        toggleGroup.selectToggle(timeToggle);
                
        timeToggle.setOnMouseClicked(value -> {
            startTextField.setPromptText("min");
            endTextField.setPromptText("min");
        });
        
        distanceToggle.setOnMouseClicked(value -> {
            startTextField.setPromptText("km");
            endTextField.setPromptText("km");
        });
        
        Button submitButton = new Button("Submit");
        submitButton.getStyleClass().add(BUTTONS);
        submitButton.setOnAction(value -> {
            String startStr = startTextField.getText();            
            if (startStr.isEmpty())
                startStr = "0";
            String endStr = endTextField.getText();
            if (endStr.isEmpty())
                endStr = "0";
            
            int start = Integer.parseInt(startStr);
            int end = Integer.parseInt(endStr);
            int interval = (int)weeksSpinner.getValue();
            List<Double> progressionValues = (List)model.apply(start, end, interval);
            
            Toggle selected = toggleGroup.getSelectedToggle();            
            if (selected.equals(timeToggle)) {
                List<String> stringProgressionTimes = Util.convertListOfMinutes(progressionValues);                
                observableList.setAll("Progression times");
                observableList.addAll(stringProgressionTimes);
                
            }
            if (selected.equals(distanceToggle)) {
                //Parse Double to String
                List<String> stringProgressionDistances = progressionValues.stream()
                        .map(each -> String.format("%.1f", each))
                        .collect(Collectors.toList());
                observableList.setAll("Progression distances");
                observableList.addAll(stringProgressionDistances);
            }
            
            });
           
        HBox toggleBox = new HBox(0);
        toggleBox.getChildren().addAll(timeToggle, distanceToggle);
        
        HBox listBox = new HBox(10);
        //observableList.setAll(startProperty);        
        listView.setItems(observableList);        
        listBox.getChildren().addAll(listView);
        
        inputLayout.getChildren().addAll(startLabel,  startTextField,
                endLabel, endTextField,
                weeksLabel, weeksSpinner,
                submitButton, toggleBox);
        
        this.getChildren().addAll(inputLayout, listBox);
        this.getStyleClass().add(MAINNODE);
    }
    
    private boolean checkField(TextField field, String oldValue, String newValue ) {            
            if (newValue.matches("[0-9]*")) {                
                field.setText(newValue);
                return true;
            } else {
                field.setText(oldValue);
                return false;
            }
    }
    
    private void checkFieldLength(TextField field) {
        if (field.getText().length() > CHAR_LIMIT) {
            field.setText(field.getText().substring(0, CHAR_LIMIT));
        }
    }
    
}
