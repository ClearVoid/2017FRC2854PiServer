package VisualProcessing;

import java.awt.image.BufferedImage;
import java.io.*;

import javax.imageio.ImageIO;





public class Test {

	public static void main(String[] args) throws IOException {
		
		BufferedImage img = ImageIO.read(new File("Test.png"));
		
		int[][] data = ImageUtil.grayScale(img);
		data = ImageUtil.fillClosed(data);
		BufferedImage img2 = ImageUtil.arrayToImg(data);
		ImageUtil.showImage(img, img2);
        
	}
	
}
	