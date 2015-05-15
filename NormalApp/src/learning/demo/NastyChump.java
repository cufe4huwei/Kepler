package learning.demo;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class NastyChump {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		for(int i = 0; i < 1; i++){
			try {
				Socket client = new Socket("localhost", 8080);
				Utils.log("Connect to server No.%d", i);
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				Utils.log("Fail to connect to server No.%d", i);
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Utils.log("Fail to connect to server No.%d", i);
				e.printStackTrace();
			}
		}
	}
}
