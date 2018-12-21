/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.bike;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedAreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;
import static main.StyleSheetConstants.AEROCHART;
import static main.StyleSheetConstants.LABELS;
import static main.StyleSheetConstants.POWERSLIDER;
import static main.StyleSheetConstants.VELOCITYSLIDER;
import static main.StyleSheetConstants.ZONESLIDER;
import tritools.models.aero.AeroModel;
import tritools.models.aero.GravityModel;
import tritools.models.aero.RollingResistanceModel;
import tritools.models.aero.WindResistanceModel;
import tritools.models.aero.Model;

/**
 *
 * @author user
 */
//http://www.gribble.org/cycling/power_v_speed.html
//http://www.triradar.com/training-advice/how-to-calculate-your-drag/
//http://www.cyclingpowerlab.com/CyclingAerodynamics.aspx
public class AerodynamicsPane extends HBox {       
    private final DoubleProperty velocityProperty = new SimpleDoubleProperty();
    private final DoubleProperty massProperty = new SimpleDoubleProperty();
    private final DoubleProperty gradientProperty = new SimpleDoubleProperty();
    private final DoubleProperty airDensityProperty = new SimpleDoubleProperty();
    private final DoubleProperty dragProperty = new SimpleDoubleProperty();
    private final DoubleProperty frontalAreaProperty = new SimpleDoubleProperty();
    private final DoubleProperty windProperty = new SimpleDoubleProperty();
    private final DoubleProperty rollingProperty = new SimpleDoubleProperty();
    private final DoubleProperty powerProperty = new SimpleDoubleProperty();        
            
            
    //private StackedAreaChart chart = null;
    private final ObservableList obsRolling = FXCollections.observableArrayList();
    private final ObservableList obsWind = FXCollections.observableArrayList();
    private final ObservableList obsGravity = FXCollections.observableArrayList();
    
