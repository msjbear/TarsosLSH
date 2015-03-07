package presentation;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.lang.reflect.Field;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javax.imageio.ImageIO;

import domain.ImageProcessing;
import domain.ImageProcessingImpl;


public class Main extends Application {
    
   
    public static void main(String[] args) {

	Application.launch(Main.class, (java.lang.String[])null);
	
    }
    
    @Override
    public void start(Stage primaryStage) {
	try {
            AnchorPane page = (AnchorPane) FXMLLoader.load(Main.class.getResource("MainWindow.fxml"));
            Scene scene = new Scene(page);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Surf");
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   
    
}
