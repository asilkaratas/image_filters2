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
import imagefilters.filter.KM3Filter;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;

/**
 *
 * @author asilkaratas
 */
public class KM3FilterView extends VBox
{
    private enum Step
    {
        START("start"),
        CONVERT_MAP("convert map"),
        PHASE0("phase0"),
        PHASE1("phase1"),
        PHASE2("phase2"),
        PHASE3("phase3"),
        PHASE4("phase4"),
        PHASE5("phase5"),
        PHASE6("phase6"),
        PHASE_SKELETON("phase skeleton");
        
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
    
    public KM3FilterView()
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
                out = KM3Filter.getInstance().start(in);
                setStep(Step.CONVERT_MAP);
                
            }
            else if(step == Step.CONVERT_MAP)
            {
                out = KM3Filter.getInstance().covertMap();
                setStep(Step.PHASE0);
            }
            else if(step == Step.PHASE0)
            {
                out = KM3Filter.getInstance().phase0();
                setStep(Step.PHASE1);
            }
            else if(step == Step.PHASE1)
            {
                out = KM3Filter.getInstance().phase1();
                setStep(Step.PHASE2);
            }
            else if(step == Step.PHASE2)
            {
                out = KM3Filter.getInstance().phase2();
                setStep(Step.PHASE3);
            }
            else if(step == Step.PHASE3)
            {
                out = KM3Filter.getInstance().phase3();
                setStep(Step.PHASE4);
            }
            else if(step == Step.PHASE4)
            {
                out = KM3Filter.getInstance().phase4();
                setStep(Step.PHASE5);
            }
            else if(step == Step.PHASE5)
            {
                out = KM3Filter.getInstance().phase4();
                setStep(Step.PHASE6);
            }
            else if(step == Step.PHASE6)
            {
                out = KM3Filter.getInstance().phase6();
                if(KM3Filter.getInstance().isModified())
                {
                    setStep(Step.PHASE0);
                }
                else
                {
                    setStep(Step.PHASE_SKELETON);
                }
            }
            else if(step == Step.PHASE_SKELETON)
            {
                out = KM3Filter.getInstance().findSkeleton();
                
                setSkeletonFound(true);
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
