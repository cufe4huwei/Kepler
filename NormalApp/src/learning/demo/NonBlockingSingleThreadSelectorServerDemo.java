package learning.demo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class NonBlockingSingleThreadSelectorServerDemo {
	private static Map<SocketChannel, Queue<ByteBuffer>> pendingData = new HashMap<>();
	private static Selector selector;
	
	public static void main(String... args) throws IOException{
		ServerSocketChannel server = ServerSocketChannel.open();
		server.bind(new InetSocketAddress("localhost", 8080));
		server.configureBlocking(false);
		Utils.log("An instance of server socket is created");
		
		selector = Selector.open();
		server.register(selector, SelectionKey.OP_ACCEPT);
		
//			ExecutorService pool = new ThreadPoolExecutor(0, 1000, 60, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
		while(true){
			int ready = selector.select();
			Utils.log("Ready count is %d", ready);
			Iterator<SelectionKey> itKeys = selector.selectedKeys().iterator();
			while(itKeys.hasNext()){
				SelectionKey key = itKeys.next();
				itKeys.remove();
				if(key.isValid()){
					if(key.isAcceptable()){
						accept(key);
					} else if(key.isReadable()){
						read(key);
					} else if(key.isWritable()){
						write(key);
					}
				}
			}
		}
		
	}
	
	private static void write(SelectionKey key) throws IOException {
		// TODO Auto-generated method stub
		Utils.log("write %s", key);
		SocketChannel client = (SocketChannel) key.channel();
		Queue<ByteBuffer> data = pendingData.get(client);
		ByteBuffer buffer;
		while((buffer = data.peek()) != null){
			client.write(buffer);
			if(!buffer.hasRemaining()){
				data.poll();
			} else {
				return;
			}
		}
		client.register(selector, SelectionKey.OP_READ);
	}

	private static void read(SelectionKey key) throws IOException {
		// TODO Auto-generated method stub
		Utils.log("read %s", key);
		SocketChannel client = (SocketChannel) key.channel();
		ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
		int read = client.read(buffer);
		if(read == -1){
			pendingData.remove(client);
			client.close();
			return;
		}
		buffer.flip();
		for(int i = 0; i < buffer.limit(); i++){
			buffer.put(i, (byte) Utils.transmogrify(buffer.get(i)));
		}
		pendingData.get(client).add(buffer);
		client.register(selector, SelectionKey.OP_WRITE);
	}

	private static void accept(SelectionKey key) throws IOException {
		// TODO Auto-generated method stub
		Utils.log("accept %s", key);
		ServerSocketChannel channel = (ServerSocketChannel) key.channel();
		SocketChannel client = channel.accept();
		client.configureBlocking(false);
		client.register(selector, SelectionKey.OP_READ);
		pendingData.put(client, new ConcurrentLinkedQueue<ByteBuffer>());
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
