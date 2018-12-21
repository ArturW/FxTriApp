/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.zones;

import java.util.stream.Stream;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import static main.StyleSheetConstants.THRESHOLDSLIDER;
import tritools.models.zones.RunThresholdModel;
import tritools.zones.RunZones;
import tritools.zones.ZoneNames;


/**
 *
 * @author user
 */
public class RunThresholdPane extends ZonesPane {
    
    private static final String LABEL = "Run Threshold: ";
    private final String defaultValue = "runThreshold";

    private final IntegerProperty thresholdProperty;
    
    public RunThresholdPane() {
        super(LABEL);                
        /* Use properties first time, and model after */
        final int value = Integer.valueOf((String)properties.get(defaultValue));
        this.thresholdNameLabel.setText(LABEL + value + " bpm");
        
        thresholdProperty = new SimpleIntegerProperty(this, defaultValue, value);
        thresholdProperty.bindBidirectional(slider.valueProperty());        
                   
        newModel(value);
        slider.getStyleClass().add(THRESHOLDSLIDER);
        slider.setMin(120);
        slider.setMax(180);
        slider.setValue(value);        
        slider.valueProperty().addListener(
                (observable, oldValue, newValue) -> {
                    //LOG.info("Slider: " + observable + ": " + oldValue + "->" + newValue);                    
                    thresholdNameLabel.setText(LABEL + thresholdProperty.get() + " bpm");                    
                    newModel(newValue.intValue());                    
                });                
    }
    
    private void newModel(final int value) {
        //RunThresholdModel model = ModelUtil.newRunThresholdModel(value);
        //Model model = ModelUtil.getModel(ModelType.RUNMODEL, value);
        RunThresholdModel model = RunThresholdModel.of(value);
        
        Stream.of(ZoneNames.values())
            .forEach(i -> {                                                                                
                RunZones zone = RunZones.valueOf(i.name());
                //TitledPane titledPane = zonesMap.get(zone.toString());

                String range;                
                if (zone.equals(zone.ZONE1)) {
                    range = " < " + String.valueOf(zone.getZone()) + ": "
                            + String.format("%.0f bpm", model.getZones().get(i.name()));                        
                } else if (zone.equals(zone.ZONE5B)) {
                    range = " > " + String.valueOf(zone.getZone()) + ": "
                            + String.format("%.0f bpm", model.getZones().get(i.name()));
                } else {                                       
                    RunZones upper = Stream.of(RunZones.values())
                            .filter(p -> p.equals(zone))
                            .findFirst()
                            .get();
                    RunZones lower = Stream.of(RunZones.values())
                            .filter(p -> upper.ordinal() == (p.ordinal() + 1))
                            .findFirst()
                            .get();

                    range = String.valueOf(lower.getZone())
                            + " - " + String.valueOf(upper.getZone()) + ": "
                            + String.format("%.0f - %.0f bpm",
                                    model.getZones().get(lower.name()),
                                    model.getZones().get(upper.name()));
                }
                rangeMap.get(i.getZone()).setText(range);                
            });   
    }
    
}
