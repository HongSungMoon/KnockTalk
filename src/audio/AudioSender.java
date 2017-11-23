package audio;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

import debug.Debug;
import server.Deliver;

public class AudioSender extends Thread{
	
	private int AUDIO_OUTPUT_PORT = 9001;
	private DatagramSocket sendSocket;
	private DatagramPacket sendPacket;
	private Deliver deliver;
	
	public AudioSender(Deliver deliver) {
		debug.Debug.log("AudioSender : Create");	
		this.deliver = deliver;
	}
	
	public void setAddress(InetAddress address) {
		try {
			sendSocket = new DatagramSocket();
			sendPacket = new DatagramPacket(new byte[AudioBuffer.AUDIO_BUFFER_SIZE], AudioBuffer.AUDIO_BUFFER_SIZE,
					address, AUDIO_OUTPUT_PORT);
			debug.Debug.log("AudioSender : setAddress	IP : " + address.toString() );	
		} catch (IOException e) {
		}
	}

	public void sendData(byte[] buffer, int index) {		
		long beforeTime = 0, currentTime = 0;
		try {
			//beforeTime = System.currentTimeMillis();
			sendPacket.setData(buffer, 0, buffer.length);
			sendSocket.send(sendPacket);
			debug.Debug.log("AudioSender : sendData  Index : " + index + "   Client : " + deliver.getClientNumber());

			//currentTime = System.currentTimeMillis();
		} catch (IOException e) {
			debug.Debug.log("UDPSENDER : SEND ERROR");
			deliver.remove();
		}
	}

}
