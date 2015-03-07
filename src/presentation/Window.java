package presentation;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;

/**
 * Displays image resulting of filtering
 * @author Dima
 *
 */
public class Window extends JFrame {
  BufferedImage img;
  
  public Window() {
    this.setSize(300, 300);
    this.setVisible(true);
   // this.setDefaultCloseOperation(EXIT_ON_CLOSE);
  }
  
  public void paint(Graphics g) {
    g.drawImage(img, 0, 10, img.getWidth(), img.getHeight() + 10,null);
    this.setSize(img.getWidth(), img.getHeight());
  }
  
  public void setImage(BufferedImage img,String title) {
    this.img = img;
    this.setTitle(title);
  }
    
}
