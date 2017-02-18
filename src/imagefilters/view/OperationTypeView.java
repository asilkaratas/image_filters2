/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imagefilters.view;

import imagefilters.model.AppModel;
import java.util.ArrayList;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import imagefilters.model.OperationType;
import java.awt.image.BufferedImage;
import javafx.scene.layout.HBox;

/**
 *
 * @author asilkaratas
 */



public class OperationTypeView extends VBox
{
    public OperationTypeView()
    {
        ToggleGroup operationGroup = new ToggleGroup();
        operationGroup.selectedToggleProperty().addListener(new OperationTypeChangeListener());
        
        HBox hBox = new HBox();
        hBox.setSpacing(5);
        
        VBox vBox = null;
        OperationType[] operationTypes = OperationType.values();
        for(int i = 0; i < operationTypes.length; ++i)
        {
            if(i % 5 == 0)
            {
                vBox = new VBox();
                vBox.setSpacing(5);
                vBox.setPrefHeight(130);
                hBox.getChildren().add(vBox);
            }
            OperationType operationType = operationTypes[i];
            RadioButton operationButton = new RadioButton(operationType.getName());
            operationButton.setUserData(operationType);
            operationButton.setToggleGroup(operationGroup);
            
            vBox.getChildren().add(operationButton);
        }
        
        BorderedTitledPane titledPane = new BorderedTitledPane("operations", hBox);
        getChildren().add(titledPane);
        
        AppModel.getInstance().getOriginalImage().addListener(new OriginalImageChangeHandler());
        
        setDisable(true);
    }
    
    private void handleOriginalImage()
    {
        BufferedImage bufferedImage = AppModel.getInstance().getOriginalImage().getValue();
        setDisable(bufferedImage == null);
    }
    
    private class OriginalImageChangeHandler implements ChangeListener<BufferedImage>
    {
        @Override
        public void changed(ObservableValue<? extends BufferedImage> observable, BufferedImage oldValue, BufferedImage newValue)
        {
            handleOriginalImage();
        }   
    } 
            
    private class OperationTypeChangeListener implements ChangeListener<Toggle>
    {
        @Override
        public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue)
        {
            AppModel.getInstance().getFilteredImage().setValue(null);
            
            OperationType operationType = (OperationType)newValue.getUserData();
            AppModel.getInstance().getOperationType().setValue(operationType);
            
        }
    }
}



