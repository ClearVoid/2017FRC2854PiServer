package VisualProcessing;

import Netowrking.Server;

public class Main {

	public static void main(String[] args) {
		VisualDriver.init(new Server(44));
		VisualDriver.run();
	}
	
}
