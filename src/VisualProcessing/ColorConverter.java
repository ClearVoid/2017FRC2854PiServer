package VisualProcessing;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ColorConverter {

	
	public static void main(String[] args) throws IOException {
		String path = "redBox.png";
		File f = new File(path);
		if(!f.exists()) {
			System.err.println("File at " + f.getAbsolutePath() + " does not exist");
			System.exit(1);
		}
		
		BufferedImage img = ImageIO.read(f);
		double hueAvg = 0;
		int counter = 0;
		for(int x = 0; x < img.getWidth(); x++) {
			for(int y = 0; y < img.getHeight(); y++) {
				hueAvg += getHue(img.getRGB(x, y));
				counter++;
			}
		}
		hueAvg /= counter;
		
		double hueDev = 0;
		counter = 0;
		for(int x = 0; x < img.getWidth(); x++) {
			for(int y = 0; y < img.getHeight(); y++) {
				hueAvg += Math.pow(getHue(img.getRGB(x, y)) - hueAvg, 2);
				counter++;
			}
		}
		hueDev /= (counter);
		hueDev = Math.sqrt(hueDev);
		System.out.println("Mean: " + hueAvg + " stDev: " + hueDev);
		
	}
	
	
	public static double getHue(int RGB) {
		Color c = new Color(RGB);
		float rP = c.getRed()/255f;
		float gP = c.getGreen()/255f;
		float bP = c.getBlue()/255f;
		float max = Math.max(rP, Math.max(gP, bP));
		float min = Math.min(rP, Math.min(gP, bP));
		float d = max - min;
		if(d == 0) {
			return 0;
		} else if(max == rP) {
			return 60 * (((gP - bP)/d)%6);
		} else if(max == gP) {
			return 60 * (((bP - rP)/d)+2);
		} else  {
			return 60 * (((rP - gP)/d)+4);
		}
	} 
	
	
}
