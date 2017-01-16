package VisualProcessing;


import java.awt.Color;
import java.util.List;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public class EdgeThin {
	private static final float[][] structEle = { { 0, 0, 0 }, { 2, 255, 2 }, { 255, 255, 255 } };

	private static float[][] structEle1 = { { 2, 0, 0 }, { 255, 255, 0 }, { 2, 255, 2 } };

	private static Matrix[] aStructs = new Matrix[8];

	private static void init(){
		Matrix struct = new Matrix(structEle);
		Matrix struct1 = new Matrix(structEle1);
		for (int i = 0; i < 4; i++) {aStructs[i] = Matrix.rotateMatrixN(struct, i);}
		for (int i = 0; i < 4; i++) {aStructs[i + 4] = Matrix.rotateMatrixN(struct1, i);}
		
	}

	public static BufferedImage thin(BufferedImage img, int iterations) {
		init();
		ArrayList<Point> delete = new ArrayList<Point>();
		BufferedImage out = ImageUtil.copy(img);

		for (Matrix m : aStructs) {
			for (int x = 1; x < img.getWidth() - 1; x++) {
				for (int y = 1; y < img.getHeight() - 1; y++) {
					Matrix pixels = new Matrix(new float[][] {
							{ out.getRGB(x - 1, y - 1) & 0xFF, out.getRGB(x + 0, y - 1) & 0xFF,
									out.getRGB(x + 1, y - 1) & 0xFF },
							{ out.getRGB(x - 1, y + 0) & 0xFF, out.getRGB(x + 0, y + 0) & 0xFF,
									out.getRGB(x + 1, y + 0) & 0xFF },
							{ out.getRGB(x - 1, y + 1) & 0xFF, out.getRGB(x + 0, y + 1) & 0xFF,
									out.getRGB(x + 1, y + 1) & 0xFF } });

					boolean shouldDelete = true;
					for (int i = 0; i < 3; i++) {
						for (int j = 0; j < 3; j++) {
							if (m.getValue(i, j) == 2 || !shouldDelete) {
								continue;
							}
							if (pixels.getValue(i, j) == m.getValue(i, j)) {
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
				out.setRGB((int) p.getX(), (int) p.getY(), new Color(255, 255, 255).getRGB());
			}
			delete.clear();
		}

		if (ImageUtil.equals(img, out) && iterations < 0) {
			return out;
		} else {
			return thin(out, --iterations);
		}

	}
}
