/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.zones;

import view.info.WorkoutsTextPane;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.stream.Stream;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import static main.StyleSheetConstants.BOXLAYOUTS;
import static main.StyleSheetConstants.LABELS;
import static main.StyleSheetConstants.ZONESLIDER;
import static main.StyleSheetConstants.ZONESTITLEDPANE;
import static main.StyleSheetConstants.ZONESTITLEDPANEHEADER;
import static main.StyleSheetConstants.ZONESTITLEDPANETEXT;
import tritools.zones.ZoneNames;

/**
 *
 * @author user
 */
public abstract class ZonesPane extends HBox {
    protected final Logger LOG = Logger.getLogger(this.getClass().getName());
    
    private static final String PROPERTIES_FILE = "/resources/default.properties";
    protected static final Properties properties = new Properties();
    
    //protected static final String STYLE = new String("-fx-background-color: #B8CFE5");    
    
    protected final Label thresholdNameLabel;   
    protected final Map<String, Label> rangeMap;
    protected final Map<String, TitledPane> zonesMap;          
    protected final Slider slider;
    
    //static WorkoutsPane workouts = new WorkoutsPane();
        
    public ZonesPane(final String str) {       
               
        try {
            properties.load(
                    ZonesPane.class.getResourceAsStream((PROPERTIES_FILE)));
        } catch(IOException ex) {
            LOG.severe("No file found: " + PROPERTIES_FILE + ": " + str);
        }
                
        final VBox leftBox = new VBox();
        leftBox.getStyleClass().add(BOXLAYOUTS);
        leftBox.setMaxWidth(400);
        leftBox.setMinWidth(400);       
       
        thresholdNameLabel = new Label(str);
        thresholdNameLabel.getStyleClass().add(LABELS);
        leftBox.getChildren().add(thresholdNameLabel);
        
        slider = new Slider();
        slider.getStyleClass().add(ZONESLIDER);                
        slider.requestFocus();        
        leftBox.getChildren().add(slider);
        
        rangeMap = new LinkedHashMap<>();
        zonesMap = new LinkedHashMap<>();              
        
        Stream.of(ZoneNames.values())
            .forEach(i -> {                
                Label zone = new Label("");
                zone.getStyleClass().add(ZONESTITLEDPANETEXT);                
                VBox pane = new VBox();
                pane.getStyleClass().add(BOXLAYOUTS);
                pane.getChildren().add(zone);
                rangeMap.put(i.getZone(), zone);
                
                TitledPane titledPane = new TitledPane(i.getZone(), pane);
                pane.getStyleClass().add(ZONESTITLEDPANE);
                zonesMap.put(i.name(), titledPane);
                titledPane.getStyleClass().add(ZONESTITLEDPANEHEADER);                
                //titledPane.setTextFill(Color.web("0x0080FF"));                
                //titledPane.setStyle("-fx-border-STYLE: solid");                
                leftBox.getChildren().add(titledPane);
                });                    
                
        
          
        final VBox rightBox = new VBox();       
        rightBox.getStyleClass().add(BOXLAYOUTS);      
        
        WorkoutsTextPane workouts = new WorkoutsTextPane();        
        rightBox.getChildren().add(workouts);
        this.getChildren().addAll(leftBox, rightBox);                    
    
    }
    
    public double getThresholdValue() {
        return slider.getValue();
    }
    
    
}
