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
import tritools.models.zones.BikeThresholdModel;
import tritools.models.zones.Model;
import tritools.models.ModelType;
import tritools.models.ModelUtil;
import tritools.zones.BikeZones;
import tritools.zones.ZoneNames;
import static view.zones.ZonesPane.properties;

/**
 *
 * @author user
 */
public class BikeThresholdPane extends ZonesPane {
    
    private static final String LABEL = "Bike Threshold: ";
    private final String defaultValue = "bikeThreshold";
    
    private final IntegerProperty thresholdProperty;
    
    public BikeThresholdPane() {
        super(LABEL);
    
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
                    newModel(thresholdProperty.get());
                });
        
    }
    
    private void newModel(final int value) {
        //BikeThresholdModel model = ModelUtil.newBikeThresholdModel(value);
        //Model model = ModelUtil.getModel(ModelType.BIKEMODEL, value);
        BikeThresholdModel model = BikeThresholdModel.of(value);
        
        Stream.of(ZoneNames.values())
            .forEach(i -> {                                                                                
                BikeZones zone = BikeZones.valueOf(i.name());
                //TitledPane titledPane = zonesMap.get(zone.toString());

                String range = "";                
                
                switch(zone) {
                    case ZONE1:
                        range = " < " + String.valueOf(zone.getZone()) + ": "
                            + String.format("%.0f bpm", model.getZones().get(i.name()));
                        break;
                    case ZONE5B:
                        range = " > " + String.valueOf(zone.getZone()) + ": "
                            + String.format("%.0f bpm", model.getZones().get(i.name()));
                        break;
                    case ZONE2:
                    case ZONE3:
                    case ZONE4:
                    case ZONE5A:                    
                        BikeZones upper = Stream.of(BikeZones.values())
                            .filter(p -> p.equals(zone))
                            .findFirst()
                            .get();
                        BikeZones lower = Stream.of(BikeZones.values())
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
