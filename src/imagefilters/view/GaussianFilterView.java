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
public class GaussianFilterView extends VBox
{
    private ConvolutionFilter filter;
    public GaussianFilterView()
    {
        int[] kernel = {
            1, 4, 1,
            4, 16, 4,
            1, 4, 1
        };
        filter = new ConvolutionFilter("Gaussian", kernel, 36, 0);
        
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
