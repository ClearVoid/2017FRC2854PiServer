package VisualProcessing;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

/**
 * 
 * This class deals with changing entire images as a whole
 *
 */
public class ImageTransform {

	/**
	 * Zooms in an image <br>
	 * Parts of image will be lost while zooming in
	 * @param img The image to zoom
	 * @param zoomLevel How much to zoom
	 * @return A new image that is a zoomed version of the image
	 */
	public static BufferedImage zoom(BufferedImage img, int zoomLevel) {
		int newImageWidth = img.getWidth() * zoomLevel;
		int newImageHeight = img.getHeight() * zoomLevel;
		BufferedImage resizedImage = new BufferedImage(newImageWidth,
				newImageHeight, BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(img, 0, 0, newImageWidth, newImageHeight, null);
		g.dispose();
		return resizedImage;
	}
	
	/**
	 * Resizes the image <br>
	 * Keeps the entire original image, unlike zoom
	 * @param img The original image
	 * @param newW The new width of the image
	 * @param newH The new Height of the image
	 * @return A new image with dimensions newW and newH
	 */
	public static BufferedImage resize(BufferedImage img, int newW, int newH) {
		Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
		BufferedImage dimg = new BufferedImage(newW, newH,
				BufferedImage.TYPE_INT_ARGB);

		Graphics2D g2d = dimg.createGraphics();
		g2d.drawImage(tmp, 0, 0, null);
		g2d.dispose();

		return dimg;
	}
	
	/**
	 * Rotates an image
	 * @param img The image
	 * @param degrees How much to rotate the image by in degrees
	 * @return A new image that contains the rotates version of the original image
	 */
	public static BufferedImage rotate(BufferedImage img, float degrees) {
		AffineTransform transform = new AffineTransform();
		transform.rotate(degrees * Math.PI / 180d, img.getWidth() / 2,
				img.getHeight() / 2);
		AffineTransformOp op = new AffineTransformOp(transform,
				AffineTransformOp.TYPE_BILINEAR);
		img = op.filter(img, null);
		return img;
	}
	
	/**
	 * Splits an image alone a line
	 * @param img The image to split
	 * @param p A point to split along
	 * @param vertical Whether to split vertically (true) or horizontally (false)
	 * @return an array of images (0 is left, 1 is right)
	 */
	public static int[][][] splitImage(int[][] img, Point p, boolean vertical) {
		int width = img[0].length;
		int height = img.length;
		int[][] img1;
		int[][] img2;
		if (vertical) {

			img1 = new int[height][(int) p.getX()];
			img2 = new int[height][(int) (width - p.getX())];

			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					if (x < p.getX()) {
						img1[y][x] = img[y][x];
					} else {
						img2[y][(int) (x - p.getX())] = img[y][x];
					}
				}
			}

		} else {

			img1 = new int[(int) p.getY()][width];
			img2 = new int[(int) (height - p.getY())][width];

			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					if (y < p.getY()) {
						img1[y][x] = img[y][x];
					} else {
						img2[(int) (y - p.getY())][x] = img[y][x];
					}
				}
			}

		}

		return new int[][][] { img1, img2 };
	}
	
	
}
