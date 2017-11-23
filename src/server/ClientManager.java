package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import audio.AudioBuffer;
import file.FileServer;

public class ClientManager {
	
	public static int clientCount = 0;
	private HashMap<InetAddress, Deliver> delivers;
	private Vector<InetAddress> vector;
	private Vector<InetAddress> backVector;
	public static boolean flag[] = new boolean[10]; 
	private int DETECT_SEND_PORT = 9006;
	private DatagramSocket sendSocket = null;
	private DatagramPacket sendPacket = null;
	
	private String msg = "DECT";
	byte[] MsgBytes;
	
	public ClientManager() {
		delivers = new HashMap<InetAddress, Deliver>();
		vector = new Vector<InetAddress>();
		backVector = new Vector<InetAddress>();
		try {
			backVector.add(InetAddress.getByName("192.168.0.6"));
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		MsgBytes = msg.getBytes();
	}

	public boolean existClient(InetAddress address) {
		if(delivers.containsKey(address))
			return true;
		return false;
	}
	
	public void addClient(InetAddress address) {
		clientCount++;
		Deliver deliver = new Deliver(address, clientCount);
		deliver.start();
		delivers.put(address, deliver);
		vector.add(address);
	}
	
	public void removeClient(InetAddress address) {
		clientCount--;
		delivers.get(address).remove();
		delivers.get(address).interrupt();
		delivers.remove(address);
		for(int i=0; i<vector.size(); i++) {
			if(vector.get(i) == address)
				vector.remove(i);
		}
	}
	
	public void Detect() throws UnknownHostException {
		for(int i=0; i<backVector.size(); i++) {
			InetAddress ClientAddress = backVector.get(i);
			try {
				sendSocket = new DatagramSocket();
			} catch (SocketException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			sendPacket = new DatagramPacket(MsgBytes, MsgBytes.length, ClientAddress, DETECT_SEND_PORT);
			try {
				sendSocket.send(sendPacket);
				System.out.println("Send Detect MSG clientAddress : " + ClientAddress.toString() + "index : " + i);
				sendSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void getInfo(InetAddress address) {
		if(delivers.containsKey(address)) {
			
		}
	}

	public void setBackgroundVector(InetAddress address) {
		if(!backVector.contains(address)) {
			backVector.add(address);
			debug.Debug.log("BackgroundVector Added     address : " + address.toString());
		}
			
	}
	
}
