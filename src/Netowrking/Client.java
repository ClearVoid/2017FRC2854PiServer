package Netowrking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.JFrame;

public class Client extends Thread {

	private Socket s;
	private BufferedReader in;
	private String value;
	
	public Client(String host, int port) {
		try {
			s = new Socket(host, port);
			in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			this.start();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		
	}
	
	public String getLatest() {
		return value;
	}

	@Override
	public void run() {
		try {
			while(!s.isClosed() || s.isConnected()) {
			value = in.readLine();
			if(value == null) {
				break;
			}
			}
		} catch (IOException e) {
			//e.printStackTrace();
		} finally {
			try {
				s.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	// r + c = l - 1
	public static void main(String[] args) {
		Client c = new Client("localhost", 44);
		System.out.println("Connected to: " + c.s.getInetAddress());
		while(!c.s.isClosed()) {
			try {
				System.out.println(c.getLatest());
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public Socket getS() {
		return s;
	}

	public void setS(Socket s) {
		this.s = s;
	}

	public BufferedReader getIn() {
		return in;
	}

	public void setIn(BufferedReader in) {
		this.in = in;
	}


	
	
}