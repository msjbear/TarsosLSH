package domain;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public interface ImageProcessing {
    
  /**
   * converts halfton image to binary image
   * @param img - a halfton image
   * @param porog - stresshold of binarisation
   * @return - a binared image
   */
  public BufferedImage toBinaryImage(final BufferedImage img,final int porog);
  
  /**
   * 
   * @param img
   * @return
   */
  public BufferedImage toHalfToning(BufferedImage img);
  
  /**
   * 
   * @param colorRGB
   * @return
   */
  public int[] getColorToRGB(int colorRGB);
  
  /**
   * 
   * @param r
   * @param g
   * @param b
   * @return
   */
  public int getColorFromRGB(int r, int g, int b);
  
  /**
   * 
   * @param sourceImage
   * @param channel
   * @return
   */
  public ArrayList<Integer> getChannelOfImage(BufferedImage sourceImage,
	                                      char channel);
  
  /**
   * Applies a mask to eliminate influences of brightness
   * 
   * @param sourceImage
   * @return
   */
  public BufferedImage getBrightnessFilteredImage(BufferedImage sourceImage);
  
  /**
   * Returns a HSV equivalent of RGB values of color
   * 
   * @param r - RED channel value
   * @param g - GREEN channel value
   * @param b - BLUE channel value
   * @return - hsv color value
   */
  public int[] rgb2hsv(int r, int g, int b);
  
  /**
   * Trancates an area from an image
   * 
   * @param sourceImage
   * @param AREA_WIDTH
   * @param AREA_HEIGHT
   * @return
   */
  public BufferedImage trancateAreaFromImage(BufferedImage sourceImage,
	  int AREA_WIDTH, int AREA_HEIGHT);
  
  /**
   * 
   * @param img
   * @return
   */
  public BufferedImage getNegativeOfImage(BufferedImage img);
  public void writeImageToFile(BufferedImage image, String typeOfImage, String path)throws IOException;
}
