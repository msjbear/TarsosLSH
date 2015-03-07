package presentation;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import domain.ImageProcessing;
import domain.ImageProcessingImpl;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import jopensurf.SurfCompare;

public class MainWindowController implements Initializable {

	@FXML
	Parent root;
	  @FXML
	    private ResourceBundle resources;

	    @FXML
	    private URL location;

	    @FXML
	    private MenuItem menuItemOpenCarImage;
	    @FXML
	    private ImageView mainView;

	    @FXML
	    private MenuItem menuItemOpenEmblemImage;
	    @FXML
	    private ImageView carImageView;
	    
	    @FXML
	    private ImageView emblemImageView;
	    @FXML
	    private MenuItem menuItemRun;
	    @FXML
	    private MenuItem menuItemExit;
	    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		 assert menuItemOpenCarImage != null : "fx:id=\"menuItemOpenCarImage\" was not injected: check your FXML file 'MainWindow.fxml'.";
	     assert menuItemOpenEmblemImage != null : "fx:id=\"menuItemOpenEmblemImage\" was not injected: check your FXML file 'MainWindow.fxml'.";
	     assert carImageView != null : "fx:id=\"carImageView\" was not injected: check your FXML file 'MainWindow.fxml'.";
	     assert emblemImageView != null : "fx:id=\"emblemImageView\" was not injected: check your FXML file 'MainWindow.fxml'.";
	     assert menuItemExit != null : "fx:id=\"menuItemExit\" was not injected: check your FXML file 'MainWindow.fxml'.";   
	     assert menuItemRun != null : "fx:id=\"menuItemRun\" was not injected: check your FXML file 'MainWindow.fxml'.";
	     assert mainView != null : "fx:id=\"mainView\" was not injected: check your FXML file 'MainWindow.fxml'.";  
	}
	 private String imagePath="C:\\";
	 BufferedImage carImage;
	 BufferedImage emblemImage;
	 
	 @FXML
	void openCarImage(ActionEvent event) throws IOException {
		 mainView.setImage(null);
		 FileChooser fileChooser = new FileChooser();
			fileChooser.setInitialDirectory(new File(imagePath));
			displayMessage("New issue created for ");
			
			File file = fileChooser.showOpenDialog(null);
			if (file != null) {
			    System.out.println(file);
			    carImage=ImageIO.read(file);
			    setUpCarImageView(carImage);
			
			}
	}

	@FXML
    void openEmblemImage(ActionEvent event) throws IOException {
		 mainView.setImage(null);
		 FileChooser fileChooser = new FileChooser();
			fileChooser.setInitialDirectory(new File(imagePath));
			displayMessage("New issue created for ");
			
			File file = fileChooser.showOpenDialog(null);
			if (file != null) {
			    System.out.println(file);	
			    emblemImage=ImageIO.read(file);
			    setUpEmblemImageView(emblemImage);
			
			}
	}
	public void displayMessage(String message) {
			System.out.println(message);
    }
	public void setUpCarImageView(BufferedImage image) {
		Image image1 = SwingFXUtils.toFXImage(image, null);
		carImageView.setImage(image1);
		
		//imageView.setFitWidth(100);
		carImageView.setPreserveRatio(true);
		carImageView.setSmooth(true);
		carImageView.setCache(true);
	}    
	
	public void setUpEmblemImageView(BufferedImage image) {
		Image image1 = SwingFXUtils.toFXImage(image, null);
		emblemImageView.setImage(image1);
		
		//imageView.setFitWidth(100);
		emblemImageView.setPreserveRatio(true);
		emblemImageView.setSmooth(true);
		emblemImageView.setCache(true);
	}   
	 @FXML
	void exit(ActionEvent event) {
	 System.exit(0);
	}
	 
	 @FXML
    void run(ActionEvent event) throws IOException {
		 ImageProcessing ip = new ImageProcessingImpl();
		 final SurfCompare sc= new SurfCompare(ip.trancateAreaFromImage(carImage, 200,200),emblemImage);
	   //  sc.display();
	   //  sc.matchesInfo();
		//Dialogs.showInformationDialog(stage, "I have a great message for you!",			    "Information Dialog", "title");
		
		 
		 BufferedImage im=sc.getBufferedImageOfPanel();
	  ip.writeImageToFile(im,"jpg", "C:\\11.jpg");
	     Canvas canvas = new Canvas(im.getWidth(),im.getHeight());
	     final GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
	     
	    
	     
	    // graphicsContext.fillText("lklikjk",12.3, 145.3);
	     graphicsContext.drawImage(SwingFXUtils.toFXImage(im, null),0, 0);
	     
	     WritableImage wim = new WritableImage(im.getWidth(),im.getHeight());
	     canvas.snapshot(null, wim);
	   //  im=SwingFXUtils.fromFXImage(wim, null);
	     mainView.setImage(wim);
	     mainView.setPreserveRatio(true);
	     mainView.setSmooth(true);
	     mainView.setCache(true);   
	     int porog = 3; //      
	     String out_str="";
	     if (sc.getResultOfMatching()>porog)
	         out_str="";
	     else
	    	 out_str="";
	     final String out=out_str;
	     Thread t = new Thread(new Runnable(){
	         public void run(){
	        	  JOptionPane.showMessageDialog(null,    out);
	         }
	     });
	   t.start();
	   
	 }
	 
}
