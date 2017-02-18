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
import javafx.scene.layout.VBox;

import imagefilters.model.OperationType;
import javafx.scene.layout.Pane;

/**
 *
 * @author asilkaratas
 */
public class OperationContainerView extends VBox
{
    private VBox containerBox;
    
    public OperationContainerView()
    {
        containerBox = new VBox();
        containerBox.setPrefHeight(130);
        
        BorderedTitledPane titledPane = new BorderedTitledPane("operation control", containerBox);
        getChildren().add(titledPane);
        
        AppModel.getInstance().getOperationType().addListener(new OperationTypeChangeHandler());
    }
    
    private void handleOperationType()
    {
        OperationType operationType = AppModel.getInstance().getOperationType().getValue();
        
        Pane view = null;
        if(operationType.equals(OperationType.THRESHOLDING))
        {
            view = new ThresholdingView();
        }
        else if(operationType.equals(OperationType.NORMALIZATION))
        {
            view = new NormalizationView();
        }
        else if(operationType.equals(OperationType.BRIGHTNESS))
        {
            view = new BrightnessView();
        }else if(operationType.equals(OperationType.EROSION))
        {
            view = new ErosionView();
        }
        else if(operationType.equals(OperationType.INVERSION))
        {
            view = new InversionView();
        }
        else if(operationType.equals(OperationType.MEDIAN))
        {
            view = new MedianFilterView();
        }
        else if(operationType.equals(OperationType.GRAYSCALE))
        {
            view = new GrayscaleFilterView();
        }
        else if(operationType.equals(OperationType.HIGHPASS))
        {
            view = new HighPassFilterView();
        }
        else if(operationType.equals(OperationType.LOWPASS))
        {
            view = new LowPassFilterView();
        }
        else if(operationType.equals(OperationType.GAUSSIAN))
        {
            view = new GaussianFilterView();
        }
        else if(operationType.equals(OperationType.SOBEL))
        {
            view = new SobelFilterView();
        }
        else if(operationType.equals(OperationType.SOBEL2))
        {
            view = new Sobel2FilterView();
        }
        else if(operationType.equals(OperationType.KMM))
        {
            view = new KMMFilterView();
        }else if(operationType.equals(OperationType.KM3))
        {
            view = new KM3FilterView();
        }
        
        containerBox.getChildren().clear();
        
        if(view != null)
        {
            containerBox.getChildren().add(view);
        }
    }
    
    private class OperationTypeChangeHandler implements ChangeListener<OperationType>
    {
        @Override
        public void changed(ObservableValue<? extends OperationType> observable, OperationType oldValue, OperationType newValue)
        {
            handleOperationType();
        }   
    } 
}
