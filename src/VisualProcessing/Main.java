package VisualProcessing;


import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;

import com.github.sarxos.webcam.Webcam;




public class Main {
	public static Webcam webcam = Webcam.getWebcams().get(1);
	
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
	
	public static long lastTime = System.nanoTime();
	public static long startTime = System.nanoTime();
	public static long deltaTime = 0;
	public static int frames;
	
	
	public static void webcamInit(){
		System.out.println("Initializing Webcam");
		if (webcam != null){System.out.println("Webcam: " + webcam.getName());} else {System.out.println("No webcam detected");}
		webcam.open();
		feedFrame = webcam.getImage();
	}
	public static void JFrameInit(){
		System.out.println("Initializing JFrames");
		BufferedImage screen = new BufferedImage(feedFrame.getWidth() * 10, feedFrame.getHeight() * 10, BufferedImage.TYPE_3BYTE_BGR);
		g = screen.createGraphics();
	
		frame.add(new JLabel(new ImageIcon(screen)));
		frame.pack();
	//	frame.setVisible(true);
		
		BufferedImage screen2 = new BufferedImage(feedFrame.getWidth(), feedFrame.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
		g2 = screen2.createGraphics();
	
		frame2.add(new JLabel(new ImageIcon(screen2)));
		frame2.pack();
	//	frame2.setVisible(true);
		
		
		BufferedImage screen1 = new BufferedImage(feedFrame.getWidth(), feedFrame.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
		g1 = screen1.createGraphics();

	
		frame1.add(new JLabel(new ImageIcon(screen1)));
		frame1.pack();
	//	frame1.setVisible(true);
		frame1.setLocationRelativeTo(null);
		frame.setLocation(500, 500);
		
		bar1.setMaximum(500);
		bar1.setValue(108);
		bar1.setMinimum(1);
		JSlider bar2 = new JSlider();
		bar2.setMaximum(255);
		bar2.setValue(167);
		
		options.add(bar1, BorderLayout.PAGE_START);
		options.add(bar2, BorderLayout.PAGE_END);
		options.pack();
		options.setResizable(false);
		options.setVisible(true);
	}
	public static void feedProcess(int lowPass, int threshHold){
		long startTime = System.nanoTime();
		feedFrame = webcam.getImage();
		System.out.printf("%-35s%,15d\n", "Time to get image: " , -(startTime - (startTime = System.nanoTime())));
		feedArr = ImageUtil.grayScale(feedFrame);
		System.out.printf("%-35s%,15d\n","Time to grayScale: " , -(startTime - (startTime = System.nanoTime())) );
		feedArr = ImageUtil.lowPass(feedArr, lowPass/100f);
		System.out.printf("%-35s%,15d\n","Time to low pass: " , -(startTime - (startTime = System.nanoTime())) );
		feedArr = ImageUtil.singleBandEdgeDetection(feedArr);
		System.out.printf("%-35s%,15d\n","Time to edge detect: " , -(startTime - (startTime = System.nanoTime())) );
		feedArr = ImageUtil.threshHold(feedArr,threshHold);
		System.out.printf("%-35s%,15d\n","Time to thresh hold: " , -(startTime - (startTime = System.nanoTime())) );
		feedArr = ImageUtil.invert(feedArr);
		//g2.drawImage(feedProc, 0, 0, null);
		System.out.printf("%-35s%,15d\n","Time to invert: " , -(startTime - (startTime = System.nanoTime())) );
		feedArr = EdgeThin.thin(feedArr);
		System.out.printf("%-35s%,15d\n","Time to thin: " , -(startTime - (startTime = System.nanoTime())) );
		System.out.println();
		//feedProc = ImageUtil.resize(feedProc, 500, 500);
		//feedProc = ImageUtil.f2b(feedArr);
		//g.drawImage(feedProc, 0, 0, feedProc.getWidth(), feedProc.getHeight(), null);
		//g1.drawImage(feedFrame, 0, 0, feedFrame.getWidth(), feedFrame.getHeight(), null);
		
	}
	public static void main(String[] args) throws IOException {
		
		webcamInit();
		//JFrameInit();
		EdgeThin.init();
		System.out.println("Starting to run");
		lastTime = System.nanoTime();
		startTime = System.nanoTime();
		deltaTime = 0;
		frames = 0;
		
		int lowPass = 3;
		int threshHold = 34;
		
		while(true) {

			startTime = System.nanoTime();
			deltaTime += -(lastTime - startTime);
			
			threshHold = bar2.getValue();
			lowPass = bar1.getValue();
			
			feedProcess(lowPass,threshHold);
			
			if(deltaTime >= 1000000000l) {System.out.println("FPS: " + 1000000000d/(double)(startTime - lastTime));}
			
		//	frame.repaint();
		//	frame1.repaint();
		//	frame2.repaint();
			frames++;
			lastTime = startTime;		

		}
		
	}
}