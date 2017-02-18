/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imagefilters.view;

import imagefilters.model.AppModel;
import imagefilters.model.ImageExtensions;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;

/**
 *
 * @author asilkaratas
 */
public class ImageLoaderView extends VBox implements EventHandler<ActionEvent>
{
    public ImageLoaderView()
    {
        Button loadButton = new Button();
        loadButton.setText("load");
        loadButton.setOnAction(this);
        
        VBox vBox = new VBox();
        vBox.getChildren().add(loadButton);
        vBox.setPrefHeight(130);
        
        BorderedTitledPane titledPane = new BorderedTitledPane("load image", vBox);
        getChildren().add(titledPane);
    }

    @Override
    public void handle(ActionEvent event)
    {
        System.out.println("Here:" + event.getSource());
        
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(ImageExtensions.getImageFilter());

        File file = fileChooser.showOpenDialog(getScene().getWindow());

        if(file != null)
        {
            BufferedImage image = null;
            try 
            {
                image = ImageIO.read(file);
            } 
            catch (IOException ex)
            {
                Logger.getLogger(ImageLoaderView.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            AppModel.getInstance().getOriginalImage().setValue(image);
        }
    
    }
}
