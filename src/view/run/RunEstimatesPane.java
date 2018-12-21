/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.run;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javafx.util.converter.LongStringConverter;
import static main.StyleSheetConstants.BOXLAYOUTS;
import static main.StyleSheetConstants.BUTTONS;
import static main.StyleSheetConstants.CELLHEADER;
import static main.StyleSheetConstants.LABELS;
import static main.StyleSheetConstants.MAINNODE;
import static main.StyleSheetConstants.TEXTFIELDS;
import tritools.models.estimates.EstimatesModel;


import tritools.util.Util;
import tritools.zones.IDistance;
import tritools.zones.RunDistance;

/**
 *
 * @author user
 */
public class RunEstimatesPane extends VBox {
    protected final Logger LOG = Logger.getLogger(this.getClass().getName());
    
    private static final int CHAR_LIMIT = 3;
    
    private final ObservableMap<IDistance, Duration> observableResultsMap = FXCollections.observableHashMap();
    //MapProperty<Distance, Duration> mapProp = new SimpleMapProperty<>();
    //private final ObservableList<Distance> distanceList = FXCollections.observableArrayList();
    private final ObservableList<String> observableDurationList = FXCollections.observableArrayList();
    private final ObservableList<String> observableDistanceList = FXCollections.observableArrayList();
    private final ObservableList<String> observableKmPaceList = FXCollections.observableArrayList();
    private final ObservableList<String> observableMiPaceList = FXCollections.observableArrayList();
    
    private final List<String> list;
    