    public AerodynamicsPane() {
        final List<String> list = Arrays.asList(
            new String[]{"Mass", "Gradient", "Air Density", "Drag Coefficient", "Frontal Area", "Wind", "Rolling Resistance"});
    
        Label titleLabel = new Label("Aerodynamics");
        titleLabel.getStyleClass().add(LABELS);
        
        Label velocityLabel = new Label("Speed: ");
        velocityLabel.getStyleClass().add(LABELS);
        Label velocityValue = new Label();
        velocityValue.getStyleClass().add(LABELS);
        Label velocityUnits = new Label(" km/h");
        velocityUnits.getStyleClass().add(LABELS);
        HBox speedBox = new HBox(velocityLabel, velocityValue, velocityUnits);
        
        Slider speedSlider = new Slider();
        speedSlider.getStyleClass().add(ZONESLIDER);
        speedSlider.getStyleClass().add(VELOCITYSLIDER);
        speedSlider.setMin(0);
        speedSlider.setMax(100);
        speedSlider.setValue(30);
                  
        StringConverter<Number> velocityConverter = new NumberStringConverter(){
            @Override
            public String toString(Number t) {
                return String.format("%.1f", t);
            }
        };
        
        velocityProperty.bind(speedSlider.valueProperty());
        Bindings.bindBidirectional(velocityValue.textProperty(), velocityProperty, velocityConverter);       
        speedSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
                    //velocityValue.setText(String.format("%.1f km/h", (speedSlider.getValue())));                    
                    update();
                }); 
                                                        
        HBox variablesBox = new HBox();
        VBox labelsBox = new VBox();
        VBox spinnersBox = new VBox();
        list.stream()
                .forEach(string -> {
                    Label label = new Label(string);
                    label.getStyleClass().add(LABELS);
                    labelsBox.getChildren().add(label);
                    
                    Spinner<Number> spinner;
                    switch (string) {
                        case "Mass":                            
                            spinner = new Spinner<>(0, 300, 85);
                            massProperty.bind(spinner.valueProperty());
                            break;
                        case "Gradient":
                            spinner = new Spinner(0.0, 30.0, 0.1, 0.1);
                            gradientProperty.bind(spinner.valueProperty());
                            break;
                        case "Air Density":
                            spinner = new Spinner(0.0, 2.0, 1.036, 0.001);
                            airDensityProperty.bind(spinner.valueProperty());
                            break;
                        case "Drag Coefficient":
                            spinner = new Spinner(0.0, 2.0, 0.75, 0.01);
                            dragProperty.bind(spinner.valueProperty());
                            break;
                        case "Frontal Area":
                            spinner = new Spinner(0.0, 2.0, 0.40, 0.01);
                            frontalAreaProperty.bind(spinner.valueProperty());
                            break;
                        case "Wind":
                            spinner = new Spinner(-50.0, 50.0, 0, 0.1);
                            windProperty.bind(spinner.valueProperty());
                            break;
                        case "Rolling Resistance":
                            spinner = new Spinner(0.000, 1.000, 0.005, 0.001);
                            rollingProperty.bind(spinner.valueProperty());
                            break;
                        default:
                            spinner = new Spinner();
                            spinner.setEditable(false);
                            
                        }
                    spinner.setEditable(true);
                    spinner.valueProperty().addListener((listener, oldValue, newValue) -> {                        
                        update();                        
                    });                    
                    spinner.setMaxWidth(100);
                    spinner.setMinWidth(100);
                    spinnersBox.getChildren().add(spinner);
                    //HBox variableBox = new HBox(label, spinner);
                                        
                });
        variablesBox.getChildren().addAll(labelsBox, spinnersBox);        
        
        Label powerLabel = new Label("Power: ");
        powerLabel.getStyleClass().add(LABELS);
        Label powerValue = new Label();
        powerValue.getStyleClass().add(LABELS);
        Label powerUnits = new Label(" watts");
        powerUnits.getStyleClass().add(LABELS);
        HBox powerBox = new HBox(powerLabel, powerValue, powerUnits);
        
        Slider powerSlider = new Slider();
        powerSlider.getStyleClass().add(ZONESLIDER);
        powerSlider.getStyleClass().add(POWERSLIDER);
        powerSlider.setMin(0);
        powerSlider.setMax(1000);
        powerSlider.setValue(100);
        powerSlider.setDisable(false);
        
        StringConverter<Number> powerConverter = new NumberStringConverter(){
            @Override
            public String toString(Number t) {
                return String.format("%.1f", t);
            }
        };
        Bindings.bindBidirectional(powerValue.textProperty(), powerProperty, powerConverter);
        powerSlider.valueProperty().bind(powerProperty);
        //powerProperty.bind(powerSlider.valueProperty()); // reverse calculation
        powerSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
                    //powerValue.setText(String.format("%.0f watts", (powerSlider.getValue()))); 
                    if (powerSlider.getValue() > 1000) {
                        powerSlider.setMin(1000);
                        powerSlider.setMax(10000);
                        powerSlider.setMajorTickUnit(1000);
                    } else if (powerSlider.getValue() < 1000) {
                        powerSlider.setMin(0);
                        powerSlider.setMax(1000);
                        powerSlider.setMajorTickUnit(200);
                    }
                });
        
        VBox controlBox = new VBox(titleLabel, speedBox, speedSlider, variablesBox, powerBox, powerSlider);                       
        
        VBox leftBox = new VBox();
        leftBox.setMaxWidth(400);
        leftBox.setMinWidth(400);
        leftBox.getChildren().add(controlBox);
        
        VBox rightBox = new VBox();
        rightBox.getChildren().addAll(new AeroCoeffVariablesPane());
        
        HBox parametersBox = new HBox(leftBox, rightBox);                
        TitledPane parametersPane = new TitledPane("Parameters", parametersBox); 
        
        final Label label = new Label();
        final Line line = new Line();
        final StackedAreaChart<Number, Number> chart = createChart(line, label);        
        
        Pane pane = new Pane();
        pane.getChildren().addAll(chart, line, label);
        TitledPane chartPane = new TitledPane("Chart", pane);
        
        Accordion accordion = new Accordion();
        accordion.setExpandedPane(parametersPane);        
        accordion.getPanes().addAll(parametersPane, chartPane);
                                
        this.getChildren().addAll(accordion);
        update();        
    }
    
    /* Update Power Property value bind to slider*/
    private void update() {
        Model gravityModel =
                new GravityModel.Builder(massProperty.get(), velocityProperty.get())
                .gradient(gradientProperty.get())
                .build();
        
        Model windModel =
                new WindResistanceModel.Builder(velocityProperty.get())
                .airDensity(airDensityProperty.get())
                .drag(dragProperty.get())
                .frontalArea(frontalAreaProperty.get())
                .windVelocity(windProperty.get())
                .build();
        
        Model rollingModel =
                new RollingResistanceModel.Builder(massProperty.get(), velocityProperty.get())
                .rolling(rollingProperty.get())
                .build();        
        
        AeroModel aeroModel = AeroModel.newInstance(gravityModel, rollingModel, windModel);                
        powerProperty.setValue(aeroModel.getPower());
        updateChart(); // update observable values only
    }
    
    /* Create chart and update line location*/
    private StackedAreaChart<Number, Number> createChart(Line line, Label label) {        
        final int min = 0;
        final int max = 60;
        final int tick = 10;
        final NumberAxis speedAxis = new NumberAxis("Speed(km/h)", min, max, tick);
        final NumberAxis powerAxis = new NumberAxis();
        powerAxis.setLabel("Power(watts)");                
        
        final StackedAreaChart<Number, Number> chart =
                new StackedAreaChart<>(speedAxis, powerAxis);
        chart.getStyleClass().add(AEROCHART);        
        chart.setTitle("Power vs Speed");
        
        List<XYChart.Data<Number, Number>> rollingList = IntStream.rangeClosed(min, max)
                .mapToObj(n -> {
                    Double power = getAeroModel(n).getRollingResistanceModel().getPower();
                    return new XYChart.Data<Number, Number>(n, power);
                })
                .collect(Collectors.toList());
        
        obsRolling.setAll(rollingList);
        XYChart.Series<Number, Number> rollingSeries =
                new XYChart.Series<>(obsRolling);
        rollingSeries.setName("Rolling");
        
        List<XYChart.Data<Number, Number>> windList = IntStream.rangeClosed(min, max)
                .mapToObj(n -> {
                    Double power = getAeroModel(n).getWindResistanceModel().getPower();
                    return new XYChart.Data<Number, Number>(n, power);
                })
                .collect(Collectors.toList());
        obsWind.setAll(windList);
        XYChart.Series<Number, Number> windSeries =
                new XYChart.Series<>(obsWind);
        windSeries.setName("Wind");
        
        List<XYChart.Data<Number, Number>> gravityList = IntStream.rangeClosed(min, max)
                .mapToObj(n -> {
                    Double power = getAeroModel(n).getGravityModel().getPower();
                    return new XYChart.Data<Number, Number>(n, power);
                })
                .collect(Collectors.toList());
        obsGravity.setAll(gravityList);
        XYChart.Series<Number, Number> gravitySeries =
                new XYChart.Series<>(obsGravity);        
        gravitySeries.setName("Gravity");
        
        chart.getData().setAll(rollingSeries, gravitySeries, windSeries);       
        
        //http://stackoverflow.com/questions/15615881/how-to-add-a-value-marker-to-javafx-chart
        
        final Node chartBackground = chart.lookup(".chart-plot-background");
        final Bounds chartBounds = chartBackground.localToScene(chartBackground.getBoundsInLocal());                               
        
        //Fix this in future
        Double xShift = 67d;//chartBounds.getMinX();
        Double yShift = 40d; //chartBounds.getMinY();
        System.out.println("Min bounds: " + xShift + ", " + yShift);
        
            
        chartBackground.getParent().getChildrenUnmodifiable().stream()
            .filter((n) -> (n != chartBackground))//&& n != speedAxis && n != powerAxis))
            .forEach((n) -> {
            n.setMouseTransparent(true);
            });
                             
        chartBackground.setOnMouseMoved(value -> {
            Double x = chart.getXAxis().getValueForDisplay(value.getX()).doubleValue();
            Double y = chart.getYAxis().getValueForDisplay(value.getY()).doubleValue();
            System.out.println("Speed: " + x + " Power: " + y);
            //System.out.println(powerAxis.getLowerBound() + ", " + powerAxis.getUpperBound());
            //System.out.println(chart.getYAxis().getDisplayPosition(powerAxis.getLowerBound()) + ", " +
            //        chart.getYAxis().getDisplayPosition(powerAxis.getUpperBound()));                                
                                    
            line.setStartX(xShift + chart.getXAxis().getDisplayPosition(x));
            line.setEndX(xShift + chart.getXAxis().getDisplayPosition(x));            
            line.setStartY(yShift + chart.getYAxis().getDisplayPosition(powerAxis.getLowerBound()));
            line.setEndY(yShift + chart.getYAxis().getDisplayPosition(powerAxis.getUpperBound()));            
            
            AeroModel m = getAeroModel(x);            
            String power = String.format("%.1f Watts @ %.1f km/h", m.getPower(), x);
            label.setText(power);
            label.setLayoutX(80);
            label.setLayoutY(50);            
            String rollingP = String.format("%.0f", m.getRollingResistanceModel().getPower());
            String windP = String.format("%.0f", m.getWindResistanceModel().getPower());
            String gravityP = String.format("%.0f", m.getGravityModel().getPower());            
            rollingSeries.setName("Rolling: " + rollingP);
            windSeries.setName("Wind: " + windP);
            gravitySeries.setName("Gravity: " + gravityP);
        });
        
        return chart;
    }
        
    /* Create model based on velocity argument */
    private AeroModel getAeroModel(final double velocity) {        
        Model gravityModel =
                new GravityModel.Builder(massProperty.get(), velocity)
                .gradient(gradientProperty.get())
                .build();
        
        Model windModel =
                new WindResistanceModel.Builder(velocity)
                .airDensity(airDensityProperty.get())
                .drag(dragProperty.get())
                .frontalArea(frontalAreaProperty.get())
                .windVelocity(windProperty.get())
                .build();
        
        Model rollingModel =
                new RollingResistanceModel.Builder(massProperty.get(), velocity)
                .rolling(rollingProperty.get())
                .build();
        
        return AeroModel.newInstance(gravityModel, rollingModel, windModel);
    }
    
    /* Update observable values */
    private void updateChart() {
        final int min = 0;
        final int max = 60;
        
        List<XYChart.Data<Number, Number>> rollingList = IntStream.rangeClosed(min, max)
                .mapToObj(n -> {
                    Double power = getAeroModel(n).getRollingResistanceModel().getPower();
                    return new XYChart.Data<Number, Number>(n, power);
                })
                .collect(Collectors.toList());
        obsRolling.setAll(rollingList);
        
        List<XYChart.Data<Number, Number>> windList = IntStream.rangeClosed(min, max)
                .mapToObj(n -> {
                    Double power = getAeroModel(n).getWindResistanceModel().getPower();
                    return new XYChart.Data<Number, Number>(n, power);
                })
                .collect(Collectors.toList());
        obsWind.setAll(windList);
        
        List<XYChart.Data<Number, Number>> gravityList = IntStream.rangeClosed(min, max)
                .mapToObj(n -> {
                    Double power = getAeroModel(n).getGravityModel().getPower();
                    return new XYChart.Data<Number, Number>(n, power);
                })
                .collect(Collectors.toList());
        obsGravity.setAll(gravityList);
    }
}
