package learning.demo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleServerDemo {
	public static void main(String... args) throws IOException{
		ServerSocket server = new ServerSocket(8080);
		Utils.log("An instance of server socket is created");
		while(true){
			Utils.log("Waiting for new client");
			Socket socket = server.accept(); //blocking call, never null
			String clientID = socket.getInetAddress().getCanonicalHostName();
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
	}
}
