package learning.demo;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleServerDemo {
	public static void main(String... args) throws IOException{
		ServerSocket server = new ServerSocket(8080);
		Utils.log("An instance of server socket is created");
		while(true){
			Utils.log("Waiting for new client");
			Socket socket = server.accept(); //blocking call, never null
			Utils.process(socket);
		}
	}

	
}
