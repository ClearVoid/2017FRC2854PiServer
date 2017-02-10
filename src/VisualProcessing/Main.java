package VisualProcessing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;

import com.github.sarxos.webcam.Webcam;

public class Main {

	public static Webcam webcam = Webcam.getDefault();

	// public static Webcam webcam = Webcam.getWebcams().get(2);

	public static BufferedImage feedFrame;
	public static BufferedImage feedProc;
	public static int[][] feedArr;

	public static JFrame frame = new JFrame();
	public static JFrame frame1 = new JFrame();
	public static JFrame frame2 = new JFrame();

	public static BufferedImage screen;
	public static BufferedImage screen2;

	public static Graphics g;
	public static Graphics g1;
	public static Graphics g2;

	public static JFrame options = new JFrame();
	public static JSlider bar1 = new JSlider();
	public static JSlider bar2 = new JSlider();
	public static JSlider bar3 = new JSlider();

	public static JLabel label1 = new JLabel();
	public static JLabel label2 = new JLabel();
	public static JLabel label3 = new JLabel();

	public static JPanel panel = new JPanel();

	public static long lastTime = System.nanoTime();
	public static long startTime = System.nanoTime();
	public static long deltaTime = 0;
	public static int frames;

	public static double avgDistance;
	public static int count;

	public static final double tanAngle = 1.333;
	
	private static final double[] focals = new double[] {177.31, 211.75};
	private static final double[] imagesize = new double[] {27.4, 30.75};
	//27.4
	//30.75

	public static void webcamInit() {
		System.out.println("Initializing Webcam");
		System.out.println(Webcam.getWebcams());
		if (webcam != null) {
			System.out.println("Webcam: " + webcam.getName());
		} else {
			System.out.println("No webcam detected");
		}
		webcam.open();
		feedFrame = webcam.getImage();
	}

	public static void JFrameInit() {
		System.out.println("Initializing JFrames");
		BufferedImage screen = new BufferedImage(feedFrame.getWidth() * 2, feedFrame.getHeight() * 2,
				BufferedImage.TYPE_3BYTE_BGR);
		g = screen.createGraphics();
		g.setColor(Color.red);

		frame.add(new JLabel(new ImageIcon(screen)));
		frame.pack();
		frame.setVisible(true);
		frame.setTitle("Final Image");

		BufferedImage screen2 = new BufferedImage(feedFrame.getWidth(), feedFrame.getHeight(),
				BufferedImage.TYPE_3BYTE_BGR);
		g2 = screen2.createGraphics();
		g2.setColor(Color.red);

		frame2.add(new JLabel(new ImageIcon(screen2)));
		frame2.pack();
		frame2.setVisible(true);

		BufferedImage screen1 = new BufferedImage(feedFrame.getWidth(), feedFrame.getHeight(),
				BufferedImage.TYPE_3BYTE_BGR);
		g1 = screen1.createGraphics();
		g1.setColor(Color.red);

		frame1.add(new JLabel(new ImageIcon(screen1)));
		frame1.pack();
		frame1.setVisible(true);
		frame1.setLocationRelativeTo(null);
		frame1.setTitle("Camera");
		frame.setLocation(500, 500);

		label1.setText("ColorCut Error: ");
		label1.setLabelFor(bar1);
		label2.setText("LowPass: ");
		label2.setLabelFor(bar2);
		label3.setText("ThreshHold: ");
		label3.setLabelFor(bar3);

		bar1.setMaximum(500);
		bar1.setValue((int) (2.12 * 50));
		bar1.setMinimum(1);
		bar1.setPaintLabels(true);
		bar2.setMaximum(500);
		bar2.setValue((int) (.73 * 100));
		bar2.setMinimum(1);
		bar2.setPaintLabels(true);
		bar3.setMaximum(1000);
		bar3.setValue(173);
		bar3.setMinimum(1);
		bar3.setPaintLabels(true);

		panel = new JPanel();

		panel.add(label1);
		panel.add(bar1);
		panel.add(label2);
		panel.add(bar2);
		panel.add(label3);
		panel.add(bar3);

		options.add(panel);
		options.setSize(1500, 65);
		// options.pack();
		options.setVisible(true);
	}
	
