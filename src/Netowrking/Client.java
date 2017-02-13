package Netowrking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client {

	private Socket s;
	private Scanner in;
	
	public Client(int port, InetAddress ip) {
		try {
			s = new Socket(ip, port);
			in = new Scanner(new InputStreamReader(s.getInputStream()));
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public String getLatest() {
		while(in.hasNext()) {
			String s = in.nextLine();
			if(in.hasNextLine()) {
				continue;
			} else {
				return s;
			}
		}
		return null;
	}
	
}
