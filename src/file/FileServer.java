package file;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class FileServer extends Thread{
	
	private ServerSocket listener;
	private Socket socket;
	private int FILE_SERVER_PORT = 9004;
	
	public FileServer() {
		try {
			listener = new ServerSocket(FILE_SERVER_PORT);
		} catch (IOException e) {
			debug.Debug.log("FileServer ServerSocket is not Created");
		}
	}
	
	public void run() {
		while (true) {
			try {
				socket = listener.accept();
				System.out.println("Client Accept     Address : " + socket.getInetAddress().toString());
				FileManager fileManager = new FileManager(socket);
				fileManager.start();
				socket = null;
			} catch (IOException e) {
				debug.Debug.log("FileServer ClientSocket is not Created");
			}
		}
	}

}
