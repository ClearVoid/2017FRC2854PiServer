package VisualProcessing;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;//10.6.2  RanSSC

import com.github.sarxos.webcam.Webcam;

public class ImageMeanAndStDev {

	public static void main(String[] args) {
		
		String path = "tape1.png";
		boolean takeNewPic = false;
		try {
		File file = new File(path);
		BufferedImage img;
		if(takeNewPic) {
			Webcam cam = Webcam.getDefault();
			cam.open();
			img = cam.getImage();
		} else {
			img = ImageIO.read(file);
		}
		File f = new File("cameraPic.png");
		if(!f.exists()) {
			f.createNewFile();
		}
		ImageIO.write(img, "png", f);
		double hueAvg = 0;
		int total = 0;
		for(int x = 0; x < img.getWidth(); x++) {
			for(int y = 0; y < img.getHeight(); y++) {
				
				int rgb = img.getRGB(x, y);
				int red = (rgb >> 16) & 0x000000FF;
				int green = (rgb >>8 ) & 0x000000FF;
				int blue = (rgb) & 0x000000FF;
				
				float[] hsv = new float[3];
				Color.RGBtoHSB(red, green, blue, hsv);
				hueAvg += hsv[0]*360;
				total++;
			}
		}
		
		hueAvg /= total;
		
		total = 0;
		
		double hueStDev = 0;
		
		for(int x = 0; x < img.getWidth(); x++) {
			for(int y = 0; y < img.getHeight(); y++) {
				
				int rgb = img.getRGB(x, y);
				double red = (rgb >> 16) & 0x000000FF;
				double green = (rgb >>8 ) & 0x000000FF;
				double blue = (rgb) & 0x000000FF;
				
				hueStDev += Math.pow(red - hueAvg, 2);
				
				total++;
			}
		}
		
		hueStDev /= total;
		
		
		hueStDev = Math.round(Math.sqrt(hueStDev));

		
		System.out.println("Mean: " + hueAvg);
		System.out.println("StDev: " + hueStDev);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
