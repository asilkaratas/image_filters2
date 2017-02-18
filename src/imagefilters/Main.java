/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imagefilters;

import imagefilters.model.AppModel;
import imagefilters.view.MainView;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author asilkaratas
 */
public class Main extends Application
{
    
    @Override
    public void start(Stage primaryStage)
    {  
        MainView mainView = new MainView();
        
        Scene scene = new Scene(mainView, 800, 650);
        scene.getStylesheets().add(AppModel.CSS);
        
        primaryStage.setTitle("Image Filters");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        launch(args);
    }
    
}
