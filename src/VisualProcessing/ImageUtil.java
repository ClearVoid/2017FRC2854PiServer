package VisualProcessing;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.List;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import javax.swing.*;

public class ImageUtil {

	public static int[][] grayScale(BufferedImage img) {
		return grayScale(img, 4);
	}

	public static BufferedImage zoom(BufferedImage img, int zoomLevel) {
		int newImageWidth = img.getWidth() * zoomLevel;
		int newImageHeight = img.getHeight() * zoomLevel;
		BufferedImage resizedImage = new BufferedImage(newImageWidth, newImageHeight, BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(img, 0, 0, newImageWidth, newImageHeight, null);
		g.dispose();
		return resizedImage;
	}
	
	public static void drawLargePixel(BufferedImage img, int x, int y, int rgb) {
		try {
		img.setRGB(x, y, rgb);
		img.setRGB(x+1, y, rgb);
		img.setRGB(x-1, y, rgb);
		img.setRGB(x, y+1, rgb);
		img.setRGB(x, y-1, rgb);
		} catch (ArrayIndexOutOfBoundsException e) {
			return;
		}
	}

	public static BufferedImage resize(BufferedImage img, int newW, int newH) {
		Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
		BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

		Graphics2D g2d = dimg.createGraphics();
		g2d.drawImage(tmp, 0, 0, null);
		g2d.dispose();

		return dimg;
	}
	
	
	public static BufferedImage colorCut(BufferedImage img, float[] averages, float[] standardDevations, float Standarderror) {
		BufferedImage out = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
		for(int x = 0; x < img.getWidth(); x++) {
			for(int y = 0; y < img.getHeight(); y++) {
				int rgb = img.getRGB(x, y);
				float red = (rgb >> 16) & 0x000000FF;
				float green = (rgb >>8 ) & 0x000000FF;
				float blue = (rgb) & 0x000000FF;
				
				float zR = (red - averages[0])/standardDevations[0];
				float zB = (blue - averages[1])/standardDevations[1];
				float zG = (green - averages[2])/standardDevations[2];
				
				if(Math.abs(zR) <= Standarderror && Math.abs(zB) <= Standarderror && Math.abs(zG) <= Standarderror) {
					out.setRGB(x, y, rgb);
				} else {
					out.setRGB(x, y, (new Color(255, 255, 255).getRGB()));
				}
				
			}
		}
		return out;
		
	}

	public static int[][] grayScale(BufferedImage img, int type) {
		int[][] out = new int[img.getHeight()][img.getWidth()];
		for (int x = 0; x < img.getWidth(); x++) {
			for (int y = 0; y < img.getHeight(); y++) {

				Color c = new Color(img.getRGB(x, y));
				float avg;
				switch (type) {
				case 1:
					avg = c.getRed();
					break;
				case 2:
					avg = c.getBlue();
					break;
				case 3:
					avg = c.getGreen();
					break;
				default:
					avg = (c.getRed() + c.getGreen() + c.getBlue()) / 3f;
				}

				//System.out.println("x: " + x + " imgWidth: " + img.getWidth() + " imgHeight: " + img.getHeight() + " arrayWidth: " + out[0].length + " arrayHeight: " + out.length);
				out[y][x] = (int) avg;
			}
		}
		return out;
	}
	
	public static void showImage(BufferedImage... imgs) {
		for(BufferedImage img:imgs) {
			showImage(img);
		}
	}

	public static void showImage(BufferedImage img) {
		showImage(img, "");
	}

	public static void showImage(BufferedImage img, String title) {
		JFrame frame = new JFrame();
		frame.add(new JLabel(new ImageIcon(img)));
		frame.setTitle(title);
		frame.pack();
		frame.setVisible(true);
	}

	public static int averageColor(int r, int g, int b) {
		return (r + g + b) / 3;
	}

	public static int averageColor(int RGB) {
		Color c = new Color(RGB);
		return averageColor(c.getRed(), c.getGreen(), c.getBlue());
	}

	public static void edgeTrace(float[][][] edgeData, float threshHold) {
		float[][] mag = edgeData[0];
		float[][] dir = edgeData[1];

		System.out.println(Arrays.toString(dir));

		int width = mag[0].length;
		int height = mag.length;

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				float angle = dir[x][y];
				float dx = (float) Math.cos(angle);
				dx = (-.5f < dx && dx < .5f) ? 0 : 1;
				float dy = (float) Math.sin(angle);
				dy = (-.5f < dy && dy < .5f) ? 0 : 1;
				float nextMag;
				try {
					nextMag = mag[(int) dx][(int) dy];
				} catch (IndexOutOfBoundsException e) {
					continue;
				}
				if (Math.abs(nextMag - mag[x][y]) <= threshHold) {
					mag[(int) dx][(int) dy] = mag[x][y];
				}

			}
		}

		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int magni = (int) (mag[x][y] / 3);
				img.setRGB(x, y, (new Color(magni, magni, magni).getRGB()));
			}
		}

		showImage(img);

	}

	public static BufferedImage arrayToImg(int[][] data) {
		int width = data[0].length;
		int height = data.length;
		
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
		
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				int pixel = data[y][x];
				img.setRGB(x, y, (new Color(pixel, pixel, pixel).getRGB()));
				
			}
		}
		
		return img;
		
	}
	

	public static int[][] fillClosed(int[][] img) {
		return fillClosed(img, 0);
	}
	
	public static int[][] fillClosed(int[][] img, int depth) {

		int width = img[0].length;
		int height = img.length;
		
		int[][] out = new int[height][width];
		
		boolean fill = false;
		boolean gap = false;
		boolean edge = false;
		
		for(int y = 0; y < height;y++) {
			fill = false;
			edge = false;
			gap = false;
			for(int x = 0; x < width ; x++) {
				if(fill && img[y][x] == 255) {
					gap = true;
				}
				if(img[y][x] == 255 && edge){
					fill = false;
					edge = false;
					gap = false;
				}
				if(img[y][x] == 0) {
					if(gap) {
						edge = true;
					}  else if(!fill) {
						fill = true;
					} 
				}
				if(y == 33) {
					//System.out.println(new Point(x,y).toString() + " Gap: " + gap + " Fill: " + fill + " Edge: " + edge);
				}
				out[y][x] = fill ? 1 : 255;
			}
		}
		
		for(int x = 0; x < width ; x++) {
			fill = false;
			edge = false;
			gap = false;
			for(int y = 0; y < height;y++) {

				if(fill && img[y][x] == 255) {
					gap = true;
				} 
				if(img[y][x] == 255 && edge){
					fill = false;
					edge = false;
					gap = false;
				}
				if(img[y][x] == 0) {
					if(gap) {
						edge = true;
					}  else if(!fill) {
						fill = true;
					} 
				}
				if(y == 34) {
					//	System.out.println(new Point(x,y).toString() + " Gap: " + gap + " Fill: " + fill);
				}
				

				out[y][x] = (fill && out[y][x] == 1) ? 0 : 255;
				
			}
		}
		

		if(Arrays.deepHashCode(img) != Arrays.deepHashCode(out)) {	
			//ImageUtil.showImage(ImageUtil.arrayToImg(out), ++depth + "");
			return fillClosed(out, depth);
		} else {
			return out;
		}
	}
	

	public static int[][] singleBandEdgeDetection(int[][] img) {

		int width = img[0].length;
		int height = img.length;

		int[][] output = new int[height - 2][width - 2];

		int[][] mX = new int[][] { { -1, 0, 1 }, { -2, 0, 2 }, { -1, 0, 1 } };

		int[][] mY = new int[][] { { 1, 2, 1 }, { 0, 0, 0 }, { -1, -2, -1 } };

		for (int x = 1; x < width - 1; x++) {
			for (int y = 1; y < height - 1; y++) {

				int gradX = 0, gradY = 0;
				
				gradX += img[y - 1][x - 1] * mX[0][0];
				gradX += img[y - 1][x + 0] * mX[0][1];
				gradX += img[y - 1][x + 1] * mX[0][2];
				gradX += img[y + 0][x - 1] * mX[1][0];
				gradX += img[y + 0][x + 0] * mX[1][1];
				gradX += img[y + 0][x + 1] * mX[1][2];
				gradX += img[y + 1][x - 1] * mX[2][0];
				gradX += img[y + 1][x + 0] * mX[2][1];
				gradX += img[y + 1][x + 1] * mX[2][2];
				
				gradY += img[y - 1][x - 1] * mY[0][0];
				gradY += img[y - 1][x + 0] * mY[0][1];
				gradY += img[y - 1][x + 1] * mY[0][2];
				gradY += img[y + 0][x - 1] * mY[1][0];
				gradY += img[y + 0][x + 0] * mY[1][1];
				gradY += img[y + 0][x + 1] * mY[1][2];
				gradY += img[y + 1][x - 1] * mY[2][0];
				gradY += img[y + 1][x + 0] * mY[2][1];
				gradY += img[y + 1][x + 1] * mY[2][2];

				int mag = Math.abs(gradX) + Math.abs(gradY);

				// mag = mag < 0 ? 0 : (mag > 255 ? 255 : mag);
				// mag = 255 - mag;
				// System.out.println(mag);
				output[y-1][x-1] = mag;

			}
		}
		return output;
	}

	public static BufferedImage rotate(BufferedImage img, float degrees) {
		AffineTransform transform = new AffineTransform();
		transform.rotate(degrees * Math.PI / 180d, img.getWidth() / 2, img.getHeight() / 2);
		AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
		img = op.filter(img, null);
		return img;
	}

	public static BufferedImage GaussianFilter(BufferedImage img, float dc_level, float cutoff) {

		int value = 0;
		double distance = 0;
		int sizex = img.getWidth();
		int sizey = img.getHeight();

		BufferedImage out = new BufferedImage(sizex, sizey, BufferedImage.TYPE_3BYTE_BGR);

		int xcenter = (sizex / 2) + 1;
		int ycenter = (sizey / 2) + 1;
		for (int y = 0; y < sizey; y++) {
			for (int x = 0; x < sizex; x++) {
				distance = Math.abs(x - xcenter) * Math.abs(x - xcenter)
						+ Math.abs(y - ycenter) * Math.abs(y - ycenter);
				distance = Math.sqrt(distance);
				value = (int) (dc_level * Math.exp((-1 * distance * distance) / (1.442695 * cutoff * cutoff)));
				out.setRGB(xcenter, y, new Color(value, value, value).getRGB());

			}
		}

		return out;
	}

	public static int[][] invert(int[][] img) {

		int width = img[0].length;
		int height = img.length;

		int[][] out = new int[height][width];

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int pixel = img[y][x];
				if (pixel == 0) {
					pixel = 255;
				} else if (pixel == 255) {
					pixel = 0;
				} else {
					throw new RuntimeException("Must be binary image");
				}
				out[y][x] = pixel;
			}
		}
		return out;
	}

	public static boolean equals(BufferedImage img1, BufferedImage img2) {
		if (img1.getWidth() != img2.getWidth() || img1.getHeight() != img2.getHeight()) {
			return false;
		}
		for (int x = 0; x < img1.getWidth(); x++) {
			for (int y = 0; y < img1.getHeight(); y++) {
				if (img1.getRGB(x, y) != img2.getRGB(x, y)) {
					return false;
				}
			}
		}
		return true;
	}

	public static BufferedImage copy(BufferedImage img) {
		BufferedImage out = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
		int width = img.getWidth();
		int height = img.getHeight();
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				out.setRGB(x, y, img.getRGB(x, y));
			}
		}
		return out;
	}

	private static int[][] imgToBinaryImgArr(BufferedImage img) {
		int[][] out = new int[img.getWidth()][img.getHeight()];
		for (int x = 0; x < img.getWidth(); x++) {
			for (int y = 0; y < img.getHeight(); y++) {
				out[x][y] = (img.getRGB(x, y) & 0xff) == 0 ? 0 : 1;
			}
		}
		return out;
	}

	private static BufferedImage arrToImg(int[][] data) {

		int width = data[0].length;
		int height = data.length;

		BufferedImage out = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				// System.out.println(data[y][x]);
				int pixel = data[y][x] * 255;
				// System.out.println(pixel);
				out.setRGB(x, y, new Color(pixel, pixel, pixel).getRGB());
			}
		}
		return out;
	}

	public static BufferedImage zhengThinning(BufferedImage img) {

		int[][] givenImage = imgToBinaryImgArr(img);

		int[][] binaryImage;

		binaryImage = givenImage.clone();

		int a, b;
		List<Point> pointsToChange = new LinkedList<Point>();
		boolean hasChange;
		do {
			hasChange = false;
			for (int y = 1; y + 1 < binaryImage.length; y++) {
				for (int x = 1; x + 1 < binaryImage[y].length; x++) {
					a = getA(binaryImage, y, x);
					b = getB(binaryImage, y, x);
					if (binaryImage[y][x] == 1 && 2 <= b && b <= 6 && a == 1
							&& (binaryImage[y - 1][x] * binaryImage[y][x + 1] * binaryImage[y + 1][x] == 0)
							&& (binaryImage[y][x + 1] * binaryImage[y + 1][x] * binaryImage[y][x - 1] == 0)) {
						pointsToChange.add(new Point(x, y));
						// binaryImage[y][x] = 0;
						hasChange = true;
					}
				}
			}
			for (Point point : pointsToChange) {
				binaryImage[(int) point.getY()][(int) point.getX()] = 0;
			}
			pointsToChange.clear();
			for (int y = 1; y + 1 < binaryImage.length; y++) {
				for (int x = 1; x + 1 < binaryImage[y].length; x++) {
					a = getA(binaryImage, y, x);
					b = getB(binaryImage, y, x);
					if (binaryImage[y][x] == 1 && 2 <= b && b <= 6 && a == 1
							&& (binaryImage[y - 1][x] * binaryImage[y][x + 1] * binaryImage[y][x - 1] == 0)
							&& (binaryImage[y - 1][x] * binaryImage[y + 1][x] * binaryImage[y][x - 1] == 0)) {
						pointsToChange.add(new Point(x, y));
						hasChange = true;
					}
				}
			}
			for (Point point : pointsToChange) {
				binaryImage[(int) point.getY()][(int) point.getX()] = 0;
			}
			pointsToChange.clear();
		} while (hasChange);
		return rotate(arrToImg(binaryImage), 90);
	}

	private static int getA(int[][] binaryImage, int y, int x) {
		int count = 0;
		// p2 p3
		if (y - 1 >= 0 && x + 1 < binaryImage[y].length && binaryImage[y - 1][x] == 0
				&& binaryImage[y - 1][x + 1] == 1) {
			count++;
		}
		// p3 p4
		if (y - 1 >= 0 && x + 1 < binaryImage[y].length && binaryImage[y - 1][x + 1] == 0
				&& binaryImage[y][x + 1] == 1) {
			count++;
		}
		// p4 p5
		if (y + 1 < binaryImage.length && x + 1 < binaryImage[y].length && binaryImage[y][x + 1] == 0
				&& binaryImage[y + 1][x + 1] == 1) {
			count++;
		}
		// p5 p6
		if (y + 1 < binaryImage.length && x + 1 < binaryImage[y].length && binaryImage[y + 1][x + 1] == 0
				&& binaryImage[y + 1][x] == 1) {
			count++;
		}
		// p6 p7
		if (y + 1 < binaryImage.length && x - 1 >= 0 && binaryImage[y + 1][x] == 0 && binaryImage[y + 1][x - 1] == 1) {
			count++;
		}
		// p7 p8
		if (y + 1 < binaryImage.length && x - 1 >= 0 && binaryImage[y + 1][x - 1] == 0 && binaryImage[y][x - 1] == 1) {
			count++;
		}
		// p8 p9
		if (y - 1 >= 0 && x - 1 >= 0 && binaryImage[y][x - 1] == 0 && binaryImage[y - 1][x - 1] == 1) {
			count++;
		}
		// p9 p2
		if (y - 1 >= 0 && x - 1 >= 0 && binaryImage[y - 1][x - 1] == 0 && binaryImage[y - 1][x] == 1) {
			count++;
		}
		return count;
	}

	private static int getB(int[][] binaryImage, int y, int x) {
		return binaryImage[y - 1][x] + binaryImage[y - 1][x + 1] + binaryImage[y][x + 1] + binaryImage[y + 1][x + 1]
				+ binaryImage[y + 1][x] + binaryImage[y + 1][x - 1] + binaryImage[y][x - 1] + binaryImage[y - 1][x - 1];
	}

	public static int[][] threshHold(int[][] img, int threshHold) {
		int width = img[0].length;
		int height = img.length;
		int[][] out = new int[height][width];
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				out[y][x] = img[y][x] > threshHold ? 255 : 0;
			}
		}
		return out;
	}

	private static int[][] lowPassX(int[][] img, float smoothing) {
		int value = img[0][0];
		int width = img[0].length;
		int height = img.length;
		int[][] out = new int[height][width];
		
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
			
				int currentValue = img[y][x];
				value += (currentValue - value) / smoothing;
				value = value < 0 ? 0 : value;
				value = value > 255 ? 255 : value;
				out[y][x] = value;

			}
		}
		return out;
	}
	
	private static int[][] lowPassY(int[][] img, float smoothing) {
		int value = img[0][0];
		int width = img[0].length;
		int height = img.length;
		int[][] out = new int[height][width];
		
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
			
				int currentValue = img[y][x];
				value += (currentValue - value) / smoothing;
				value = value < 0 ? 0 : value;
				value = value > 255 ? 255 : value;
				out[y][x] = value;

			}
		}
		return out;
	}

	public static int[][] lowPass(int[][] img, float smoothing) {

		int[][] xImg = lowPassX(img, smoothing);
		int[][] yImg = lowPassY(img, smoothing);

		int width = img[0].length;
		int height = img.length;
		int[][] out = new int[height][width];

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int xPixel = xImg[y][x];
				int yPixel = yImg[y][x];
				out[y][x] = (xPixel < yPixel) ? xPixel : yPixel;

			}
		}
		return out;

	}
	

	public static Point findCenter(int[][] img) {
		
		int xAvg = 0;
		int yAvg = 0;
		int width = img[0].length;
		int height = img.length;
		int total = 0;
		for(int x = 0; x < width ; x++) {
			for(int y = 0; y < height; y++) {
				
				if(img[y][x] == 0) {
					xAvg += x;
					yAvg += y;
					total++;
				}
				
			}
		}
		
		if(total == 0) {
			return null;
		}
		xAvg /= total;
		yAvg /= total;
		
		return new Point(xAvg, yAvg);
	}
	
	public static int[][][] splitImage(int[][] img, Point p, boolean vertical) {
		int width = img[0].length;
		int height = img.length;
		int[][] img1;
		int[][] img2;
		if(vertical) {
			
			img1 = new int[height][(int) p.getX()];
			img2 = new int[height][(int) (width - p.getX())];
			
			for(int x = 0; x < width; x++) {
				for(int y = 0; y < height; y++) {
					if( x < p.getX()) {
						img1[y][x] = img[y][x];
					} else {
						img2[y][(int) (x - p.getX())] = img[y][x];
					} 
				}
			}
			
		} else {
			

			img1 = new int[(int) p.getY()][width];
			img2 = new int[(int) (height - p.getY())][width];
			
			for(int x = 0; x < width; x++) {
				for(int y = 0; y < height; y++) {
					if( y < p.getY()) {
						img1[y][x] = img[y][x];
					} else {
						img2[(int) (y - p.getY())][x] = img[y][x];
					} 
				}
			}
			
		}
		
		return new int[][][] {img1, img2};
	}
	
	public static int[] findImageMaxX(int[][] img, Point center, boolean right) {
		
		return findImageMaxX(img, center, 0, right, false, new int[2]);
	}
	
	private static int[] findImageMaxX(int[][] img, Point center, int width, boolean right, boolean found, int[] data) {
		try {
		if(img[center.y][center.x + width] == 255 && !found){
			data[0] = right ? width : -width;
			return findImageMaxX(img, center, width+(right?1:-1),right,  false, data);
		} else if(img[center.y][center.x + width] == 0) {
			return findImageMaxX(img, center, width+(right?1:-1), right, true, data);
		} else {
			data[1] = right ? width : -width;
			return data;
		}
		} catch (ArrayIndexOutOfBoundsException e) {
			return new int[] {-1, -1, -1};
		}
		
	}
	
	/**
	 * 
	 * @return {width, leftHeight, rightHeight}
	 */
	public static int[] getDimensions(int[][] img) {
		Point p = ImageUtil.findCenter(img);
		//System.out.println(p);
		if(p == null) {
			return new int[] {-1, -1, -1};
		}
		// int[][][] split = ImageUtil.splitImage(data, p, false);
		int[] width1 = ImageUtil.findImageMaxX(img, p, true);
		int[] width2 = ImageUtil.findImageMaxX(img, p, false);
		int avgWidth1 = (width1[0] + width1[1])/2;		
		int avgWidth2 = (width2[0] + width2[1])/2;
		int width = (avgWidth1 + avgWidth2);

		int rightHeight =  ImageUtil.findImageMaxY(img, new Point(p.x + avgWidth1, p.y), true)
				+ ImageUtil.findImageMaxY(img, new Point(p.x + avgWidth1, p.y), false);
		
		int leftHeight = ImageUtil.findImageMaxY(img, new Point(p.x - avgWidth2, p.y), true)
				+ ImageUtil.findImageMaxY(img, new Point(p.x - avgWidth2, p.y), false);
		return new int[] {width, leftHeight, rightHeight};
	}
	
	public static int findImageMaxY(int[][] img, Point center, boolean up) {
		return findImageMaxY(img, center, 0, up);
	}
	
	private static int findImageMaxY(int[][] img, Point center, int height, boolean up) {
		
		try {
		if(img[center.y + height][center.x] == 0 || img[center.y + height][center.x+1] == 0 || img[center.y + height][center.x-1] == 0) {
			return findImageMaxY(img, center, height+ (up?-1:1), up);
		} else {
			return up?-height:height;
		}
		} catch (ArrayIndexOutOfBoundsException e) {
			return -1;
		}
	}
	
	public static double getDistance(int screenWidth, int projectedWidth, double actualWidth, double tanAngle) {
		return screenWidth * actualWidth / (projectedWidth * tanAngle);
	}
	
	
	public static double blackPerArea(int[][] img, Rectangle rect) {
		int blacks = 0;
		for(int x = (int) rect.getX(); x < rect.getX() + rect.getWidth(); x++) {
			for(int y = (int) rect.getY(); y < rect.getY() + rect.getHeight(); y++) {
				if(img[y][x] == 0) {
					blacks++;
				}
			}
		}
		return (double)blacks/(rect.getWidth()*rect.getHeight());
	}
	
	public static void bruteForce(int[][] img, Rectangle rect, int dierction) {
		
		double lastProp = blackPerArea(img, rect);
		
	}
	
	public static Dimension calculateDimension(int[][] img) {
		Point p = ImageUtil.findCenter(img);
		int width = img[0].length;
		int height = img.length;
		
		int yUp = 1;
		int yDown = 1;
		int xLeft = 1;
		int xRight = 1;
		
		
		return null;
	}
	
	

}