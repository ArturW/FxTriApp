/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.zones;

import java.util.stream.Stream;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import static main.StyleSheetConstants.POWERSLIDER;
import tritools.models.zones.BikePowerModel;
import tritools.models.zones.Model;
import tritools.models.ModelType;
import tritools.models.ModelUtil;
import tritools.zones.PowerZones;
import tritools.zones.ZoneNames;
import static view.zones.ZonesPane.properties;

/**
 *
 * @author user
 */
public class BikePowerPane extends ZonesPane {

    private static final String LABEL = "Power Threshold: ";
    private final String defaultValue = "powerThreshold";
   
    private final IntegerProperty thresholdProperty;
    
    public BikePowerPane() {
        super(LABEL);
                
        final int value = Integer.valueOf((String)properties.get(defaultValue));
        this.thresholdNameLabel.setText(LABEL + value + " watts");
        
        thresholdProperty = new SimpleIntegerProperty(this, defaultValue, value);
        thresholdProperty.bindBidirectional(slider.valueProperty());
        
        newModel(value);
        
        slider.getStyleClass().add(POWERSLIDER);
        slider.setMin(100);
        slider.setMax(600);
        slider.setValue(value);
        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
                    //LOG.info("Slider: " + observable + ": " + oldValue + "->" + newValue);
                    thresholdNameLabel.setText(LABEL + thresholdProperty.get() + " watts");
                    newModel(thresholdProperty.get());
                }); 
    }
    
    private void newModel(final int value) {
        //BikePowerModel model = ModelUtil.newBikePowerModel(value);
        //Model model = ModelUtil.getModel(ModelType.POWERMODEL, value);
        BikePowerModel model = BikePowerModel.of(value);
        
        Stream.of(ZoneNames.values())
            .forEach(i -> {                                                                                
                PowerZones zone = PowerZones.valueOf(i.name());
                //TitledPane titledPane = zonesMap.get(zone.toString());

                String range = "";                
                
                switch(zone) {
                    case ZONE1:
                        range = " < " + String.valueOf(zone.getZone()) + ": "
                            + String.format("%.0f watts", model.getZones().get(i.name()));
                        break;
                    case ZONE5B:
                        range = " > " + String.valueOf(zone.getZone()) + ": "
                            + String.format("%.0f watts", model.getZones().get(i.name()));
                        break;
                    case ZONE2:
                    case ZONE3:
                    case ZONE4:
                    case ZONE5A:                    
                        PowerZones upper = Stream.of(PowerZones.values())
                            .filter(p -> p.equals(zone))
                            .findFirst()
                            .get();
                        PowerZones lower = Stream.of(PowerZones.values())
                            .filter(p -> upper.ordinal() == (p.ordinal() + 1))
                            .findFirst()
                            .get();

                        range = String.valueOf(lower.getZone())
                            + " - " + String.valueOf(upper.getZone()) + ": "
                            + String.format("%.0f - %.0f watts",
                                    model.getZones().get(lower.name()),
                                    model.getZones().get(upper.name()));
                    }
                    rangeMap.get(i.getZone()).setText(range);                                     
            });   
    }
}