	public static void feedProcess(float lowPass, int threshHold, float[] averages, float[] deviations, float stError) {
		// long startTime = System.nanoTime();
		feedFrame = webcam.getImage();
		// System.out.println(feedFrame.getWidth());
		// System.out.printf("%-35s%,15d\n", "Time to get image: " , -(startTime
		// - (startTime = System.nanoTime())));
		feedProc = featureDetection.colorCut(feedFrame, averages, deviations, stError);
		// System.out.println(bar1.getValue()/100f);
		// System.out.printf("%-35s%,15d\n", "Time to color cut: " , -(startTime
		// - (startTime = System.nanoTime())));
		feedArr = FundUtil.grayScale(feedProc);
		// System.out.printf("%-35s%,15d\n","Time to grayScale: " , -(startTime
		// - (startTime = System.nanoTime())) );
		feedArr = ImageNoise.lowPass(feedArr, lowPass);
		// System.out.printf("%-35s%,15d\n","Time to low pass: " , -(startTime -
		// (startTime = System.nanoTime())) );
		feedArr = featureDetection.edgeDetection(feedArr);
		// System.out.printf("%-35s%,15d\n","Time to edge detect: " ,
		// -(startTime - (startTime = System.nanoTime())) );
		feedArr = FundUtil.threshHold(feedArr, threshHold);
		// System.out.printf("%-35s%,15d\n","Time to thrilled: " ,
		// -(startTime - (startTime = System.nanoTime())) );
		feedArr = FundUtil.invert(feedArr);
		// System.out.printf("%-35s%,15d\n","Time to invert: " , -(startTime -
		// (startTime = System.nanoTime())) );
		// feedArr = EdgeThin.thin(feedArr);
		// System.out.printf("%-35s%,15d\n","Time to thin: " , -(startTime -
		// (startTime = System.nanoTime())) );
		// System.out.println();
		feedArr = featureDetection.fillClosed(feedArr);
		feedProc = ImageUtil.arrayToImg(feedArr);

		Point p = ImageAnalysis.findCenter(feedArr);
		if (p != null && p.x != 0) {
			int[][][] splitImages = ImageTransform.splitImage(feedArr, p, true);

			BufferedImage imgLeft = ImageUtil.arrayToImg(splitImages[0]);
			BufferedImage imgRight = ImageUtil.arrayToImg(splitImages[1]);
			Rectangle leftRect = ImageAnalysis.getRect(splitImages[0], .5f);
			Rectangle rightRect = ImageAnalysis.getRect(splitImages[1], .5f);
			g.drawImage(imgLeft, 0, 0, imgLeft.getWidth(), imgLeft.getHeight(), null);
			if (leftRect != null) {
				g.drawRect(leftRect.x, leftRect.y, leftRect.width, leftRect.height);

				System.out.println(Arrays.toString(VisualMath.getDimensions(leftRect, imagesize, focals)));
			}

			g2.drawImage(imgRight, 0, 0, imgRight.getWidth(), imgRight.getHeight(), null);
			if (rightRect != null) {
				g2.drawRect(rightRect.x, rightRect.y, rightRect.width, rightRect.height);
			}
		} else {
			g.drawImage(feedProc, 0, 0, feedProc.getWidth(), feedProc.getHeight(), null);
		}
			g1.drawImage(feedFrame, 0, 0, feedFrame.getWidth(), feedFrame.getHeight(), null);

	}

	public static void main(String[] args) {
		try {
			
			webcamInit();
			JFrameInit();
			EdgeThin.init();
			System.out.println("Starting to run");
			lastTime = System.nanoTime();
			startTime = System.nanoTime();
			deltaTime = 0;
			frames = 0;

			float lowPass = 3;
			int threshHold = 34;
			float stError = 3;

			final float[] ratios = new float[] { 170.51f, 250.748f, 250.8337f }; // windows
																				// log
																				// on
			float[] deviations = new float[] { 65.42f, 4.311f, 13.71f };

			// final float[] ratios = new float[] {85.852f, 253.0721f, 217.07f};
			// //blue screens
			// float[] deviations = new float[] {67.31f,6.61f, 49.46f};

			while (true) {

				startTime = System.nanoTime();
				deltaTime += -(lastTime - startTime);

				stError = bar1.getValue() / 50f;
				lowPass = bar2.getValue() / 100f;
				threshHold = bar3.getValue();

				label1.setText("ColorCut Error: " + stError);
				label2.setText("LowPass: " + lowPass);
				label3.setText("ThreshHold: " + threshHold);

				feedProcess(lowPass, threshHold, ratios, deviations, stError);

				if (deltaTime >= 1000000000l) {
					System.out.println("FPS: " + 1000000000d / (double) (startTime - lastTime));
					deltaTime = 0;
					System.out.println(label1.getText() + " " + label2.getText() + " " + label3.getText());
				}

				frame.repaint();
				frame.pack();
				frame1.repaint();
				frame1.pack();
				frame2.repaint();
				frame2.pack();
				frames++;
				lastTime = startTime;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			webcam.close();
		}

	}
}