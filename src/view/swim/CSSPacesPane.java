/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.swim;

import java.util.Arrays;
import java.util.List;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import static main.StyleSheetConstants.ZONETITLELABEL;
import static main.StyleSheetConstants.ZONEWORKOUTS;

/**
 *
 * @author user
 */
public class CSSPacesPane extends TextFlow {
    String paceTitle = "Estimated paces based on CSS\n";
    String pace1 = " - 400m : CSS pace -2 to -4 sec per 100m\n";
    String pace2 = " - 1500m : CSS pace\n";
    String pace3 = " - 5km : CSS pace + 2 to 4 sec per 100m\n";
    String pace4 = " - 10km : CSS pace + 6 to 10 sec per 100m\n";
        
    String workoutTitle = "Typical Workouts\n";
    //String workout = "You swim 5:45-7:30 for 400m:\n";
    String workout1 = " - 8x200m with 20sec recovery\n";
    String workout2 = " - 4x400m with 40sec recovery\n";
    String workout3 = " - 15x100m with 10sec recovery\n";


    CSSPacesPane() {
        this.getStyleClass().add(ZONETITLELABEL);        
        
        Text titleText = new Text(paceTitle);
        titleText.getStyleClass().add(ZONETITLELABEL);
        this.getChildren().add(titleText);
        
        final List<String> paces = Arrays.asList(pace1, pace2, pace3, pace4);        
        paces.stream()
            .forEach(line -> {
                Text text = new Text(line);
                text.getStyleClass().add(ZONEWORKOUTS);
                this.getChildren().add(text);
            });
        
        Text workoutText = new Text(workoutTitle);
        workoutText.getStyleClass().add(ZONETITLELABEL);
        this.getChildren().add(workoutText);
        
        final List<String> workouts = Arrays.asList(workout1, workout2, workout3);        
        workouts.stream()
            .forEach(line -> {
                Text text = new Text(line);
                text.getStyleClass().add(ZONEWORKOUTS);
                this.getChildren().add(text);
            });
        
    }
}
