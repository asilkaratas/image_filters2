/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imagefilters.view;

import imagefilters.filter.InversionFilter;
import imagefilters.filter.ThresholdFilter;
import imagefilters.model.AppModel;
import imagefilters.model.Color;
import imagefilters.model.OperationType;
import java.awt.image.BufferedImage;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javax.swing.event.DocumentEvent;
import imagefilters.filter.GrayscaleFilter;

/**
 *
 * @author asilkaratas
 */
public class GrayscaleFilterView extends VBox
{
    
    public GrayscaleFilterView()
    {
        
        update();
    }
    
    private void update()
    {
        AppModel appModel = AppModel.getInstance();
        BufferedImage in = appModel.getOriginalImage().getValue();
       
        
        BufferedImage out = GrayscaleFilter.apply(in);
        
        appModel.getFilteredImage().set(out);
    }
    
    
}
