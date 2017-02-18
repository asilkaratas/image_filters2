/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imagefilters.view;

import imagefilters.model.AppModel;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javax.imageio.ImageIO;

/**
 *
 * @author asilkaratas
 */
public class ImagePreviewView extends HBox implements EventHandler<ActionEvent>
{
    public static double IMAGE_WIDTH = 350;
    public static double IMAGE_HEIGHT = 350;
    
    private ImageView originalImageView;
    private ImageView filteredImageView;
    
    private CheckBox showResultCheckBox;
    
    private Button applyButton;
    
    public ImagePreviewView()
    {
        originalImageView = new ImageView();
        originalImageView.setFitWidth(IMAGE_WIDTH);
        originalImageView.setFitHeight(IMAGE_HEIGHT);
        originalImageView.setPreserveRatio(true);
        
        VBox originalImageBox = new VBox();
        originalImageBox.setPrefSize(IMAGE_WIDTH, IMAGE_HEIGHT);
        originalImageBox.setAlignment(Pos.CENTER);
        originalImageBox.getChildren().add(originalImageView);
        
        BorderedTitledPane originalTitledPane = new BorderedTitledPane("original image", originalImageBox);
        
       applyButton = new Button("apply");
       applyButton.setOnAction(this);
        
        showResultCheckBox = new CheckBox("show result");
        showResultCheckBox.selectedProperty().addListener(new SelectedChangeHandler());
        
        filteredImageView = new ImageView();
        filteredImageView.setFitWidth(IMAGE_WIDTH);
        filteredImageView.setFitHeight(IMAGE_HEIGHT);
        filteredImageView.setPreserveRatio(true);
        filteredImageView.setSmooth(false);
        
        VBox filteredImageBox = new VBox();
        filteredImageBox.setPrefSize(IMAGE_WIDTH, IMAGE_HEIGHT);
        filteredImageBox.setAlignment(Pos.CENTER);
        filteredImageBox.getChildren().add(filteredImageView);
        
        VBox filteredContainerBox = new VBox();
        filteredContainerBox.setSpacing(5);
        filteredContainerBox.getChildren().addAll(showResultCheckBox, applyButton, filteredImageBox);
        
        BorderedTitledPane filteredTitledPane = new BorderedTitledPane("filtered image", filteredContainerBox);
 
        getChildren().addAll(originalTitledPane, filteredTitledPane);
        
        
        AppModel.getInstance().getOriginalImage().addListener(new OriginalImageChangeHandler());
        AppModel.getInstance().getFilteredImage().addListener(new FilteredImageChangeHandler());
    }
    
    private Image convertImage(BufferedImage bufferedImage)
    {
        Image image = null;
        try
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", baos);
            InputStream is = new ByteArrayInputStream(baos.toByteArray());
            image = new Image(is, IMAGE_WIDTH, IMAGE_HEIGHT, true, false);
        } 
        catch (IOException ex)
        {
            Logger.getLogger(ImagePreviewView.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        return image;
    }
    
    private void update()
    {
        handleOriginalImage();
        
        if(showResultCheckBox.isSelected())
        {
            handleFilteredImage();
        }
        else
        {
            handleOriginalImageForResult();
        }
    }
    
    private void handleOriginalImage()
    {
        
        BufferedImage bufferedImage = AppModel.getInstance().getOriginalImage().getValue();
        if(bufferedImage != null)
        {
            Image image = convertImage(bufferedImage);
            originalImageView.setImage(image); 
        }
        else
        {
           originalImageView.setImage(null);
        }
    }
    
     private void handleOriginalImageForResult()
    {
        BufferedImage bufferedImage = AppModel.getInstance().getOriginalImage().getValue();
        if(bufferedImage != null)
        {
            Image image = convertImage(bufferedImage);
            filteredImageView.setImage(image); 
        }
        else
        {
           filteredImageView.setImage(null);
        }
    }
    
    private void handleFilteredImage()
    {
        BufferedImage bufferedImage = AppModel.getInstance().getFilteredImage().getValue();
        if(bufferedImage != null)
        {
            Image image = convertImage(bufferedImage);
            filteredImageView.setImage(image);    
        }
        else
        {
           filteredImageView.setImage(null);
           
        }
    }
    
    private class FilteredImageChangeHandler implements ChangeListener<BufferedImage>
    {
        @Override
        public void changed(ObservableValue<? extends BufferedImage> observable, BufferedImage oldValue, BufferedImage newValue)
        {
            showResultCheckBox.setSelected(true);
            update();
        }   
    }
    
    private class OriginalImageChangeHandler implements ChangeListener<BufferedImage>
    {
        @Override
        public void changed(ObservableValue<? extends BufferedImage> observable, BufferedImage oldValue, BufferedImage newValue)
        {
            showResultCheckBox.setSelected(true);
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
    
    @Override
    public void handle(ActionEvent event)
    {
        BufferedImage filteredImage = AppModel.getInstance().getFilteredImage().getValue();
        AppModel.getInstance().getOriginalImage().setValue(filteredImage);
    }
}
