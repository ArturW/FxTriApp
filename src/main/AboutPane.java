/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import static main.StyleSheetConstants.ABOUTLABEL;

/**
 *
 * @author user
 */
public class AboutPane extends StackPane {
    
    AboutPane() {                
        final Dialog<ButtonType> dialog = new Alert(Alert.AlertType.INFORMATION);
        //Dialog dialog = new ChoiceDialog(Alert.AlertType.INFORMATION);
        //Dialog dialog = new TextInputDialog();
        dialog.setTitle("About FXTriApp");
        dialog.setHeaderText("Welcome to FXTriApp");
        dialog.setContentText("'Artur Wojtas'<awojtas@gmail.com>");
        dialog.setGraphic(null);
        
        final Hyperlink link = new Hyperlink("About");
        link.getStyleClass().add(ABOUTLABEL);
        link.setVisited(false);
        link.setFocusTraversable(false);
                
        link.setOnAction(value -> {
            dialog.showAndWait();
        });
        
        this.getChildren().add(link);
        
    }
    
    
}
