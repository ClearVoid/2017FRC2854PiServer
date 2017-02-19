package VisualProcessing;

import Networking.Server;

public class Main {

	public static void main(String[] args) {
		VisualDriver.init(new Server(44));
		VisualDriver.run();
	}
	
}
