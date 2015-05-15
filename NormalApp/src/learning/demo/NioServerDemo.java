package learning.demo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class NioServerDemo {

		public static void main(String... args) throws IOException{
			ServerSocketChannel server = ServerSocketChannel.open();
			server.bind(new InetSocketAddress("localhost", 8080));
			Utils.log("An instance of server socket is created");
			
			ExecutorService pool = new ThreadPoolExecutor(0, 1000, 60, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
			while(true){
				Utils.log("Waiting for new client");
				SocketChannel socketChannel = server.accept(); //blocking call, never null
				pool.submit(new SocketProcessImpl(socketChannel));
			}
		}
		
		private static class SocketProcessImpl implements Runnable {
			private SocketChannel channel = null;
			public SocketProcessImpl(SocketChannel channel) {
				super();
				this.channel = channel;
			}
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Utils.process(channel);
			}
			
		}

}
