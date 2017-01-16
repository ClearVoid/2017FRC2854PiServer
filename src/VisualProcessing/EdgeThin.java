package VisualProcessing;

import java.awt.Point;
import java.util.ArrayList;

public class EdgeThin {
	private static final int[][] structEle = { { 0, 0, 0 }, { 2, 255, 2 },
			{ 255, 255, 255 } };
	private static final int[][] structEle1 = { { 2, 0, 0 }, { 255, 255, 0 },
			{ 2, 255, 2 } };

	private static int[][][] aStructs = new int[8][3][3];

	public static void init() {
		System.out.println("Initializing EdgeThin");

		for (int i = 0; i < 4; i++) {
			aStructs[i * 2 + 0] = rotate(structEle, i);
			aStructs[i * 2 + 1] = rotate(structEle1, i);
		}

	}

	public static int[][] thin(int[][] img) {
		ArrayList<Point> delete = new ArrayList<Point>();
		int width = img[0].length;
		int height = img.length;
		int[][] out = new int[height - 2][width - 2];

		for (int i = 1; i < img.length - 1; i++) {
			System.arraycopy(img[i], 0, out[i-1], 0, width - 2);
		}

		boolean hasChanged = false;

		for (int[][] kernel : aStructs) {
			for (int x = 1; x < width - 1; x++) {
				for (int y = 1; y < height - 1; y++) {

					int[][] pixels = new int[][] {
							{ img[y - 1][x - 1], img[y - 1][x],
									img[y - 1][x + 1] },
							{ img[y][x - 1], img[y][x], img[y][x + 1] },
							{ img[y + 1][x - 1], img[y + 1][x],
									img[y + 1][x + 1] } };

					boolean shouldDelete = true;
					for (int i = 0; i < 3; i++) {
						for (int j = 0; j < 3; j++) {
							if (kernel[j][i] == 2 || !shouldDelete) {
								continue;
							}
							if (kernel[j][i] == pixels[j][i]) {
								shouldDelete = false;
							}
						}
					}
					if (shouldDelete) {
						delete.add(new Point(x, y));
					}
				}
			}
			for (Point p : delete) {
				hasChanged = true;
				out[p.y - 1][p.x - 1] = 0;
			}
			delete.clear();
		}

		if (!hasChanged) {
			return out;
		} else {
			return thin(out);
		}

	}

	public static int[][] rotate(int[][] array, int n) {

		int[][] out = new int[3][3];
		for (int i = 0; i < array.length; i++) {
			System.arraycopy(array[i], 0, out[i], 0, 3);
		}

		for (int i = 0; i < n; i++) {
			out = rotate3X3(out);
		}

		return out;
	}

	public static int[][] rotate3X3(int[][] a) {

		int[][] out = new int[3][3];

		int temp = a[0][0];
		a[0][0] = a[0][2];
		a[0][2] = a[2][2];
		a[2][2] = a[2][0];
		a[2][0] = temp;

		temp = a[0][1];
		a[0][1] = a[1][2];
		a[1][2] = a[2][1];
		a[2][1] = a[1][0];
		a[1][0] = temp;

		return out;
	}
}
