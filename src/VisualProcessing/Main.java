package VisualProcessing;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;

import com.github.sarxos.webcam.Webcam;




public class Main {
	//public static Webcam webcam = Webcam.getDefault();
	public static Webcam webcam = Webcam.getWebcams().get(2);
	
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
	
	
	public static void webcamInit(){
		System.out.println("Initializing Webcam");
		System.out.println(Webcam.getWebcams());
		if (webcam != null){System.out.println("Webcam: " + webcam.getName());} else {System.out.println("No webcam detected");}
		webcam.open();
		feedFrame = webcam.getImage();
	}
	public static void JFrameInit(){
		System.out.println("Initializing JFrames");
		BufferedImage screen = new BufferedImage(feedFrame.getWidth(), feedFrame.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
		g = screen.createGraphics();
	
		frame.add(new JLabel(new ImageIcon(screen)));
		frame.pack();
		frame.setVisible(true);
		frame.setTitle("Final Image");
		
		BufferedImage screen2 = new BufferedImage(feedFrame.getWidth(), feedFrame.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
		g2 = screen2.createGraphics();
	
		frame2.add(new JLabel(new ImageIcon(screen2)));
		frame2.pack();
		//frame2.setVisible(true);
		
		
		BufferedImage screen1 = new BufferedImage(feedFrame.getWidth(), feedFrame.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
		g1 = screen1.createGraphics();

	
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
		bar1.setValue((int) (3.34 * 50));
		bar1.setMinimum(1);
		bar1.setPaintLabels(true);
		bar2.setMaximum(500);
		bar2.setValue(5 * 100);
		bar2.setMinimum(1);
		bar2.setPaintLabels(true);
		bar3.setMaximum(500);
		bar3.setValue(183);
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
		options.setSize(1000, 65);
		options.setResizable(false);
		//options.pack();
		options.setVisible(true);
	}
	public static void feedProcess(float lowPass, int threshHold, float[] averages, float[] devations, float stError){
		//long startTime = System.nanoTime(); 
		feedFrame = webcam.getImage();
		//System.out.printf("%-35s%,15d\n", "Time to get image: " , -(startTime - (startTime = System.nanoTime())));
		feedProc = ImageUtil.colorCut(feedFrame, averages, devations, stError); 
		//System.out.println(bar1.getValue()/100f);
		//System.out.printf("%-35s%,15d\n", "Time to color cut: " , -(startTime - (startTime = System.nanoTime())));
		feedArr = ImageUtil.grayScale(feedFrame);
		//System.out.printf("%-35s%,15d\n","Time to grayScale: " , -(startTime - (startTime = System.nanoTime())) );
		feedArr = ImageUtil.lowPass(feedArr, lowPass);
		//System.out.printf("%-35s%,15d\n","Time to low pass: " , -(startTime - (startTime = System.nanoTime())) );
		feedArr = ImageUtil.singleBandEdgeDetection(feedArr);
		//System.out.printf("%-35s%,15d\n","Time to edge detect: " , -(startTime - (startTime = System.nanoTime())) );
		feedArr = ImageUtil.threshHold(feedArr,threshHold);
		//System.out.printf("%-35s%,15d\n","Time to thresh hold: " , -(startTime - (startTime = System.nanoTime())) );
		feedArr = ImageUtil.invert(feedArr);
		//System.out.printf("%-35s%,15d\n","Time to invert: " , -(startTime - (startTime = System.nanoTime())) );
		//feedArr = EdgeThin.thin(feedArr);
		//System.out.printf("%-35s%,15d\n","Time to thin: " , -(startTime - (startTime = System.nanoTime())) );
		//System.out.println();
		//feedProc = ImageUtil.resize(feedProc, 500, 500);
		Point p = ImageUtil.findCenter(feedArr);
		feedProc = ImageUtil.arrayToImg(feedArr);
		ImageUtil.drawLargePixel(feedProc, p.x, p.y, new Color(255, 0, 0).getRGB());
		g.drawImage(feedProc, 0, 0, feedProc.getWidth(), feedProc.getHeight(), null);
		g1.drawImage(feedFrame, 0, 0, feedFrame.getWidth(), feedFrame.getHeight(), null);
		
	}
	public static void main(String[] args) throws IOException {
		
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
		
		final float[] ratios = new float[] {255f, 255f, 255f};
		final float[] devations = new float[] {10f, 10f, 10f};
		
		while(true) {

			startTime = System.nanoTime();
			deltaTime += -(lastTime - startTime);
			
			stError = bar1.getValue() / 50f;
			lowPass = bar2.getValue() / 100f;
			threshHold = bar3.getValue();
			
			label1.setText("ColorCut Error: " + stError);
			label2.setText("LowPass: " + lowPass);
			label3.setText("ThreshHold: " + threshHold);
			
			feedProcess(lowPass,threshHold, ratios, devations, stError);
			
			if(deltaTime >= 1000000000l) {
				System.out.println("FPS: " + 1000000000d/(double)(startTime - lastTime)); 
				deltaTime = 0;
				System.out.println(label1.getText() + " " + label2.getText() + " " + label3.getText());
			}
			
			frame.repaint();
			frame1.repaint();
			//frame2.repaint();
			frames++;
			lastTime = startTime;		

		}
		
	}
}