
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client {

	private Socket s;
	private BufferedReader in;
	
	public Client(String host, int port) {
		try {
			s = new Socket(host, port);
			in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
//	public String getLatest() {
//		while(in.hasNext()) {
//			String s = in.nextLine();
//			if(in.hasNextLine()) {
//				continue;
//			} else {
//				return s;
//			}
//		}
//		return null;
//	}
	
	
	
	public static void main(String[] args) {
		Client c = new Client("10.75.66.210", 44);
		System.out.println("Connected to: " + c.s.getInetAddress());
		while(true) {
			try {
				System.out.println(c.getIn().readLine());
			} catch (IOException e) {
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