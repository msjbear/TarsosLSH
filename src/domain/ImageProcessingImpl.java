/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * 
 * @author Dima
 *
 */
public class ImageProcessingImpl implements ImageProcessing {	
 
	
  /*
   *  Преобразование изображения в бинарное
   */
  public BufferedImage toBinaryImage(final BufferedImage img,final int porog) {
    final int iw = img.getWidth();
    final int ih = img.getHeight();
    BufferedImage img1 = new BufferedImage(iw, ih, BufferedImage.TYPE_INT_RGB);
    
    for (int i = 0;i < ih;i++) {
      for (int j = 0;j < iw;j++) {
        int c = img.getRGB(j,i); 
        
        if ((c & 255) > porog)
          //255- blue если изобр. цветное о преобразование будет идти по синему каналу
          // 65280 -green 16711680- red
          img1.setRGB(j,i, getColorFromRGB(0, 0, 0)); 
        else
          img1.setRGB(j, i, getColorFromRGB(255, 255, 255));                      
      }     
    }
    return img1;
  } 
  
  /*
   *  Запись изображения в файл
   */
  public void writeImageToFile(BufferedImage image, String typeOfImage, String path) throws IOException {                   
    File outputFile = new File(path);
    ImageIO.write(image, typeOfImage, outputFile);    
  }
  
  /*
   *  заполняет массив интенсивностями пикселей
   */
  public ArrayList<Integer> toHalfToningImage(BufferedImage img) {
    ArrayList<Integer> mas = new ArrayList<Integer>();     
    int iw = img.getWidth();
    int ih = img.getHeight();
    
    for (int i = 0;i < ih;i++) {
      for (int j = 0;j < iw;j++) {
        int c = img.getRGB(j,i);             
        int a = (int)(((c & 16711680) >> 16) * 0.3        // r
        		   + ((c & 65280) >> 8) * 0.59    // g
        		   + (c & 255) * 0.11);           // b
        mas.add(a);
      }
    }
    return mas;
  } 
  
  /*
   * 
   */
  public BufferedImage toHalfToning(BufferedImage img) {
    int iw = img.getWidth();
    int ih = img.getHeight();
    
    BufferedImage img1 = new BufferedImage(iw, ih, BufferedImage.TYPE_INT_RGB);
    for (int i = 0;i < ih;i++) {
      for (int j = 0;j < iw;j++) {
        int c = img.getRGB(j,i);             
        int a = (int)(((c & 16711680) >> 16) * 0.3 
        		  + ((c & 65280) >> 8) * 0.59
        		  + ((c & 255)) * 0.11);
        img1.setRGB(j, i, getColorFromRGB(a, a, a));
      }
    }
    return img1;
  } 
  
  public ArrayList<Integer> getChannelOfImage(BufferedImage sourceImage, char channel) {
    ArrayList<Integer> pixelsMap = new ArrayList<Integer>();     
    int iw = sourceImage.getWidth();
    int ih = sourceImage.getHeight();
    int index;
    
    switch (channel) {
    
      case 'r':
	  index = 0;
	break;
	
      case 'g':
	  index = 1;
	break;
	
      default:
	  index = 2;	
    }    
    for (int i = 0;i < ih;i++) {
      for (int j = 0; j < iw ;j++) {
        int c = sourceImage.getRGB(j,i);         
        pixelsMap.add(getColorToRGB(c)[2]);
      }
    }
    return pixelsMap;
  }
  
  public int[] getColorToRGB(int colorRGB) {    
    int r = (colorRGB & 16711680) >> 16;
    int g = (colorRGB & 65280) >> 8;
    int b = (colorRGB & 255);
    int[] rgb = new int[]{r, g, b};
    
    return rgb;
  }
  
  public int getColorFromRGB(int r, int g, int b) {
    int colorRGB = (r << 16) | (g << 8) | b;
    return colorRGB;
  }
  
  static final int MASK_SIZE = 15; // odd number
  
  public BufferedImage getBrightnessFilteredImage(BufferedImage sourceImage) {
    // forming the filter's mask
    float[] mask = new float[MASK_SIZE * MASK_SIZE];
    for (int i = 0;i < MASK_SIZE * MASK_SIZE / 2;i++) {
	mask[i] = -1 / (MASK_SIZE * MASK_SIZE - 1);
	mask[MASK_SIZE * MASK_SIZE - i-1] = -1 / (MASK_SIZE * MASK_SIZE - 1);
    }
    mask[MASK_SIZE * MASK_SIZE / 2] = 1; 
    
    Kernel k = new Kernel(MASK_SIZE, MASK_SIZE, mask);
    ConvolveOp op1 = new ConvolveOp(k);
    BufferedImage blurry = op1.filter(sourceImage, null);
     
    return blurry;
  } 
  
  public int[] rgb2hsv(int r, int g, int b) {
	int[] hsv = new int[3];
	int min;    //Min. value of RGB
	int max;    //Max. value of RGB
	int delMax; //Delta RGB value
	
	if (r > g) { min = g; max = r; }
	else { min = r; max = g; }
	if (b > max) max = b;
	if (b < min) min = b;
							
	delMax = max - min;

	float H = 0, S;
	float V = max;
	   
	if ( delMax == 0 ) { H = 0; S = 0; }
	else {                                   
		S = delMax / 255f;
		if ( r == max ) 
			H = ((g - b) / (float)delMax) * 60;
		else if ( g == max ) 
			H = ( 2 + (b - r) / (float)delMax) * 60;
		else if ( b == max ) 
			H = ( 4 + (r - g) / (float)delMax) * 60;   
	}								 
	hsv[0] = (int)(H);
	hsv[1] = (int)(S * 100);
	hsv[2] = (int)(V);
      return hsv;
  }
  
  /**
   * Performs trancating an area of image which
   * will be used in calculating Laws features
   *  
   * @param sourceImage
   * @return
   */  
  public BufferedImage trancateAreaFromImage(BufferedImage sourceImage,
	  int AREA_WIDTH, int AREA_HEIGHT) {
    if ((AREA_WIDTH == 0) || (AREA_HEIGHT == 0)) {
	return sourceImage;
    }
    final int iw = sourceImage.getWidth();
    final int ih = sourceImage.getHeight();
    
    int x = iw / 2 - AREA_WIDTH/2;
    int y = ih / 2 - AREA_HEIGHT/2;
    
    BufferedImage result = sourceImage.getSubimage(x, y+50, AREA_WIDTH, AREA_HEIGHT);
   // new Window().setImage(result,"SubImage");
    return result;
  }
  

  public BufferedImage getNegativeOfImage(BufferedImage img) {
      int iw=img.getWidth();
      int ih=img.getHeight();
      BufferedImage img1=new BufferedImage(iw, ih,  BufferedImage.TYPE_INT_RGB);
      
      for(int i=0;i<ih;i++){
         for(int j=0;j<iw;j++){
            int c= img.getRGB(j,i);        
            int a = (int)(((c & 16711680) >> 16) * 0.3 
  		  + ((c & 65280) >> 8) * 0.59
  		  + ((c & 255)) * 0.11);
            img1.setRGB(j, i, getColorFromRGB(~a, ~a, ~a));
         }
      }
      return img1;  
  }

}
