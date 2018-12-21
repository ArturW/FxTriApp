/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.info;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

/**
 *
 * @author user
 */
public class TimeSavingsPane extends StackPane {
    
    ImageView imageView;
    
    public TimeSavingsPane() {        
        Image image = new Image("file:src/resources/time-savings.jpg");                
        //System.out.println("Image: " + image);        
        imageView = new ImageView(image);        
        //imageView.fitWidthProperty().bind(this.widthProperty());
        //imageView.fitHeightProperty().bind(this.heightProperty());        
        //System.out.println("ImageView: " + imageView);
        imageView.setFitWidth(800);
        imageView.setPreserveRatio(true);
        this.getChildren().add(imageView);
        this.setStyle("-fx-background-color: black;");
    }
    
}
