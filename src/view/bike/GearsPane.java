/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.bike;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Accordion;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import static main.StyleSheetConstants.BOXLAYOUTS;
import static main.StyleSheetConstants.CADANCESLIDER;
import static main.StyleSheetConstants.CELLHEADER;
import static main.StyleSheetConstants.GEARSCELLHEADER;
import static main.StyleSheetConstants.GEARSCELLVIEW;
import static main.StyleSheetConstants.GEARSLISTHEADER;
import static main.StyleSheetConstants.GEARSLISTVIEW;
import static main.StyleSheetConstants.LABELS;
import static main.StyleSheetConstants.ZONESLIDER;
import tritools.models.mechanical.CassetteSizes;
import tritools.models.mechanical.CranksetSizes;
import tritools.models.mechanical.Gears;
import tritools.models.mechanical.GearsModel;
import tritools.models.mechanical.Sprocket;
import tritools.models.mechanical.WheelSizes;
import tritools.util.Util;

/**
 *
 * @author user
 */
public class GearsPane extends VBox {   
    
    private static final int MAX_SIZE = 3; // maximum number of cranks
    //private final Menu cranksetMenu;
    private final ComboBox<String> cranksetComboBox;
    private final ComboBox<String> cassetteComboBox;
    private final ChoiceBox<String> wheelChoiceBox;
    private final Slider cadanceSlider;
        
    private final ObservableList<String> observableCassette = FXCollections.observableArrayList();
    private final List<ObservableList<String>> speedList = new ArrayList<>();
                
    private final String defaultCrankset = "50-34";
    private final String defaultCassette = "10/11-23";
    private final String defaultWheelSize = "700C";
    private final int minCadance = 40;
    private final int maxCadance = 120;
    private final int defaultCadance = 100;
            
