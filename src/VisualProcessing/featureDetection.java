package VisualProcessing;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * 
 * This class deals with analyzing images to detect features
 */
public class featureDetection {

	/**
	 * An edge detection using a 3x3 kernel <br>
	 * Implementation of the sobel edge detection
	 * 
	 * @param img
	 *            The image the detect
	 * @return An image that contains the magnitudes of the gradient from the
	 *         detection
	 */
	public static int[][] edgeDetection(int[][] img) {

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
				output[y - 1][x - 1] = mag;

			}
		}
		return output;
	}

	/**
	 * Removes colors from an image according to the desired averages and
	 * standard deviations <br>
	 * 
	 * @param img
	 *            The image
	 * @param averages
	 *            The average Color values, in the form {red, blue green}
	 * @param standardDevations
	 *            The standard deviations of the color values in the form {red,
	 *            blue, green}
	 * @param Standarderror
	 *            The standard error, or how many deviations at max a given
	 *            value can be from the mean to be considered a point
	 * @return The image with the colors removed
	 */
	public static BufferedImage colorCut(BufferedImage img, float[] averages, float[] standardDevations,
			float Standarderror) {
		BufferedImage out = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
		for (int x = 0; x < img.getWidth(); x++) {
			for (int y = 0; y < img.getHeight(); y++) {
				int rgb = img.getRGB(x, y);
				float red = (rgb >> 16) & 0x000000FF;
				float green = (rgb >> 8) & 0x000000FF;
				float blue = (rgb) & 0x000000FF;

				float zR = (red - averages[0]) / standardDevations[0];
				float zB = (blue - averages[1]) / standardDevations[1];
				float zG = (green - averages[2]) / standardDevations[2];

				if (Math.abs(zR) <= Standarderror && Math.abs(zB) <= Standarderror && Math.abs(zG) <= Standarderror) {
					out.setRGB(x, y, rgb);
				} else {
					out.setRGB(x, y, (new Color(0, 0, 0).getRGB()));
				}

			}
		}
		return out;

	}
	/**
	 * Thins an image using the zheng thinning algorithm
	 * @param img The image
	 * @return The thinned image
	 */
	public static BufferedImage zhengThinning(BufferedImage img) {

		int[][] givenImage = ImageUtil.imgToBinaryImgArr(img);

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
		return ImageTransform.rotate(ImageUtil.arrToImg(binaryImage), 90);
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

	/**
	 * fills in closed regions of an image <br>
	 * There should not be single pixels present in the image
	 * @param img
	 * @return
	 */
	public static int[][] fillClosed(int[][] img) {
		return fillClosed(img, 0);
	}

	private static int[][] fillClosed(int[][] img, int depth) {

		int width = img[0].length;
		int height = img.length;

		int[][] out = new int[height][width];

		boolean fill = false;
		boolean gap = false;
		boolean edge = false;

		for (int y = 0; y < height; y++) {
			fill = false;
			edge = false;
			gap = false;
			for (int x = 0; x < width; x++) {
				if (fill && img[y][x] == 255) {
					gap = true;
				}
				if (img[y][x] == 255 && edge) {
					fill = false;
					edge = false;
					gap = false;
				}
				if (img[y][x] == 0) {
					if (gap) {
						edge = true;
					} else if (!fill) {
						fill = true;
					}
				}
				if (y == 33) {
					// System.out.println(new Point(x,y).toString() + " Gap: " +
					// gap + " Fill: " + fill + " Edge: " + edge);
				}
				out[y][x] = fill ? 1 : 255;
			}
		}

		for (int x = 0; x < width; x++) {
			fill = false;
			edge = false;
			gap = false;
			for (int y = 0; y < height; y++) {

				if (fill && img[y][x] == 255) {
					gap = true;
				}
				if (img[y][x] == 255 && edge) {
					fill = false;
					edge = false;
					gap = false;
				}
				if (img[y][x] == 0) {
					if (gap) {
						edge = true;
					} else if (!fill) {
						fill = true;
					}
				}
				if (y == 34) {
					// System.out.println(new Point(x,y).toString() + " Gap: " +
					// gap + " Fill: " + fill);
				}

				out[y][x] = (fill && out[y][x] == 1) ? 0 : 255;

			}
		}

		if (Arrays.deepHashCode(img) != Arrays.deepHashCode(out)) {
			// ImageUtil.showImage(ImageUtil.arrayToImg(out), ++depth + "");
			return fillClosed(out, depth);
		} else {
			return out;
		}
	}

}
