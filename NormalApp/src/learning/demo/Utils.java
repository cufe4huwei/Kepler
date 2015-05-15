package learning.demo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class Utils {
	public static int transmogrify(int src){
		if(Character.isLetter(src)){
			return src ^ ' ';
		} else {
			return src;
		}
	}
	
	public static void log(String format, Object... args) {
		System.out.println(String.format(format, args));
	}
	
	public static void process(Socket socket) {
		String clientID = socket.getRemoteSocketAddress().toString();
		Utils.log("A client of %s is acceptted", clientID);
		InputStream input = null;
		OutputStream output = null;
		try {
			input = socket.getInputStream();
			output = socket.getOutputStream();
			int data;
			while((data = input.read()) != -1) {
				data = Utils.transmogrify(data);
				output.write(data);
			}
		} catch(Exception e){
			Utils.log("Error during interact with client, " + e.getMessage());
		}
		finally {
			if(input != null){
				try {
					Utils.log("Close input stream of %s", clientID);
					input.close();
				} catch (Exception e) {
					Utils.log("Error when closing input stream");
				}
			}
			if(output != null){
				try {
					Utils.log("Close out stream of %s", clientID);
					output.close();
				} catch (Exception e) {
					Utils.log("Error when closing output stream");
				}
			}
		}
	}
	
	public static void process(SocketChannel channel) {
		String clientID = "";
		try {
			clientID = channel.getRemoteAddress().toString();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Utils.log("A client of %s is acceptted", clientID);
		try {
			ByteBuffer buffer = ByteBuffer.allocate(1024);
			while((channel.read(buffer)) != -1) {
				buffer.flip();
				Utils.log("position %d, limit %d, capacity %d", buffer.position(), buffer.limit(), buffer.capacity());
				for(int i = 0 ; i < buffer.limit(); i++){
					buffer.put(i, (byte) Utils.transmogrify(buffer.get(i)));
				}
				channel.write(buffer);
				buffer.clear();
			}
		} catch(Exception e){
			Utils.log("Error during interact with client, " + e.getMessage());
		}
		finally {
		}
	}
}