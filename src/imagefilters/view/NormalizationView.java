/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imagefilters.view;

import imagefilters.filter.NormalizationFilter;
import imagefilters.filter.ThresholdFilter;
import imagefilters.model.AppModel;
import imagefilters.model.OperationType;
import java.awt.image.BufferedImage;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;

/**
 *
 * @author asilkaratas
 */
public class NormalizationView extends VBox
{
    private CheckBox channelCheckBox;
    public NormalizationView()
    {
        channelCheckBox = new CheckBox("use channel");
        channelCheckBox.selectedProperty().addListener(new SelectedChangeHandler());
        
        VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.getChildren().add(channelCheckBox);
        
        String title = OperationType.NORMALIZATION.getName();
        BorderedTitledPane titledPane = new BorderedTitledPane(title, vbox);
        getChildren().add(titledPane);
        
        update();
    }
    
    private void update()
    {
        AppModel appModel = AppModel.getInstance();
        BufferedImage in = appModel.getOriginalImage().getValue();
        boolean useChannel = channelCheckBox.isSelected();
        
        BufferedImage out = NormalizationFilter.apply(in, useChannel);
        
        appModel.getFilteredImage().set(out);
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
