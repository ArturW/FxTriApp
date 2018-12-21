/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import tritools.models.ModelUtil;
import tritools.util.ProgressionModel;
import view.bike.AerodynamicsPane;
import view.zones.BikePowerPane;
import view.zones.BikeThresholdPane;
import view.run.RunEstimatesPane;
import view.info.ProgressionPane;
import view.swim.CSSPane;
import view.bike.GearsPane;
import view.swim.SwimEstimatesPane;
import view.info.TimeSavingsPane;
import view.zones.RunPacesPane;
import view.zones.RunThresholdPane;
import view.info.RPETextPane;

/*
* https://sites.google.com/site/bomanton/endurance
*/
/**
 *
 * @author user
 */
public class GUI extends Application {    

    private static final String style = new String("-fx-background-color: #B8CFE5");
    private static final String STYLESHEET_PATH = "resources/styleSheet.css";
    
    @Override
    public void start(Stage primaryStage) {
    
        final boolean closable = false;
                       
        final Tab titleTab = new Tab("", new TitlePane());
        titleTab.setStyle(style);
        titleTab.setClosable(closable);
        
        /********** Run Tab **********/
        final Tab runZonesTab = new Tab("Run Zones", new RunThresholdPane());
        runZonesTab.setClosable(closable);
        final Tab runPacesTab = new Tab("Run Paces", new RunPacesPane());
        runPacesTab.setClosable(closable);
        final Tab runEstimatesTab = new Tab("Estimates", new RunEstimatesPane(ModelUtil.newEstimatesModel()));
        runEstimatesTab.setClosable(closable);
        final Tab runVDotTab = new Tab("VDot");
        runVDotTab.setClosable(closable);
        runVDotTab.setDisable(true);
        final TabPane runTabs = new TabPane();
        runTabs.getTabs().addAll(runZonesTab, runPacesTab, runEstimatesTab, runVDotTab);
        final Tab runToolsTab = new Tab("Run Tools", runTabs);        
        runToolsTab.setClosable(closable);

        /********** Bike Tab **********/
        final Tab bikeZonesTab = new Tab("Bike Zones", new BikeThresholdPane());
        bikeZonesTab.setClosable(closable);
        final Tab bikePowerTab = new Tab("Power Zones", new BikePowerPane());
        bikePowerTab.setClosable(closable);
        final Tab bikeAerodynamicsTab = new Tab("Aerodynamics", new AerodynamicsPane());
        bikeAerodynamicsTab.setClosable(closable);
        final Tab bikeGearRatioTab = new Tab("Gear Ratio", new GearsPane());
        bikeGearRatioTab.setClosable(closable);
        final Tab bikeTimeSavingsTab = new Tab("Time Savings", new TimeSavingsPane());
        bikeTimeSavingsTab.setClosable(closable);
        final TabPane bikeTabs = new TabPane(bikeZonesTab, bikePowerTab,
                bikeAerodynamicsTab, bikeGearRatioTab);
        final Tab bikeToolsTab = new Tab("Bike Tools", bikeTabs);
        bikeToolsTab.setClosable(false);        

        /********** Swim Tab **********/
        final Tab swimCSSTab = new Tab("CSS", new CSSPane(ModelUtil.newCSSMOdel()));
        swimCSSTab.setClosable(closable);
        final Tab swimEstimatesTab = new Tab("Estimates", new SwimEstimatesPane(ModelUtil.newEstimatesModel()));
        swimEstimatesTab.setClosable(closable);
        final TabPane swimTabs = new TabPane();
        swimTabs.getTabs().addAll(swimCSSTab, swimEstimatesTab);
        Tab swimToolsTab = new Tab("Swim Tools", swimTabs);        
        swimToolsTab.setClosable(false);
        
        /********** Utils Tab **********/
        final Tab progressionTab = new Tab("Progression", new ProgressionPane(ProgressionModel.newInstance()));
        progressionTab.setClosable(false);
        
        final ContextMenu progressionContextMenu = new ContextMenu();
        progressionContextMenu.setOnShowing(e -> System.out.println("Showing: " + e));
        progressionContextMenu.setOnShown(e -> System.out.println("Shown: " + e));        
        MenuItem progressionMenuItem1 = new MenuItem();
        progressionMenuItem1.setText("About");
        progressionMenuItem1.setOnAction(e -> System.out.println("About: " + e));
        MenuItem progressionMenuItem2 = new MenuItem();
        progressionMenuItem2.setText("...");
        progressionMenuItem2.setOnAction(e -> System.out.println(": " + e));
        progressionContextMenu.getItems().addAll(progressionMenuItem1, progressionMenuItem2);
        progressionTab.setContextMenu(progressionContextMenu);
        
        final Tab scheduleTab = new Tab("Schedule");
        scheduleTab.setClosable(false);                          
        scheduleTab.setDisable(true);
        
        final Tab rpeTab = new Tab("RPE", new RPETextPane());
        rpeTab.setClosable(false);
        
        final Tooltip progressionTooltip = new Tooltip();
        progressionTooltip.setText("Calculates progression times in number of weeks");
        progressionTab.setTooltip(progressionTooltip);
        progressionTooltip.setOnShowing(e -> System.out.println("Progression Tooltip: " + e));
        progressionTooltip.setOnShown(e -> System.out.println("Progression Tooltip: " + e));     
        
        final TabPane utilTabPane = new TabPane(scheduleTab, rpeTab, progressionTab);
                        
        final Tab utilTab = new Tab("Utils", utilTabPane);        
        utilTab.setClosable(false);
        final Tooltip utilTooltip = new Tooltip();
        utilTab.setTooltip(utilTooltip);        
        utilTooltip.setText("Helpful utilities for workouts");               

        /* Create main tab menu */
        TabPane root = new TabPane();        
        root.setSide(Side.LEFT);        
        root.getTabs().addAll(titleTab,
                                runToolsTab,
                                bikeToolsTab,
                                swimToolsTab,
                                utilTab);
        
        Scene scene = new Scene(root, 800, 500);
        scene.getStylesheets().add(STYLESHEET_PATH);
        
        primaryStage.setResizable(false);
        primaryStage.setTitle("Tri App");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
