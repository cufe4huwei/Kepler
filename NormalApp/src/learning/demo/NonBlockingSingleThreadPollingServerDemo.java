package learning.demo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

public class NonBlockingSingleThreadPollingServerDemo {

	private static Collection<SocketChannel> sockets = Collections.newSetFromMap(new HashMap<SocketChannel, Boolean>());
	
	public static void main(String... args) throws IOException{
		ServerSocketChannel server = ServerSocketChannel.open();
		server.bind(new InetSocketAddress("localhost", 8080));
		server.configureBlocking(false);
		Utils.log("An instance of server socket is created");
		
//			ExecutorService pool = new ThreadPoolExecutor(0, 1000, 60, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
		while(true){
			Utils.log("Waiting for new client");
			SocketChannel socketChannel = server.accept(); //non-blocking call, usually return null
//				pool.submit(new SocketProcessImpl(socketChannel));
			if(socketChannel != null){
				Utils.log("Connection from %s", socketChannel.getRemoteAddress());
				socketChannel.configureBlocking(false);
				sockets.add(socketChannel);
			}
			Iterator<SocketChannel> it = sockets.iterator();
			Utils.log(sockets.toString());
			while(it.hasNext()){
				SocketChannel socket = it.next();
				ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
				int read = socket.read(buffer);
				if(read == -1){
					it.remove();
				} else if(read != 0){
					buffer.flip();
					for(int i = 0; i < buffer.limit(); i++){
						buffer.put(i, (byte) Utils.transmogrify(buffer.get(i)));
					}
					socket.write(buffer);
					buffer.clear();
				}
			}
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
