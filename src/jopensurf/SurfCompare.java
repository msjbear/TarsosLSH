/*
This work was derived from Chris Evan's opensurf project and re-licensed as the
3 clause BSD license with permission of the original author. Thank you Chris! 

Copyright (c) 2010, Andrew Stromberg
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
    * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright
      notice, this list of conditions and the following disclaimer in the
      documentation and/or other materials provided with the distribution.
    * Neither Andrew Stromberg nor the
      names of its contributors may be used to endorse or promote products
      derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL Andrew Stromberg BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package jopensurf;

import domain.ImageProcessingImpl;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.*;
//import org.imgscalr.Scalr;

import presentation.Window;


public class SurfCompare extends JPanel {
	private static final long serialVersionUID = 1L;

	private static final int BASE_CIRCLE_DIAMETER = 8;
	private static final int TARGET_CIRCLE_DIAMETER = 4;
	private static final int UNMATCHED_CIRCLE_DIAMETER = 4;
	
	private BufferedImage image;
	private BufferedImage imageB;
	private float mImageAXScale = 0;
	private float mImageAYScale = 0;
	private float mImageBXScale = 0;
	private float mImageBYScale = 0;
	private int mImageAWidth = 0;
	private int mImageAHeight = 0;
	private int mImageBWidth = 0;
	private int mImageBHeight = 0;
	private Surf mSurfA;
	private Surf mSurfB;
	
	private Map<SURFInterestPoint,SURFInterestPoint> mAMatchingPoints;
	private Map<SURFInterestPoint,SURFInterestPoint> mBMatchingPoints;

	private boolean mUpright = false;
	
    public SurfCompare(BufferedImage image,BufferedImage imageB,float balanceValue, float threshold, int octaves){
        this.image = image;
    	this.imageB = imageB;
    	mSurfA = new Surf(image, balanceValue,  threshold,  octaves);
    	mSurfB = new Surf(imageB, balanceValue,  threshold,  octaves);

    	mImageAXScale = (float)Math.min(image.getWidth(),800)/(float)image.getWidth();
    	mImageAYScale = (float)Math.min(image.getHeight(),800 * (float)image.getHeight()/(float)image.getWidth())/(float)image.getHeight();
    	
    	mImageBXScale = (float)Math.min(imageB.getWidth(),800)/(float)imageB.getWidth();
    	mImageBYScale = (float)Math.min(imageB.getHeight(),800 * (float)imageB.getHeight()/(float)imageB.getWidth())/(float)imageB.getHeight();
    	
    	mImageAWidth = (int)((float)image.getWidth() * mImageAXScale);
    	mImageAHeight = (int)((float)image.getHeight() * mImageAYScale);
    	mImageBWidth = (int)((float)imageB.getWidth() * mImageBXScale);
    	mImageBHeight = (int)((float)imageB.getHeight() * mImageBYScale);
    	
    	// получить совпадающие точки
    	mAMatchingPoints = mSurfA.getMatchingPoints(mSurfB,mUpright);
    	mBMatchingPoints = mSurfB.getMatchingPoints(mSurfA,mUpright);
    }
   // Graphics2D gg;
  //  BufferedImage bi;
    public SurfCompare(BufferedImage image,BufferedImage imageB){
    	
        this.image = image;
    	this.imageB = imageB;
    	mSurfA = new Surf(image);
    	mSurfB = new Surf(imageB);

    	mImageAXScale = (float)Math.min(image.getWidth(),800)/(float)image.getWidth();
    	mImageAYScale = (float)Math.min(image.getHeight(),800 * (float)image.getHeight()/(float)image.getWidth())/(float)image.getHeight();
    	
    	mImageBXScale = (float)Math.min(imageB.getWidth(),800)/(float)imageB.getWidth();
    	mImageBYScale = (float)Math.min(imageB.getHeight(),800 * (float)imageB.getHeight()/(float)imageB.getWidth())/(float)imageB.getHeight();
    	
    	mImageAWidth = (int)((float)image.getWidth() * mImageAXScale);
    	mImageAHeight = (int)((float)image.getHeight() * mImageAYScale);
    	mImageBWidth = (int)((float)imageB.getWidth() * mImageBXScale);
    	mImageBHeight = (int)((float)imageB.getHeight() * mImageBYScale);
    	
    	//  bi = new BufferedImage(mImageAWidth+mImageBWidth,Math.max(mImageAHeight,mImageBHeight), BufferedImage.TYPE_INT_RGB);
     //    gg = bi.createGraphics();
    	// получить совпадающие точки
    	mAMatchingPoints = mSurfA.getMatchingPoints(mSurfB,mUpright);
    	mBMatchingPoints = mSurfB.getMatchingPoints(mSurfA,mUpright);
    }

    /**
     * Drawing an image can allow for more
     * flexibility in processing/editing.
     */
    @Override
    protected void paintComponent(Graphics g) {
        // Center image in this component.
        g.drawImage(image,0,0,mImageAWidth,mImageAHeight,this);
        g.drawImage(imageB,mImageAWidth,0,mImageBWidth,mImageBHeight,Color.WHITE,this);
        
        //if there is a surf descriptor, go ahead and draw the points
        if ( mSurfA != null && mSurfB != null ){
        	drawIpoints(g,mUpright ? mSurfA.getUprightInterestPoints() : mSurfA.getFreeOrientedInterestPoints(),0,mImageAXScale,mImageAYScale);
        	drawIpoints(g,mUpright ? mSurfB.getUprightInterestPoints() : mSurfB.getFreeOrientedInterestPoints(),mImageAWidth,mImageBXScale,mImageBYScale);
		drawConnectingPoints(g);
        }
    }
    
    private void drawIpoints(Graphics g,List<SURFInterestPoint> points,int offset,float xScale,float yScale){
    	Graphics2D g2d = (Graphics2D)g;
    	g2d.setColor(Color.RED);
    	for ( SURFInterestPoint point : points ){
		if ( mAMatchingPoints.containsKey(point) || mBMatchingPoints.containsKey(point) ) continue;
    		int x = (int)(xScale * point.getX()) + offset;
    		int y = (int)(yScale * point.getY());
    		g2d.drawOval(x-UNMATCHED_CIRCLE_DIAMETER/2,y-UNMATCHED_CIRCLE_DIAMETER/2,UNMATCHED_CIRCLE_DIAMETER,UNMATCHED_CIRCLE_DIAMETER);
    	}
    	//g2d.setColor(Color.GREEN);
    	//for ( SURFInterestPoint point : commonPoints ){
    	//	int x = (int)(xScale * point.getX()) + offset;
    	//	int y = (int)(yScale * point.getY());
    	//	g2d.drawOval(x,y,8,8);
    	//}
    }

    private void drawConnectingPoints(Graphics g){
    	Graphics2D g2d = (Graphics2D)g;
    	g2d.setColor(Color.GREEN);
	int offset = mImageAWidth;
    	for ( SURFInterestPoint point : mAMatchingPoints.keySet() ){
    		int x = (int)(mImageAXScale * point.getX());
    		int y = (int)(mImageAYScale * point.getY());
    		g2d.drawOval(x-BASE_CIRCLE_DIAMETER/2,y-BASE_CIRCLE_DIAMETER/2,BASE_CIRCLE_DIAMETER,BASE_CIRCLE_DIAMETER);
		SURFInterestPoint target = mAMatchingPoints.get(point);
		int tx = (int)(mImageBXScale * target.getX()) + offset;
    		int ty = (int)(mImageBYScale * target.getY());
		g2d.drawOval(tx-TARGET_CIRCLE_DIAMETER/2,ty-TARGET_CIRCLE_DIAMETER/2,TARGET_CIRCLE_DIAMETER,TARGET_CIRCLE_DIAMETER);
		g2d.drawLine(x,y,tx,ty);
    	}
    	g2d.setColor(Color.BLUE);
    	for ( SURFInterestPoint point : mBMatchingPoints.keySet() ){
    		int x = (int)(mImageBXScale * point.getX()) + offset;
    		int y = (int)(mImageBYScale * point.getY());
    		g2d.drawOval(x-BASE_CIRCLE_DIAMETER/2,y-BASE_CIRCLE_DIAMETER/2,BASE_CIRCLE_DIAMETER,BASE_CIRCLE_DIAMETER);
		SURFInterestPoint target = mBMatchingPoints.get(point);
		int tx = (int)(mImageAXScale * target.getX());
    		int ty = (int)(mImageAYScale * target.getY());
		g2d.drawOval(tx-TARGET_CIRCLE_DIAMETER/2,ty-TARGET_CIRCLE_DIAMETER/2,TARGET_CIRCLE_DIAMETER,TARGET_CIRCLE_DIAMETER);
		g2d.drawLine(x,y,tx,ty);
    	}
    }

    public void display(){
    	JFrame f= new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(new JScrollPane(this));
        f.setSize(mImageAWidth+mImageBWidth,Math.max(mImageAHeight,mImageBHeight));
        f.setLocation(0,0);
       // f.setVisible(true);
    }
    
    public BufferedImage getBufferedImageOfPanel(){
    	
    		return createImage(this);   
    		 
    }
    public BufferedImage createImage(JPanel panel) {

    	panel.setSize(mImageAWidth+mImageBWidth,Math.max(mImageAHeight,mImageBHeight));
        int w = panel.getWidth();
        int h = panel.getHeight();
        BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bi.createGraphics();
        panel.paint(g);
        return bi;
    }
    public String matchesInfo(){
    //  Map<SURFInterestPoint,SURFInterestPoint> pointsA = mSurfA.getMatchingPoints(mSurfB,true);
  //    Map<SURFInterestPoint,SURFInterestPoint> pointsB = mSurfB.getMatchingPoints(mSurfA,true);
      String s1=("Between carImage and emblemImage there are: " + mAMatchingPoints.size() + " matching points of " + mSurfA.getUprightInterestPoints().size());
      String s2=("Between emblemImage and carImage there are: " + mBMatchingPoints.size() + " matching points of " + mSurfB.getUprightInterestPoints().size());
      return (s1+"\n"+s2);
    }
    
    public float getResultOfMatching(){
    	int total_emblem= mSurfB.getUprightInterestPoints().size();
    	int total_car= mSurfA.getUprightInterestPoints().size();
    	
    	int positive_emblem=mAMatchingPoints.size();
    	int positive_car=mBMatchingPoints.size();
    	
        float min= (total_emblem<total_car)?total_emblem:total_car;
        float max=(positive_emblem>positive_car)?positive_emblem:positive_car;
        
        //System.out.println(min+" "+max+" "+" | "+total_emblem+" "+total_car+"|"+positive_emblem+" "+positive_car);
        
        
        return (positive_emblem+positive_car);
    }
    /*public static void main(String[] args) throws IOException {
        BufferedImage imageA = ImageIO.read(new File("C:\\Emb\\Honda.jpg"));
        BufferedImage imageB = ImageIO.read(new File("C:\\Avto\\Honda.JPG"));
        ImageProcessingImpl ip=new ImageProcessingImpl();
        imageB=ip.trancateAreaFromImage(imageB, 150,150);
     //  imageA = Scalr.resize(imageA, 200);
      // imageB = Scalr.resize(imageB, 200);
        
//        System.out.println(imageA);
//        System.out.println(imageB);
        SurfCompare show = new SurfCompare(imageA,imageB);
        show.display();
        show.matchesInfo();
    }*/
}
