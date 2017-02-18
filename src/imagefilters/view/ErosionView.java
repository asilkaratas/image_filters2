/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imagefilters.view;

import imagefilters.filter.BrightnessFilter;
import imagefilters.filter.ErosionFilter;
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

/**
 *
 * @author asilkaratas
 */
public class ErosionView extends VBox
{
    private Label valueLabel;
    private Slider slider;
    private CheckBox usePercentageCheckBox;
    
    public ErosionView()
    {
        usePercentageCheckBox = new CheckBox("use percentage");
        usePercentageCheckBox.selectedProperty().addListener(new SelectedChangeHandler());
        
        slider = new Slider(-255, 255, 125);
        slider.valueProperty().addListener(new ValueChangeHandler());
        
        valueLabel = new Label();
        
        HBox hbox = new HBox();
        hbox.setSpacing(5);
        hbox.getChildren().addAll(slider, valueLabel);
        
        
        VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.getChildren().addAll(usePercentageCheckBox, hbox);
        
        String title = OperationType.BRIGHTNESS.getName();
        BorderedTitledPane titledPane = new BorderedTitledPane(title, vbox);
        getChildren().add(titledPane);
        
        update();
    }
    
    private void update()
    {
        AppModel appModel = AppModel.getInstance();
        BufferedImage in = appModel.getOriginalImage().getValue();
        int brightness = (int)slider.getValue();
        boolean usePercentage = usePercentageCheckBox.isSelected();
        
        valueLabel.setText(Integer.toString(brightness));
        
        BufferedImage out = ErosionFilter.apply(in, brightness);
        appModel.getFilteredImage().set(out);
    }
    
    
    private class ValueChangeHandler implements ChangeListener<Number>
    {
        @Override
        public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
        {
            update();
        }   
    } 
    
    private class SelectedChangeHandler implements ChangeListener<Boolean>
    {
        @Override
        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
        {
            update();
        }   
    } 
    
    
}