    public GearsPane() {
                
        final Label cranksetLabel = new Label("Crankset");
        cranksetLabel.getStyleClass().add(LABELS);                
        cranksetComboBox = new ComboBox<>(
            FXCollections.observableArrayList(CranksetSizes.getCranksetNames()));
        cranksetComboBox.setValue(defaultCrankset);
        
        final Label cassetteLabel = new Label("Cassette");
        cassetteLabel.getStyleClass().add(LABELS);
        cassetteComboBox = new ComboBox<>(
            FXCollections.observableArrayList(CassetteSizes.getCassetteNames()));
        cassetteComboBox.setValue(defaultCassette);
        
        final Label wheelLabel = new Label("Wheel Size");
        wheelLabel.getStyleClass().add(LABELS);
        wheelChoiceBox = new ChoiceBox<>(
            FXCollections.observableArrayList(WheelSizes.getWheelNames()));
        wheelChoiceBox.setValue(defaultWheelSize);
        wheelChoiceBox.setDisable(true); // Disablle wheelSize
        
        cadanceSlider = new Slider();        
        cadanceSlider.getStyleClass().add(ZONESLIDER);
        cadanceSlider.getStyleClass().add(CADANCESLIDER);        
        cadanceSlider.setMin(minCadance);
        cadanceSlider.setMax(maxCadance);
        cadanceSlider.setValue(defaultCadance);
        //final StringProperty cadanceProperty = new SimpleStringProperty();
        //cadanceProperty.bindBidirectional(cadanceSlider.valueProperty(), new NumberStringConverter());
        final Label cadanceLabel = new Label("Cadance: " + (int)cadanceSlider.getValue() + " rpm");
        cadanceLabel.getStyleClass().add(LABELS);
        
        final VBox cadanceBox = new VBox(cadanceLabel, cadanceSlider);
        cadanceBox.getStyleClass().add(BOXLAYOUTS);        
        final HBox sprocketsBox = new HBox(cranksetLabel, cranksetComboBox, cassetteLabel, cassetteComboBox);
        sprocketsBox.getStyleClass().add(BOXLAYOUTS);
        final HBox wheelSizeBox = new HBox(wheelLabel, wheelChoiceBox);
        wheelSizeBox.getStyleClass().add(BOXLAYOUTS);
        final VBox selectionBox = new VBox(sprocketsBox, wheelSizeBox);
        selectionBox.getStyleClass().add(BOXLAYOUTS);
        
        final HBox controlBox = new HBox(selectionBox, cadanceBox);
        controlBox.getStyleClass().add(BOXLAYOUTS);                
        this.getChildren().addAll(controlBox);
               
        final Label listTitle = new Label("Cadance vs Speed");
        listTitle.getStyleClass().add(LABELS);
        this.getChildren().add(listTitle);
                
        final GearsModel model = createModel();
        
        observableCassette.addAll(Util.parse(model.getGears().getCassette().getSprocket()));
        observableCassette.add(0, "");          
        final ListView<String> cassetteView = new ListView<>(observableCassette);        
        cassetteView.getStyleClass().add(GEARSLISTHEADER);
        final VBox outputBox = new VBox(cassetteView); 
        
        /* Load CSS for each cell */
        cassetteView.setCellFactory(value -> {
            return new ListCell<String>() {
                @Override
                public void updateItem(String s, boolean b) {
                    super.updateItem(s, b);
                    this.getStyleClass().add(GEARSCELLHEADER);
                    setText(s);
                }
            };            
        });                                
        
        /* Add values to ViewList for first time */
        IntStream.range(0, MAX_SIZE) 
            .forEach(i -> {
                if (i < model.getGears().getCrankset().getSize()) {
                    List<String> fmtSpeedList = Util.format(model.getSpeed().get(i));
                    fmtSpeedList.add(0, String.valueOf(model.getGears().getCrankset().getSprocket().get(i)));
                    ObservableList<String> list = FXCollections.observableArrayList(fmtSpeedList);
                    speedList.add(list);                                           
                } else {
                    ObservableList<String> list = FXCollections.observableArrayList(Arrays.asList(new String[] {""}));
                    speedList.add(list);
                }
                ListView<String> speedView = new ListView<>(speedList.get(i));
                speedView.getStyleClass().add(GEARSLISTVIEW);
                /* Load CSS for each cell */
                speedView.setCellFactory(value -> {
                    return new ListCell<String>() {
                        @Override                        
                        public void updateItem(String s, boolean b) {
                            super.updateItem(s, b);
                            this.getStyleClass().add(GEARSCELLVIEW);
                            if (s != null) {
                                if (!s.contains(".")) {
                                    this.getStyleClass().add(CELLHEADER);
                                }
                            }
                            setText(s);
                        }
                    };                
                });
                outputBox.getChildren().add(speedView);                                              
            });                        
        
        final TitledPane listPane = new TitledPane("Gears View", outputBox);
        
        final LineChart<Number, Number> chart = createChart();
        updateChart(chart);
        final TitledPane chartPane = new TitledPane("Chart", chart);
                        
        Accordion accordion = new Accordion();
        accordion.setExpandedPane(listPane);        
        accordion.getPanes().addAll(listPane, chartPane);
        
        this.getChildren().add(accordion);
                
        cranksetComboBox.valueProperty().addListener((listener, oldValue, newValue) -> {
            updateObservable();
            updateChart(chart);
        });
        
        cassetteComboBox.valueProperty().addListener((listener, oldValue, newValue) -> {               
            updateObservable();
            updateChart(chart);
        });
        
        cadanceSlider.valueProperty().addListener((listener, oldValue, newValue) -> {
            cadanceLabel.setText("Cadance: " + (int)cadanceSlider.getValue() + " rpm");            
            updateObservable();
        });
    }
    
    private GearsModel createModel() {
        final List<Integer> cranksetList = CranksetSizes.getCrankset(cranksetComboBox.getValue());
        final Sprocket crankset = Sprocket.newInstance(cranksetList);
        final List<Integer> cassetteList = CassetteSizes.getCassette(cassetteComboBox.getValue());
        final Sprocket cassette = Sprocket.newInstance(cassetteList);

        final Gears gears = Gears.newInstance(crankset, cassette);
        final int wheel = WheelSizes.getWheel(wheelChoiceBox.getValue());
        final int cadance = (int)cadanceSlider.getValue();

        final GearsModel model = new GearsModel.Builder()                
                .sprockets(crankset, cassette)
                .wheelSize(wheel)
                .rpm(cadance)
                .build();            
        return model;
    }    
    
