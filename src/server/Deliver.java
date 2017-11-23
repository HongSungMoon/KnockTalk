package server;

import java.net.InetAddress;

import audio.AudioBuffer;
import audio.AudioReceiver;
import audio.AudioSender;
import audio.AudioServer;

public class Deliver extends Thread {
	
	private AudioSender audioSender;
	private AudioReceiver receiver;
	private AudioBuffer[] audioBuffers;
	private byte tempBuffer[] = new byte[AudioBuffer.AUDIO_BUFFER_SIZE]; 
	
	private boolean flag = false;
	private int readIndex;	// audio data
	InetAddress address;
	private int clientNumber;
	
	public Deliver(InetAddress address, int clientNumber) {
		debug.Debug.log("Deliver : Create	IP : " + address.toString());
		this.address = address;
		this.clientNumber = clientNumber;
		receiver = new AudioReceiver();
		receiver.start();
		readIndex = AudioServer.writeIndex;
		audioBuffers = Server.audioBuffers;
		audioSender = new AudioSender(this);
		audioSender.setAddress(this.address);
	}
	
	public void remove() {
		receiver.remove();
		flag = true;
		this.interrupt();
	}

	public void run() {
//		long beforeTime = 0, currentTime = 0;
		while(true) {
			if(flag)
				return;
//			beforeTime = System.currentTimeMillis();
			audioBuffers[readIndex].getBuffer(tempBuffer, 1);
			readIndex = (readIndex + 1) % Server.BUFFER_SIZE;
//			debug.Debug.log("Deliver	IP : " + address.toString() + " Read");
//			debug.Debug.log("Deliver	IP : " + address.toString() + ": READ DATA" + "readIndex : " + readIndex);
			audioSender.sendData(tempBuffer, readIndex);
//			currentTime = System.currentTimeMillis();
//			System.out.println("Deliver Read    " + (currentTime-beforeTime) + "         Index = " + readIndex);
			try {
			Server.control.clientControl(this);
			}
			catch(Exception e) {
				remove();
			}
		}
	}
	
	public int getClientNumber() {
		return clientNumber;
	}
	
}
