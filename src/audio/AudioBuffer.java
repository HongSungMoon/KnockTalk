package audio;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import server.ClientManager;

public class AudioBuffer {

	private byte buffer[];
	public static int AUDIO_BUFFER_SIZE = AudioServer.FRAME_SIZE;

	public AudioBuffer() {
		buffer = new byte[AUDIO_BUFFER_SIZE + 6000];
	}

	public void getBuffer(byte[] tempBuffer, int clientNumber) {
		System.arraycopy(buffer, 0, tempBuffer, 0, AudioBuffer.AUDIO_BUFFER_SIZE);
		// try {
		// //debug.Debug.log("Deliver " + ": WAIT ");
		// wait();
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
	}

	public void setBuffer(byte[] buffer) {
		System.arraycopy(buffer, 0, this.buffer, 0, AudioBuffer.AUDIO_BUFFER_SIZE);
	}

}
