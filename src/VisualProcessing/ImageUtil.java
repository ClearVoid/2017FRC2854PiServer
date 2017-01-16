package VisualProcessing;


import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.List;
import java.awt.Point;
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

	public static BufferedImage resize(BufferedImage img, int newW, int newH) {
		Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
		BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

		Graphics2D g2d = dimg.createGraphics();
		g2d.drawImage(tmp, 0, 0, null);
		g2d.dispose();

		return dimg;
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

	public static BufferedImage f2b(int[][] data) {
		int height = data[0].length;
		int width = data.length;

		BufferedImage out = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				// System.out.println((int) (data[x][y]/3f));
				int pixel =  (data[x][y]);
				pixel = pixel > 255 ? 255 : pixel;
				out.setRGB(x, y, (new Color(pixel, pixel, pixel)).getRGB());
			}
		}
		return out;
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

}