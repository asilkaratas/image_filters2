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
import imagefilters.filter.KMMFilter;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;

/**
 *
 * @author asilkaratas
 */
public class KMMFilterView extends VBox
{
    private enum Step
    {
        START("start"),
        CONVERT_MAP("convert map"),
        CONVERT_TO_2("convert to 2"),
        CONVERT_TO_3("convert to 3"),
        CALCULATE_4("calculate 4"),
        DELETE_4("delete 4"),
        CALCULATE_WEIGHT_2("calculate weight 2"),
        CALCULATE_WEIGHT_3("calculate weight 3");
        
        private String name;
        private Step(String name)
        {
            this.name = name;
        }
        
        public String getName()
        {
            return name;
        }
        
    }
    private Button resetButton;
    private Button actionButton;
    private Label skeletonFoundLabel;
    
    private Step step = Step.START;
    
    public KMMFilterView()
    {
        resetButton = new Button("reset");
        resetButton.setOnAction(new ResetButtonHandler());
        
        
        actionButton = new Button(step.getName());
        actionButton.setOnAction(new ActionButtonHandler());
        
        skeletonFoundLabel = new Label("One pixel skeleton is found");
        
        VBox vBox = new VBox();
        vBox.getChildren().addAll(actionButton, resetButton, skeletonFoundLabel);
        
        String title = OperationType.KMM.getName();
        BorderedTitledPane titledPane = new BorderedTitledPane(title, vBox);
        getChildren().add(titledPane);
        
        setStep(Step.START);
        setSkeletonFound(false);
    }
    
    private void update()
    {
        
    }
   
    private void setStep(Step step)
    {
        this.step = step;
        actionButton.setText(step.getName());
        
        resetButton.setVisible(step != Step.START);
    }
    
    private void setSkeletonFound(boolean isFound)
    {
        skeletonFoundLabel.setVisible(isFound);
        actionButton.setVisible(!isFound);
    }
    
    private class ActionButtonHandler implements EventHandler<ActionEvent>
    {
        @Override
        public void handle(ActionEvent event)
        {
            AppModel appModel = AppModel.getInstance();
            BufferedImage in = appModel.getOriginalImage().getValue();
        
            System.out.println(step.getName());
            
            BufferedImage out = null;
            if(step == Step.START)
            {
                out = KMMFilter.getInstance().start(in);
                setStep(Step.CONVERT_MAP);
            }
            else if(step == Step.CONVERT_MAP)
            {
                out = KMMFilter.getInstance().covertMap();
                setStep(Step.CONVERT_TO_2);
            }
            else if(step == Step.CONVERT_TO_2)
            {
                out = KMMFilter.getInstance().convertTo2();
                setStep(Step.CONVERT_TO_3);
            }
            else if(step == Step.CONVERT_TO_3)
            {
                out = KMMFilter.getInstance().convertTo3();
                setStep(Step.CALCULATE_4);
            }
            else if(step == Step.CALCULATE_4)
            {
                out = KMMFilter.getInstance().calculate4();
                setStep(Step.DELETE_4);
            }
            else if(step == Step.DELETE_4)
            {
                out = KMMFilter.getInstance().delete4();
                setStep(Step.CALCULATE_WEIGHT_2);
            }
            else if(step == Step.CALCULATE_WEIGHT_2)
            {
                out = KMMFilter.getInstance().calculateWeight2();
                setStep(Step.CALCULATE_WEIGHT_3);
            }
            else if(step == Step.CALCULATE_WEIGHT_3)
            {
                out = KMMFilter.getInstance().calculateWeight3();
                
                if(KMMFilter.getInstance().isOnePixelSkeleton())
                {
                    setSkeletonFound(true);
                }
                else
                {
                    setStep(Step.CONVERT_TO_2);
                }
            }
            
            appModel.getFilteredImage().set(out);
        }
    }
    
    private class ResetButtonHandler implements EventHandler<ActionEvent>
    {
        @Override
        public void handle(ActionEvent event)
        {
            setStep(Step.START);
            setSkeletonFound(false);
        }
    }
    
    
    
}
