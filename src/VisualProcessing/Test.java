package VisualProcessing;

import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import javax.imageio.ImageIO;

import org.marvinproject.image.segmentation.floodfillSegmentation.FloodfillSegmentation;

import com.github.sarxos.webcam.Webcam;

import marvin.image.MarvinImage;
import marvin.image.MarvinSegment;
import marvin.util.MarvinAttributes;
import marvin.image.*;




public class Test {

	public static void main(String[] args) throws IOException {
		
		BufferedImage image = ImageIO.read(new File("redBox.png"));
		ImageUtil.showImage(image, "Camera");
        HoughTransform h = new HoughTransform(image.getWidth(), image.getHeight()); 
        
        // add the points from the image (or call the addPoint method separately if your points are not in an image 
        h.addPoints(image); 
 
        // get the lines out 
        Vector<HoughLine> lines = h.getLines(30); 
 
        // draw the lines back onto the image 
        for (int j = 0; j < lines.size(); j++) { 
            HoughLine line = lines.elementAt(j); 
            line.draw(image, Color.RED.getRGB()); 
        } 
		
        ImageUtil.showImage(image, "Transform");
        ImageUtil.showImage(h.getHoughArrayImage(), "Lines");
		
		
        
	}
	
}