     private GearsModel createModel(int cadance) {
        final List<Integer> cranksetList = CranksetSizes.getCrankset(cranksetComboBox.getValue());
        final Sprocket crankset = Sprocket.newInstance(cranksetList);
        final List<Integer> cassetteList = CassetteSizes.getCassette(cassetteComboBox.getValue());
        final Sprocket cassette = Sprocket.newInstance(cassetteList);

        final Gears gears = Gears.newInstance(crankset, cassette);
        final int wheel = WheelSizes.getWheel(wheelChoiceBox.getValue());
        //final int cadance = (int)cadanceSlider.getValue();

        final GearsModel model = new GearsModel.Builder()                
                .sprockets(crankset, cassette)
                .wheelSize(wheel)
                .rpm(cadance)
                .build();            
        return model;
    }    

    private LineChart<Number, Number> createChart() {
        
        final int tick = 10;
        //final NumberAxis speedAxis = new NumberAxis("Speed(km/h)", min, max, tick);
        final NumberAxis speedAxis = new NumberAxis();
        speedAxis.setLabel("Speed(km/h)");
        speedAxis.setTickUnit(tick);
        //final NumberAxis rpmAxis = new NumberAxis("rpm", minCadance, maxCadance, tick);
        final NumberAxis rpmAxis = new NumberAxis("Cadance", 0, maxCadance, tick);        
        
        final LineChart<Number, Number> chart = new LineChart<>(rpmAxis, speedAxis);              
        chart.setLegendVisible(true);                        
        return chart;
    }
    
    private void updateChart(final LineChart<Number, Number> chart) {
                
        final List<Integer> cranksetList = CranksetSizes.getCrankset(cranksetComboBox.getValue());
        System.out.println(cranksetList);
        final List<Integer> cassetteList = CassetteSizes.getCassette(cassetteComboBox.getValue());
        System.out.println(cassetteList);
        
        chart.getData().clear();
        IntStream.range(0, cranksetList.size())
                .forEach(i -> {
                    IntStream.range(0, cassetteList.size())
                        .forEach(j -> {
                            if (i < cranksetList.size()) {

                                List<List<List<Double>>> list = 
                                IntStream.rangeClosed(0, maxCadance)
                                    .mapToObj(n -> createModel(n))
                                    .map(n -> n.getSpeed())
                                    .collect(Collectors.toList());
                              
                                List<Double> speedList = list.stream()                                        
                                        .map(n -> n.get(i).get(j))
                                        //.map(n -> Math.log(n)/Math.log(2))
                                        .collect(Collectors.toList());                                
                                
                                List<XYChart.Data<Number, Number>> listData = IntStream.of(0, speedList.size() - 1)
                                        .mapToObj(n -> new XYChart.Data<Number, Number>(n, speedList.get(n)))                                        
                                        .collect(Collectors.toList());                               
                                
                                listData.stream()
                                        .forEach(n -> {
                                            Tooltip.install(n.getNode(), new Tooltip(n.getYValue().toString()));                                            
                                            //n.getNode().setOnMouseEntered(value -> System.out.println(value));
                                        });
                                
                                final ObservableList obsList = FXCollections.observableArrayList(listData);                                
                                final XYChart.Series<Number, Number> speedSeries =
                                        new XYChart.Series<>(obsList);                                
                                speedSeries.setName("" + cranksetList.get(i) + "/" + cassetteList.get(j));                                
                                chart.getData().add(speedSeries);                                                                  
                            } else {

                            }
                        });
                });        
    }
    
    
    private void updateObservable() {
        final GearsModel model = createModel();
        observableCassette.setAll(Util.parse(model.getGears().getCassette().getSprocket()));
        observableCassette.add(0, "");
                
        /* Clear List add replace with new values */
        speedList.stream().forEach((ObservableList<String> list) -> list.clear());        
        IntStream.range(0, MAX_SIZE)
            .forEach(i -> {                
                if (i < model.getGears().getCrankset().getSize()) {
                    List<String> fmtSpeedList = Util.format(model.getSpeed().get(i));
                    fmtSpeedList.add(0, String.valueOf(model.getGears().getCrankset().getSprocket().get(i)));                    
                    ObservableList<String> list = FXCollections.observableArrayList(fmtSpeedList);
                    speedList.get(i).setAll(list);
                } else {
                    ObservableList<String> list = FXCollections.observableArrayList(Arrays.asList(new String[] {""}));
                    speedList.get(i).setAll(list);
                }         
            });
    }    
}
