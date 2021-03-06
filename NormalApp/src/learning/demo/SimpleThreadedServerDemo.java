package learning.demo;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleThreadedServerDemo {
	public static void main(String... args) throws IOException{
		ServerSocket server = new ServerSocket(8080);
		Utils.log("An instance of server socket is created");
		while(true){
			Utils.log("Waiting for new client");
			Socket socket = server.accept(); //blocking call, never null
			Thread t = new Thread(new SocketProcessImpl(socket));
			t.start();
		}
	}
	
	private static class SocketProcessImpl implements Runnable {
		private Socket client = null;
		public SocketProcessImpl(Socket client) {
			super();
			this.client = client;
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			Utils.process(client);
		}
		
	}

	
}
