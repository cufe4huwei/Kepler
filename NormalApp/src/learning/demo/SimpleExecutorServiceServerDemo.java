package learning.demo;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class SimpleExecutorServiceServerDemo {
	public static void main(String... args) throws IOException{
		ServerSocket server = new ServerSocket(8080);
		Utils.log("An instance of server socket is created");
		ExecutorService pool = new ThreadPoolExecutor(0, 1000, 60, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
		while(true){
			Utils.log("Waiting for new client");
			Socket socket = server.accept(); //blocking call, never null
			pool.submit(new SocketProcessImpl(socket));
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
