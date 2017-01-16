package VisualProcessing;


import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;

import com.github.sarxos.webcam.Webcam;

public class Main {
	public static void main(String[] args) throws IOException {
		Webcam webcam = Webcam.getDefault();
		if (webcam != null) {
			System.out.println("Webcam: " + webcam.getName());
		} else {
			System.out.println("No webcam detected");
		}
		
		webcam.open();
		
		BufferedImage img = webcam.getImage();
		
		JFrame frame = new JFrame();
		JFrame frame1 = new JFrame();
		JFrame frame2 = new JFrame();
		
		BufferedImage screen = new BufferedImage(img.getWidth() * 10, img.getHeight() * 10, BufferedImage.TYPE_3BYTE_BGR);
		Graphics g = screen.createGraphics();
	
		frame.add(new JLabel(new ImageIcon(screen)));
		frame.pack();
		frame.setVisible(true);
		
		BufferedImage screen2 = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
		Graphics g2 = screen2.createGraphics();
	
		frame2.add(new JLabel(new ImageIcon(screen2)));
		frame2.pack();
		frame2.setVisible(true);
		
		
		BufferedImage screen1 = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
		Graphics g1 = screen1.createGraphics();

	
		frame1.add(new JLabel(new ImageIcon(screen1)));
		frame1.pack();
		frame1.setVisible(true);
		frame1.setLocationRelativeTo(null);
		frame.setLocation(500, 500);
		
		JFrame options = new JFrame();
		JSlider bar1 = new JSlider();
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
		
		long lastTime = System.nanoTime();
		long startTime = System.nanoTime();
		long deltaTime = 0;
		int frames = 0;
		
		int lowPass = 3;
		int threshHold = 34;
		
		while(true) {
			startTime = System.nanoTime();
			deltaTime += -(lastTime - startTime);
			
			threshHold = bar2.getValue();
			lowPass = bar1.getValue();
			
			img =webcam.getImage();
			BufferedImage imgProc = ImageUtil.grayScale(img);
			imgProc = ImageUtil.lowPass(imgProc, lowPass/100f);
			imgProc = ImageUtil.f2b(ImageUtil.singleBandEdgeDetection(imgProc)[0]);
			imgProc = ImageUtil.threshHold(imgProc,threshHold);
			imgProc = ImageUtil.invert(imgProc);
			g2.drawImage(imgProc, 0, 0, null);
			
			imgProc = EdgeThin.thin(imgProc, 5);
			imgProc = ImageUtil.resize(imgProc, 500, 500);
			g.drawImage(imgProc, 0, 0, imgProc.getWidth(), imgProc.getHeight(), null);
			g1.drawImage(img, 0, 0, img.getWidth(), img.getHeight(), null);
			if(deltaTime >= 1000000000l) {
				deltaTime = 0;
				System.out.println(frames);
				System.out.println("lowPass: " + lowPass + " threshHold: " + threshHold);
				frames = 0;
			}
			
			frame.repaint();
			frame1.repaint();
			frame2.repaint();
			frames++;
			lastTime = startTime;		
		}
		
	}
}