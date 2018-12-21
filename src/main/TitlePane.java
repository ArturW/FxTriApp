/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

/**
 *
 * @author user
 */
public class TitlePane extends StackPane
{
    ImageView imageView;
    
    public TitlePane() {        
        Image image = new Image("file:src/resources/E-119.jpg");                
        //System.out.println("Image: " + image);        
        imageView = new ImageView(image);        
        //imageView.fitWidthProperty().bind(this.widthProperty());
        //imageView.fitHeightProperty().bind(this.heightProperty());        
        //System.out.println("ImageView: " + imageView);
        imageView.setFitWidth(800);
        imageView.setPreserveRatio(true);
                
        StackPane aboutPane = new AboutPane();
        aboutPane.setAlignment(Pos.TOP_LEFT);
        
        this.getChildren().addAll(imageView, aboutPane);
        this.setStyle("-fx-background-color: black;");
    }
    
}
