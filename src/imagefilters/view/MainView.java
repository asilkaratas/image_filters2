/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imagefilters.view;

import java.awt.image.ImageProducer;
import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 *
 * @author asilkaratas
 */
public class MainView extends StackPane
{
    public MainView()
    {
        ControlsView controlsView = new ControlsView();
        ImagePreviewView imagePreviewView = new ImagePreviewView();
        
        SplitPane splitPane = new SplitPane();
        splitPane.setOrientation(Orientation.VERTICAL);
        splitPane.getItems().addAll(controlsView, imagePreviewView);
        splitPane.setDividerPositions(.32, .68);
        
        getChildren().add(splitPane);
    }
}
