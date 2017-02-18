/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imagefilters.view;

import imagefilters.filter.ThresholdFilter;
import imagefilters.filter.convolusion.ConvolutionFilter;
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

/**
 *
 * @author asilkaratas
 */
public class Sobel2FilterView extends VBox
{
    private ConvolutionFilter filter;
    public Sobel2FilterView()
    {
        int[] kernel = {
            0, 1, 0,
            1, -4, 1,
            0, 1, 0
        };
        filter = new ConvolutionFilter("Sobel2", kernel, 1, 0);
        
        update();
    }
    
    private void update()
    {
        AppModel appModel = AppModel.getInstance();
        BufferedImage in = appModel.getOriginalImage().getValue();
        
        
        BufferedImage out = filter.apply(in);
        
        appModel.getFilteredImage().set(out);
    }
    
    
}
