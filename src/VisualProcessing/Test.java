package VisualProcessing;

import java.awt.image.BufferedImage;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;

public class Test {

	public static void main(String[] args) {

		try {
			BufferedImage img = ImageIO.read(new File("Test.png"));

			int[][] data = ImageUtil.grayScale(img);

			System.out.println(Arrays.toString(ImageUtil.getDimensions(data)));

		} catch (IOException e) {
			
			e.printStackTrace();
		}

	}

}
