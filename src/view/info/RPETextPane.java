/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.info;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import static main.StyleSheetConstants.RPEGRIDPANE;
import static main.StyleSheetConstants.RPEHEADER;
import static main.StyleSheetConstants.RPETEXT;

/**
 *
 * @author user
 */
public class RPETextPane extends TextFlow {
    
    private final String mainTitle = "Heart Rate and RPE Zones\n";
    
    private final String hrZone = "HR Zones\n";
    private final String rpeZone = "RPE Zone\n";
    private final String title = "Title\n";
    private final String purpose = "Purpose\n";
    private final String duration = "Typical Duration\n";
    
    private final String hrZone1 = "1\n";
    private final String hrZone2 = "2\n";
    private final String hrZone3 = "3\n";
    private final String hrZone4 = "4\n";
    private final String hrZone5a = "5a\n";
    private final String hrZone5b = "5b\n";
    private final String hrZone5c = "5c\n";
                
    private final String rpeZone1 = "1-2\n";
    private final String rpeZone2 = "3-4\n";
    private final String rpeZone3 = "5\n";
    private final String rpeZone4 = "6-7\n";
    private final String rpeZone5a = "8\n";
    private final String rpeZone5b = "9\n";
    private final String rpeZone5c = "10\n";
    
    private final String title1 = "Active Recovery\n";
    private final String title2 = "Aerobic Threshold\n";
    private final String title3 = "Tempo\n";
    private final String title4 = "Sub-Lactate Threshold\n";
    private final String title5a = "Lactate Threshold\n";
    private final String title5b = "Aerobic Capacity\n";
    private final String title5c = "Anaerobic Capacity\n";
    
    private final String purpose1 = "Active recovery from previous hard training\n";
    private final String purpose2 = "Build aerobic endurance\n";
    private final String purpose3 = "Challenge aerobic system\n";
    private final String purpose4 = "Improve acid tolerance for long endurance\n";
    private final String purpose5a = "Build LT performance\n";
    private final String purpose5b = "Maximally challenge aerobic system\n";
    private final String purpose5c = "Maximally challenge anaerobic system\n";
    
    private final String duration1 = "Limited by sleep\n";
    private final String duration2 = "< 12 hours\n";
    private final String duration3 = "< 8 hours\n";
    private final String duration4 = "< 3 hours\n";
    private final String duration5a = "< 1 hour\n"; 
    private final String duration5b = "< 20 minutes\n";
    private final String duration5c = "< 2 minutes\n";
    
    public RPETextPane() {
        
        final List<String> headers = Arrays.asList(new String[] {hrZone, rpeZone, title, purpose, duration});        
        
        final List<String> zone1 = Arrays.asList(new String[] {hrZone1, rpeZone1, title1, purpose1, duration1});
        final List<String> zone2 = Arrays.asList(new String[] {hrZone2, rpeZone2, title2, purpose2, duration2});
        final List<String> zone3 = Arrays.asList(new String[] {hrZone3, rpeZone3, title3, purpose3, duration3});
        final List<String> zone4 = Arrays.asList(new String[] {hrZone4, rpeZone4, title4, purpose4, duration4});
        final List<String> zone5a = Arrays.asList(new String[] {hrZone5a, rpeZone5a, title5a, purpose5a, duration5a});
        final List<String> zone5b = Arrays.asList(new String[] {hrZone5b, rpeZone5b, title5b, purpose5b, duration5b});
        final List<String> zone5c = Arrays.asList(new String[] {hrZone5c, rpeZone5c, title5c, purpose5c, duration5c});
        
        GridPane gridPane = new GridPane();
        gridPane.getStyleClass().add(RPEGRIDPANE);
               
        //gridPane.setHgap(10); //horizontal gap in pixels => that's what you are asking for
        //gridPane.setVgap(10); //vertical gap in pixels
        //gridPane.setPadding(new Insets(10, 10, 10, 10)); 
        
        Text mainHeader = new Text(mainTitle);
        mainHeader.getStyleClass().add(RPEHEADER);
        
 
        IntStream.range(0, headers.size())
            .forEach(i -> {
                Text header = new Text(headers.get(i));
                header.getStyleClass().add(RPEHEADER);
                gridPane.add(header, i, 0);                                
                
                Text zone1Text = new Text(zone1.get(i));
                zone1Text.getStyleClass().add(RPETEXT);
                gridPane.add(zone1Text, i, 1);
                
                Text zone2Text = new Text(zone2.get(i));
                zone2Text.getStyleClass().add(RPETEXT);
                gridPane.add(zone2Text, i, 2);
                
                Text zone3Text = new Text(zone3.get(i));
                zone3Text.getStyleClass().add(RPETEXT);
                gridPane.add(zone3Text, i, 3);
                
                Text zone4Text = new Text(zone4.get(i));
                zone4Text.getStyleClass().add(RPETEXT);
                gridPane.add(zone4Text, i, 4);
                
                Text zone5aText = new Text(zone5a.get(i));
                zone5aText.getStyleClass().add(RPETEXT);
                gridPane.add(zone5aText, i, 5);
                
                Text zone5bText = new Text(zone5b.get(i));
                zone5bText.getStyleClass().add(RPETEXT);
                gridPane.add(zone5bText, i, 6);
                
                Text zone5cText = new Text(zone5c.get(i));
                zone5cText.getStyleClass().add(RPETEXT);
                gridPane.add(zone5cText, i, 7);                                
            });
                 
        gridPane.getChildren().addAll();                
        this.getChildren().addAll(mainHeader, gridPane);
    }
}
