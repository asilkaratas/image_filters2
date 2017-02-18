/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imagefilters.view;

import imagefilters.model.AppModel;
import java.awt.image.BufferedImage;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 *
 * @author asilkaratas
 */
public class ControlsView extends StackPane
{
    public ControlsView()
    {
        ImageLoaderView imageLoaderView = new ImageLoaderView();
        OperationTypeView operationTypeView = new OperationTypeView();
        OperationContainerView operationContainerView = new OperationContainerView();
        
        HBox box = new HBox();
        box.getChildren().addAll(imageLoaderView, operationTypeView, operationContainerView);
        
        BorderedTitledPane titledPane = new BorderedTitledPane("controls", box);
        getChildren().add(titledPane);
        
        
    }
    
    
}
