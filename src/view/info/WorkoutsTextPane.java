/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.info;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import static main.StyleSheetConstants.WORKOUTSPANE;
import static main.StyleSheetConstants.ZONETITLELABEL;
import static main.StyleSheetConstants.ZONEWORKOUTS;

/**
 *
 * @author user
 */
public class WorkoutsTextPane extends TextFlow{
    private final String title = "Typical Workouts";
    
    private final String zone1Title = "Zone 1: Recovery Mainset\n";
    private final String zone1Workout1 = "- Recovery workout. Train steadily in Zone 1 following high-intenisty day workouts in Zone 4 and 5.\n";
        
    private final String zone2Title = "Zone 2: Aerobic Threshold Mainsets\n";
    private final String zone2Workout1 = "- Exercise for more than 1 hour, looking for improvements over time at the same heart rate.\n";
    
    private final String zone3Title = "Zone 3: Tempo Mainsets\n";
    private final String zone3Workout1 = "- Train steadily from 20 to 90 minutes.\n";
    private final String zone3Workout2 = "- Tempo Intervals. Complete 30 to 90 minutes of work intervals that are 12 to 20 minutes in duration, with recovery intervals that are one-fourth long.\n";
    
    private final String zone4Title = "Zone 4: Sub-Lactate Threshold Mainsets\n";
    private final String zone4Workout1 = "- Cruise intervals. Complete 30 to 60 mintues of intervals that are 6 to 12 minutes long, with recovery intervals that are one-fourth long.\n";
    private final String zone4Workout2 = "- Cruise steady state. Train steadily in Zone 4 for 20-30 minutes.\n";
    
    private final String zone5aTitle = "Zone 5a: Lactate Threshold Mainsets\n";
    private final String zone5aWorkout1 = "- Cruise intervals. Complete 30 to 60 minutes of intervals that are 6 to 12 minutes long, with recovery intervals that are one-fourth long.\n";
    private final String zone5aWorkout2 = "- Cruise steady state. Train steadily in Zone 5a for 20-30 minutes.\n";
    
    private final String zone5bTitle = "Zone 5b: Aerobic Capacity Mainsets\n";
    private final String zone5bWorkout1 = "- Aerobic capacity intervals. Complete 12-20 minutes total work intervals that are 2-4 minutes long. Recovery intervals initially are as long as the work intervals. Shorten the recovery intervals to better simulate the event.\n";
    private final String zone5bWorkout2 = "- 30-30s. Alternate 30 seconds at aerobic capacity effort, pace, or power with 30 seconds at recovery effort, pace, or power. Build up to 24 such intervals over 3-4 weeks.\n";
        
    private final String zone5cTitle = "Zone5c\n";
    
    public WorkoutsTextPane() {
        this.getStyleClass().add(WORKOUTSPANE);
        
        final List<String> zonesList = Arrays.asList(zone1Title, zone2Title, zone3Title, zone4Title, zone5aTitle, zone5bTitle);        
        final List<String> zone1Workouts = Arrays.asList(zone1Workout1);
        final List<String> zone2Workouts = Arrays.asList(zone2Workout1);
        final List<String> zone3Workouts = Arrays.asList(zone3Workout1, zone3Workout2);
        final List<String> zone4Workouts = Arrays.asList(zone4Workout1, zone4Workout2);
        final List<String> zone5aWorkouts = Arrays.asList(zone5aWorkout1, zone5aWorkout2);
        final List<String> zone5bWorkouts = Arrays.asList(zone5bWorkout1, zone5bWorkout2);
        
        final List<List<String>> workoutsList = Arrays.asList(zone1Workouts, zone2Workouts, zone3Workouts, zone4Workouts, zone5aWorkouts, zone5bWorkouts);
                
        /* Map all workloouts to map(zone, workout) */
        final Map<String, List<String>> workoutsMap = IntStream.rangeClosed(0, 5)
            .mapToObj(i -> i)
            .collect(Collectors.toMap(i -> zonesList.get((int)i),
                                      i ->  workoutsList.get((int)i),
                                        (v1,v2) -> v1,
                                        () -> new LinkedHashMap<>()));
            
        //System.out.println(workoutsMap);
        
        workoutsMap.keySet().stream()              
                .forEach(key -> {
                    Text zoneText = new Text(key);
                    zoneText.getStyleClass().add(ZONETITLELABEL);
                    
                    this.getChildren().add(zoneText);                                        
                    List<String> workoutList = workoutsMap.get(key);
                    workoutList.stream()
                            .forEach(workout -> {
                                
                                Text workoutText = new Text(workout);
                                workoutText.getStyleClass().add(ZONEWORKOUTS);
                                this.getChildren().add(workoutText);
                                
                            });             
                });        
    }
                
}
