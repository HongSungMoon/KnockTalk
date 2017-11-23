package server;

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Vector;

import audio.AudioBuffer;
import audio.AudioServer;
import database.Database;
import file.FileManager;
import file.FileServer;
import servo.ShellCommander;
import threadControl.ThreadControl;

public class Server extends Thread {

	private DatagramSocket serverSocket = null;
	private DatagramPacket serverPacket = null;
	private DatagramPacket infoPacket = null;
	private byte[] msg = new byte[4];
	private byte[] infoMsg = new byte[500];

	final private int CONN_PORT = 9000;

	private ClientManager clientManager;
	private AudioServer audioServer;
	private FileServer fileServer;
	private Database database;
	private ShellCommander shellCommander;
	
	public static AudioBuffer[] audioBuffers;

	public static ThreadControl control;

	public static final int BUFFER_SIZE = 5;
	final private String START_PATH = "/home/pi/TestServer5/";
	final private String END_PATH = ".avi";

	public Server() {
		// clientManager = new ClientManager();
		audioBuffers = new AudioBuffer[BUFFER_SIZE];
		for (int i = 0; i < BUFFER_SIZE; i++) {
			audioBuffers[i] = new AudioBuffer();
		}
		audioServer = new AudioServer(audioBuffers);
		audioServer.start();
		clientManager = new ClientManager();
		control = new ThreadControl();
		fileServer = new FileServer();
		fileServer.start();
		database = new Database();
		shellCommander = new ShellCommander();
	}

	public void run() {
		try {
			serverSocket = new DatagramSocket(CONN_PORT);
			serverSocket.setReceiveBufferSize(msg.length);
			serverPacket = new DatagramPacket(msg, msg.length);
			infoPacket = new DatagramPacket(infoMsg, infoMsg.length);
		} catch (SocketException e) {
			debug.Debug.log("Server : Socket Open Error");
		}
		while (true) {

			try {
				debug.Debug.log("Server : Run");
				while (true) {
					debug.Debug.log("Server : Listening");
					serverSocket.setReceiveBufferSize(msg.length);
					serverSocket.receive(serverPacket);
					msg = serverPacket.getData();
					debug.Debug.log(new String(msg));
					switch (new String(msg)) {
					case "CONN":
						debug.Debug.log("Server : Connection");
						clientConnect(serverPacket.getAddress());
						break;
					case "EXIT":
						debug.Debug.log("Server : Exit");
						clientExit(serverPacket.getAddress());
						break;
					case "INFO":
						debug.Debug.log("Server : INFO");
						serverSocket.setReceiveBufferSize(infoMsg.length);
						serverSocket.receive(infoPacket);
						infoMsg = infoPacket.getData();
						String tmpfileName = new String(infoMsg);
						String fileName = tmpfileName.substring(0, 19);
						long fileSize = getFileSize(fileName);
						debug.Debug.log("File Name : " + fileName + " Size : " + fileSize);
						database.insertDB(fileName);
						break;
					case "DECT":
						debug.Debug.log("Server : DECT");
						clientManager.Detect();
						break;
					case "BACK":
						clientManager.setBackgroundVector(serverPacket.getAddress());
						break;
					case "OPEN":
						shellCommander.openDoor();
						break;
					default:
						break;
					}

				}
			} catch (IOException e) {
				debug.Debug.log("Server : Socket Open Error");
			} catch (Exception e) {
				debug.Debug.log("Server : Door Open Error");
			}
		}
	}

	private void clientConnect(InetAddress address) {
		if (!clientManager.existClient(address)) {
			debug.Debug.log("Server : New Client Connection - IP : " + address.toString());
			clientManager.addClient(address);
		}
	}

	private void clientExit(InetAddress address) {
		if (clientManager.existClient(address)) {
			debug.Debug.log("Server : Client Exit - IP : " + address.toString());
			clientManager.removeClient(address);
		}
	}

	public long getFileSize(String fileName) {
		String filePath = START_PATH + fileName + END_PATH;
		File file = new File(filePath);
		long fileSize = file.length();
		return fileSize;
	}

}
