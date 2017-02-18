/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imagefilters.view;

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
public class ThresholdingView extends VBox
{
    private Label valueLabel;
    private CheckBox invertedCheckBox;
    private CheckBox coloredCheckBox;
    private Slider slider;
    
    
    public ThresholdingView()
    {
        slider = new Slider(0, 255, 125);
        slider.valueProperty().addListener(new ValueChangeHandler());
        
        valueLabel = new Label();
        
        HBox hbox = new HBox();
        hbox.setSpacing(5);
        hbox.getChildren().addAll(slider, valueLabel);
        
        invertedCheckBox = new CheckBox("inverted");
        invertedCheckBox.selectedProperty().addListener(new SelectedChangeHandler());
        coloredCheckBox = new CheckBox("colored");
        coloredCheckBox.selectedProperty().addListener(new SelectedChangeHandler());
        
        VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.getChildren().addAll(invertedCheckBox, coloredCheckBox, hbox);
        
        String title = OperationType.THRESHOLDING.getName();
        BorderedTitledPane titledPane = new BorderedTitledPane(title, vbox);
        getChildren().add(titledPane);
        
        update();
    }
    
    private void update()
    {
        AppModel appModel = AppModel.getInstance();
        BufferedImage in = appModel.getOriginalImage().getValue();
        boolean inverted = invertedCheckBox.isSelected();
        boolean colored = coloredCheckBox.isSelected();
        int threshold = (int)slider.getValue();
        
        valueLabel.setText(Integer.toString(threshold));
        
        BufferedImage out = ThresholdFilter.apply(in, threshold, inverted, colored);
        
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
