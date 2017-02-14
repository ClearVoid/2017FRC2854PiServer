package Netowrking;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server{

	private PrintWriter writer;
	private boolean isReady = false;
	private Socket s;
	
	private final int backflow = 5;
	
	public Server(int port)  {
		ServerSocket socket = null;
		try {
			socket = new ServerSocket(port, backflow);
			System.out.println("Starting new Server at " + socket.getInetAddress());
			while((s =socket.accept()) == null) {
				Thread.sleep(10);
			}
			System.out.println("A client at " + s.getInetAddress() + " connected");
			writer =  new PrintWriter(s.getOutputStream(), true);
			writer.println("Server ping");
			isReady = true;
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) throws InterruptedException {
		Server s = new Server(44);
		while(true) {
			int rand = (int) (Math.random() * 100);
			System.out.println("Sending: " + rand);
			s.getWriter().println(rand + "");
			Thread.sleep(1000);
		}
	}

	public PrintWriter getWriter() {
		return writer;
	}

	public void setWriter(PrintWriter writer) {
		this.writer = writer;
	}

	public boolean isReady() {
		return isReady;
	}

	public void setReady(boolean isReady) {
		this.isReady = isReady;
	}

	public Socket getS() {
		return s;
	}

	public void setS(Socket s) {
		this.s = s;
	}

	public int getBackflow() {
		return backflow;
	}
	

	
}
