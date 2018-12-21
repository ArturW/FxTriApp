/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.zones;

import java.util.stream.Stream;
import static main.StyleSheetConstants.PACESLIDER;
import tritools.models.zones.Model;
import tritools.models.zones.RunPaceModel;
import tritools.models.ModelType;
import tritools.models.ModelUtil;
import tritools.util.Util;
import tritools.zones.PaceZones;
import tritools.zones.ZoneNames;

import static view.zones.ZonesPane.properties;

/**
 *
 * @author user
 */
public class RunPacesPane extends ZonesPane {
    
    private static final String LABEL = "Pace Threshold: ";
    private final String defaultValue = "paceThreshold";
    
    public RunPacesPane() {
        super(LABEL);
        /* Use properties first time, and model after */
        final int value = Integer.valueOf((String)properties.get(defaultValue));        
        this.thresholdNameLabel.setText(LABEL + Util.convertSecondsToPrintable(value) + " min/km");
        
        
        newModel(value);
        slider.getStyleClass().add(PACESLIDER);
        slider.setMin(2);
        slider.setMax(8);
        slider.setValue(4);       
        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
                    //LOG.info("Slider: " + observable + ": " + oldValue + "->" + newValue);
                    thresholdNameLabel.setText(LABEL
                            + Util.convertSecondsToPrintable(newValue.doubleValue() * 60)
                            + " min/km");
                    newModel((int)(newValue.doubleValue() * 60));
                });
    }
    
    private void newModel(int value) {
        //RunPaceModel model = ModelUtil.newRunPaceModel(value);
        //Model model = ModelUtil.getModel(ModelType.PACEMODEL, value);
        RunPaceModel model = RunPaceModel.of(value);
        
        Stream.of(ZoneNames.values())
            .forEach(i -> {                                                                                
                PaceZones zone = PaceZones.valueOf(i.name());                                
                //TitledPane titledPane = zonesMap.get(zone.toString());
                
                String range;
                if (zone.equals(zone.ZONE1)) {
                    String fmt = Util.convertSecondsToPrintable(model.getZones().get(i.name()));
                    range = " < " + String.valueOf(zone.getZone()) + ": "
                            + String.format("%s min", fmt);
                } else if (zone.equals(zone.ZONE5B)) {
                    String fmt = Util.convertSecondsToPrintable(model.getZones().get(i.name()));
                    range = " > " + String.valueOf(zone.getZone()) + ": "
                            + String.format("%s min", fmt);
                } else {
                    PaceZones upper = Stream.of(PaceZones.values())
                            .filter(p -> p.equals(zone))
                            .findFirst()
                            .get();
                    PaceZones lower = Stream.of(PaceZones.values())
                            .filter(p -> upper.ordinal() == (p.ordinal() + 1))
                            .findFirst()
                            .get();

                    range = String.valueOf(lower.getZone())
                            + " - " + String.valueOf(upper.getZone()) + ": "
                            + String.format("%s - %s min",
                                    Util.convertSecondsToPrintable(model.getZones().get(upper.name())),
                                    Util.convertSecondsToPrintable(model.getZones().get(lower.name())));
                                    
                }
                rangeMap.get(i.getZone()).setText(range);
            });   
        
    }
}
