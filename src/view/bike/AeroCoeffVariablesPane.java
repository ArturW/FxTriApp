/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.bike;

import java.util.LinkedList;
import java.util.List;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import static main.StyleSheetConstants.AEROPANE;
import static main.StyleSheetConstants.AEROVALUES;
import static main.StyleSheetConstants.AEROVARIABLES;

/**
 *
 * @author user
 */
public class AeroCoeffVariablesPane extends TextFlow {
    private final String airDensity = "Air density typical values at different altitudes.\n";
    private final String[] airDensitySeaLevel = new String[] {"- Sea Level", "1.142"};
    private final String[] airDensity500m = new String[] {" - 500m", "1.088\n"};
    private final String[] airDensity1000m = new String[] {" - 1000m", "1.036\n"};
    private final String[] airDensity1500m = new String[] {" - 1500m", "0.986\n"};
    private final String[] airDensity2000m = new String[] {" - 2000m", "0.938\n"};
    
    private final String dragCoeff = "Typical drag coefficients.\n";    
    private final String[] dragAeroBars = new String[] {" - Aero Bars", "0.70\n"};
    private final String[] dragDrops = new String[] {" - Drops", "0.88\n"};
    private final String[] dragHoods = new String[] {" - Hoods", "1.0\n"};
    private final String[] dragTops = new String[] {" - Tops", "1.15\n"};
                
    private final String frontalArea = "Frontal areas of an average cyclist in different positions.\n";
    private final String[] frontalAreaTriathlon = new String[] {" - Triathlon", "0.25\n"};
    private final String[] frontalAreaDrops = new String[] {" - Drops", "0.32\n"};
    private final String[] frontalAreaHoods = new String[] {" - Hoods", "0.40\n"};
    private final String[] frontalAreaTops = new String[] {" - Tops", "0.6\n"};
      
    private final String rollR = "Rolling resistance of different surfaces.\n";
    private final String[] rollRWoodenTrack = new String[] {" - Wooden Track", "0.001\n"};
    private final String[] rollRSmoothConcrete = new String[] {" - Smooth Concrete", "0.002\n"};
    private final String[] rollRAsphaltRoad = new String[] {" - Asphalt Road", "0.004\n"};
    private final String[] rollRRoughRoad = new String[] {" - Rough Road", "0.008\n"};
        
    private static final String ch = ": ";
        
    AeroCoeffVariablesPane() {
        
        this.getStyleClass().add(AEROPANE);
        
        Text airDensityText = new Text(airDensity);
        airDensityText.getStyleClass().add(AEROVARIABLES);
        this.getChildren().add(airDensityText);
        List<String[]> airDensityList = new LinkedList<>();
        airDensityList.add(airDensitySeaLevel);
        airDensityList.add(airDensity500m);
        airDensityList.add(airDensity1000m);
        airDensityList.add(airDensity1500m);
        airDensityList.add(airDensity2000m);
        airDensityList.stream()
                .forEach(str -> {
                    Text name = new Text(str[0] + ch);
                    name.getStyleClass().add(AEROVALUES);
                    Text value = new Text(str[1]);
                    value.getStyleClass().add(AEROVALUES);
                    this.getChildren().addAll(name,  value);
                });
        
        Text dragCoeffText = new Text(dragCoeff);
        dragCoeffText.getStyleClass().add(AEROVARIABLES);
        this.getChildren().add(dragCoeffText);
        List<String[]> dragCoeffList = new LinkedList<>();
        dragCoeffList.add(dragAeroBars);
        dragCoeffList.add(dragDrops);
        dragCoeffList.add(dragHoods);
        dragCoeffList.add(dragTops);
        dragCoeffList.stream()
                .forEach(str -> {
                    Text name = new Text(str[0] + ch);
                    name.getStyleClass().add(AEROVALUES);
                    Text value = new Text(str[1]);
                    value.getStyleClass().add(AEROVALUES);
                    this.getChildren().addAll(name, value);
                });
        
        Text frontalAreaText = new Text(frontalArea);
        frontalAreaText.getStyleClass().add(AEROVARIABLES);
        this.getChildren().add(frontalAreaText);
        List<String[]> frontalAreaList = new LinkedList<>();
        frontalAreaList.add(frontalAreaTriathlon);
        frontalAreaList.add(frontalAreaDrops);
        frontalAreaList.add(frontalAreaHoods);
        frontalAreaList.add(frontalAreaTops);
        frontalAreaList.stream()
                .forEach(str -> {
                    Text name = new Text(str[0] + ch);
                    name.getStyleClass().add(AEROVALUES);
                    Text value = new Text(str[1]);
                    value.getStyleClass().add(AEROVALUES);
                    this.getChildren().addAll(name, value);
                });        
        
        Text rollRText = new Text(rollR);
        rollRText.getStyleClass().add(AEROVARIABLES);
        this.getChildren().add(rollRText);
        List<String[]> rollRList = new LinkedList<>();
        rollRList.add(rollRWoodenTrack);
        rollRList.add(rollRSmoothConcrete);
        rollRList.add(rollRAsphaltRoad);
        rollRList.add(rollRRoughRoad);
        rollRList.stream()
                .forEach(str -> {
                    Text name = new Text(str[0] + ch);
                    name.getStyleClass().add(AEROVALUES);
                    Text value = new Text(str[1]);
                    value.getStyleClass().add(AEROVALUES);
                    this.getChildren().addAll(name, value);
                });                        
    }    
}