    private final EstimatesModel model;
    public RunEstimatesPane(EstimatesModel  model) {
        this.model = model;
        
        Label label = new Label("Enter a recent race time (or estimated time)");                
        label.getStyleClass().add(LABELS);
        VBox distanceBox = new VBox();
        Label distanceLabel = new Label("Distance");
        distanceLabel.getStyleClass().add(LABELS);
        list = Stream.of(RunDistance.values())
                .map(m -> m.getName())
                .collect(Collectors.toList());        
        list.add(0, "Event");
        ChoiceBox<String> distanceChoiceBox = new ChoiceBox<>(FXCollections.observableArrayList(list));
        //distanceChoiceBox.setValue(RunDistance.valueOf("_5").getName());
        distanceChoiceBox.setValue("Event");
        distanceBox.getChildren().addAll(distanceLabel, distanceChoiceBox);
        distanceBox.getStyleClass().add(BOXLAYOUTS);
        
        VBox timeBox = new VBox();
        timeBox.getStyleClass().add(BOXLAYOUTS);
        Label timeLabel = new Label("Time");
        timeLabel.getStyleClass().add(LABELS);
        HBox timeInputBox = new HBox();
        TextField hrsField = new TextField();
        hrsField.getStyleClass().add(TEXTFIELDS);               
        hrsField.setPromptText("hrs");
        hrsField.setMaxWidth(50);
        TextField minField = new TextField();
        minField.getStyleClass().add(TEXTFIELDS);
        minField.setPromptText("min");
        minField.setMaxWidth(50);
        TextField secField = new TextField();
        secField.getStyleClass().add(TEXTFIELDS);               
        secField.setPromptText("sec");
        secField.setMaxWidth(50);
        timeInputBox.getChildren().addAll(hrsField, minField, secField);
        timeBox.getChildren().addAll(timeLabel, timeInputBox);        
        Button estimateButton = new Button("Estimate");
        estimateButton.getStyleClass().add(BUTTONS);                              
        HBox inputBox = new HBox();
        inputBox.getStyleClass().add(BOXLAYOUTS);                              
        inputBox.getChildren().addAll(distanceBox, timeBox, estimateButton);
                
        HBox outputBox = new HBox();
                
        ListView<String> distanceListView = new ListView<>();
        list.stream()
                .forEach(a -> observableDistanceList.add(a));
        observableDistanceList.remove(0);
        observableDistanceList.add(0, "Race");
        distanceListView.setItems(observableDistanceList);                        
        distanceListView.setCellFactory(new Callback<ListView<String>,
            ListCell<String>>() {
                @Override
                public ListCell<String> call(ListView<String> param) {
                    ListCell<String> cell = new ListCell<String>() {
                        @Override
                        public void updateItem(String item, boolean empty) {
                            super.updateItem(item, empty);
                            if ("Race".equals(item)) {
                                this.getStyleClass().add(CELLHEADER);
                            }
                            setText(item);
                        }
                    };
                    return cell;
                }
            });
        /* Using lambdas */
        /*
        distanceListView.setCellFactory((ListView<String> value) -> {
            return new ListCell();
        });
        */
        
        ListView<String> durationListView = new ListView<>();
        //Clear
        observableDistanceList.stream()
                .forEach(a -> observableDurationList.add(""));
        observableDurationList.add(0, "Time");
        durationListView.setItems(observableDurationList);
        durationListView.setCellFactory((ListView<String> value) -> {
            return new ListCell<String>(){
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);                    
                    if ("Time".equals(item)) {
                        this.getStyleClass().add(CELLHEADER);
                    }
                    setText(item);                    
                }
            };
        });
        
        observableKmPaceList.add("Pace/Km");
        ListView<String> kmPaceListView = new ListView<>(observableKmPaceList);
        kmPaceListView.setCellFactory((ListView<String> value) -> {
            return new ListCell<String>(){
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);                    
                    if ("Pace/Km".equals(item)) {
                        this.getStyleClass().add(CELLHEADER);
                    }
                    setText(item);                    
                }
            };
        });
        
        observableMiPaceList.add("Pace/Mi");
        ListView<String> miPaceListView = new ListView<>(observableMiPaceList);
        miPaceListView.setCellFactory((ListView<String> value) -> {
            return new ListCell<String>(){
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);                    
                    if ("Pace/Mi".equals(item)) {
                        this.getStyleClass().add(CELLHEADER);
                    }
                    setText(item);                    
                }
            };
        });
        
        outputBox.getChildren().addAll( distanceListView, durationListView,
                kmPaceListView, miPaceListView);
                
        this.getChildren().addAll(label, inputBox, outputBox);
        this.getStyleClass().add(MAINNODE);
        
        List<TextField> textFields = new ArrayList<>(3);
        textFields.add(hrsField);
        textFields.add(minField);
        textFields.add(secField);
        
        textFields.stream()
            .forEach(field -> {
                field.textProperty().addListener((listener, oldValue, newValue) -> 
                    checkField(field, oldValue, newValue));
                field.lengthProperty().addListener((observable, oldValue, newValue) -> 
                    checkFieldLength(field));
                }
            );
        
        estimateButton.setOnMouseReleased(value -> {
            LOG.log(Level.INFO, value.getSource().toString());
            if ("Event".equals(distanceChoiceBox.getValue()))
                return;
            IDistance dist = RunDistance.ofDistance(distanceChoiceBox.getValue());
            Duration duration = getDurationFromFields(hrsField, minField, secField);                        
            
            Map<IDistance, Duration> results = model.calculate(dist, duration);            
            //Map<Distance, Duration> results = (Map)model.apply(dist, duration);
            observableResultsMap.putAll(results);
            
            observableDurationList.remove(1, observableDurationList.size());
            observableKmPaceList.remove(1, observableKmPaceList.size());
            observableMiPaceList.remove(1, observableMiPaceList.size());
            
            observableResultsMap.values().stream()
                .sorted((a, b) -> {
                    if (a.getSeconds() < b.getSeconds())
                        return -1;
                    else {
                        return 1;
                    }
                })
                .forEach(a -> observableDurationList
                        .add(Util.convertSecondsToPrintable(a.getSeconds())));                        
            
            observableResultsMap.entrySet().stream()                
                .sorted((i, j) -> {
                    if (i.getValue().getSeconds() < j.getValue().getSeconds())
                        return -1;
                    else {
                        return 1;
                    }
                })
                .map(a -> Util.runPace(a.getKey(), a.getValue()))
                .forEach(a -> {                
                    observableKmPaceList.add(Util.formatKmPace(a));
                    observableMiPaceList.add(Util.formatMiPace(a));
                        });                               
        });
        
    }
    
    /*
    * Accept only numerical inputs
    */
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
    
    private Duration getDurationFromFields(TextField hrsField, TextField minField, TextField secField) {
       if ( hrsField.getText().equals("")) {
           hrsField.setText("0");
       }
       if ( minField.getText().equals("")) {
           minField.setText("0");
       }
       if ( secField.getText().equals("")) {
           secField.setText("0");
       }
        
       //long hrs = Long.parseLong(hrsField.getText());
       //long min = Long.parseLong(minField.getText());
       //long sec = Long.parseLong(secField.getText());
       long hrs = new LongStringConverter().fromString(hrsField.getText());
       long min = new LongStringConverter().fromString(minField.getText());
       long sec = new LongStringConverter().fromString(secField.getText());
       return Duration.ofHours(hrs).plusMinutes(min).plusSeconds(sec);                             
    }
    
}
