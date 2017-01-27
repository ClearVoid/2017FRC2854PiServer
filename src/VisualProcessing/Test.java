package VisualProcessing;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.*;

import javax.imageio.ImageIO;





public class Test {

	
	public static void main(String[] args) throws IOException {
		
		BufferedImage img = ImageIO.read(new File("Test.png"));
		ImageUtil.showImage(img, "start");
		int[][] data = ImageUtil.grayScale(img);
		Rectangle rect = ImageUtil.calculateDimension(data, 5000000/1);
        Graphics g = img.createGraphics();
        g.setColor(Color.RED);
        g.drawRect(rect.x, rect.y, rect.width, rect.height);
		ImageUtil.showImage(img, "end");
	}
	
}
	