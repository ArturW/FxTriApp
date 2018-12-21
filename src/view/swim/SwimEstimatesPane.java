/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.swim;

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
import javafx.util.converter.LongStringConverter;
import static main.StyleSheetConstants.BOXLAYOUTS;
import static main.StyleSheetConstants.BUTTONS;
import static main.StyleSheetConstants.CELLHEADER;
import static main.StyleSheetConstants.LABELS;
import static main.StyleSheetConstants.MAINNODE;
import static main.StyleSheetConstants.TEXTFIELDS;
import tritools.models.estimates.EstimatesModel;
import tritools.util.PoolLength;

import tritools.util.Util;
import tritools.zones.IDistance;
import tritools.zones.SwimDistance;

/**
 *
 * @author user
 */
public class SwimEstimatesPane extends VBox {
    protected final Logger LOG = Logger.getLogger(this.getClass().getName());
    
    private static final int CHAR_LIMIT = 3;
    
    private final ObservableMap<IDistance, Duration> observableResultsMap = FXCollections.observableHashMap();
    //MapProperty<Distance, Duration> mapProp = new SimpleMapProperty<>();
    //private final ObservableList<Distance> distanceList = FXCollections.observableArrayList();
    private final ObservableList<String> observableDurationList = FXCollections.observableArrayList();
    private final ObservableList<String> observableDistanceList = FXCollections.observableArrayList();
    private final ObservableList<String> observable100mPaceList = FXCollections.observableArrayList();
    private final ObservableList<String> observable50mPaceList = FXCollections.observableArrayList();
    private final ObservableList<String> observable25mPaceList = FXCollections.observableArrayList();
    
    //private final List<String> list;
    
    private final EstimatesModel model;
    public SwimEstimatesPane(EstimatesModel  model) {
        this.model = model;
        
        Label label = new Label("Enter a recent race time (or estimated time)");                
        label.getStyleClass().add(LABELS);
        VBox distanceBox = new VBox();
        Label distanceLabel = new Label("Distance");
        distanceLabel.getStyleClass().add(LABELS);
        List<String> list = Stream.of(SwimDistance.values())
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
        
        // Remove "Event" title and add "Race"
        observableDistanceList.remove(0);
        observableDistanceList.add(0, "Race");
        distanceListView.setItems(observableDistanceList);                
        distanceListView.setCellFactory((ListView<String> value) -> {
            return new ListCell<String>(){
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);                    
                    if ("Race".equals(item)) {
                        this.getStyleClass().add(CELLHEADER);
                    }
                    setText(item);                    
                }
            };
        });
        
        ListView<String> durationListView = new ListView<>();
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
        
        // Add header to the list
        observable100mPaceList.add("Pace/100m");
        ListView<String> _100mPaceListView = new ListView<>(observable100mPaceList);
        _100mPaceListView.setCellFactory((ListView<String> value) -> {
            return new ListCell<String>(){
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);                    
                    if ("Pace/100m".equals(item)) {
                        this.getStyleClass().add(CELLHEADER);
                    }
                    setText(item);                    
                }
            };
        });
        
        observable50mPaceList.add("Pace/50m");
        ListView<String> _50mPaceListView = new ListView<>(observable50mPaceList);
        _50mPaceListView.setCellFactory((ListView<String> value) -> {
            return new ListCell<String>(){
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);                    
                    if ("Pace/50m".equals(item)) {
                        this.getStyleClass().add(CELLHEADER);
                    }
                    setText(item);                    
                }
            };
        });
        
        observable25mPaceList.add("Pace/25m");
        ListView<String> _25mPaceListView = new ListView<>(observable25mPaceList);
        _25mPaceListView.setCellFactory((ListView<String> value) -> {
            return new ListCell<String>(){
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);                    
                    if ("Pace/25m".equals(item)) {
                        this.getStyleClass().add(CELLHEADER);
                    }
                    setText(item);                    
                }
            };
        });
        
        outputBox.getChildren().addAll( distanceListView, durationListView,
                _100mPaceListView, _50mPaceListView, _25mPaceListView);                                        
        
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
            IDistance dist = SwimDistance.ofDistance(distanceChoiceBox.getValue());
            Duration duration = getDurationFromFields(hrsField, minField, secField);                        
            
            Map<IDistance, Duration> results = model.calculate(dist, duration);            
            //Map<Distance, Duration> results = (Map)model.apply(dist, duration);
            observableResultsMap.putAll(results);
            
            // Remove header
            observableDurationList.remove(1, observableDurationList.size());
            observable100mPaceList.remove(1, observable100mPaceList.size());
            observable50mPaceList.remove(1, observable50mPaceList.size());
            observable25mPaceList.remove(1, observable25mPaceList.size());            
            
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
                .map(a -> Util.swimPace(a.getKey(), a.getValue(), PoolLength._100m))
                .forEach(a -> {                
                    observable100mPaceList.add(Util.formatSwimPace(a));                    
                        });
            
            observableResultsMap.entrySet().stream()                
                .sorted((i, j) -> {
                    if (i.getValue().getSeconds() < j.getValue().getSeconds())
                        return -1;
                    else {
                        return 1;
                    }
                })
                .map(a -> Util.swimPace(a.getKey(), a.getValue(), PoolLength._50m))
                .forEach(a -> {                
                    observable50mPaceList.add(Util.formatSwimPace(a));                    
                        }); 
            
            observableResultsMap.entrySet().stream()                
                .sorted((i, j) -> {
                    if (i.getValue().getSeconds() < j.getValue().getSeconds())
                        return -1;
                    else {
                        return 1;
                    }
                })
                .map(a -> Util.swimPace(a.getKey(), a.getValue(), PoolLength._25m))
                .forEach(a -> {                
                    observable25mPaceList.add(Util.formatSwimPace(a));                    
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
