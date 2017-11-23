package audio;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import javax.print.attribute.standard.Severity;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

import server.ClientManager;
import server.Server;

public class AudioServer extends Thread {

	public final int BUFFER_SIZE = Server.BUFFER_SIZE;
	public final static float AUDIO_SAMPLERATE = 44100.0F; // Sample Rate
	public final int INT_AUDIO_SAMPLERATE = 44100; // Sample Rate
	public final static int NUMBER_OF_BITS_IN_CHANNEL = 16; // Number of bits
	public final static int NUMBER_OF_CHANNELS = 2; // Number of channels
	public final int NUMBER_OF_BYTES_IN_FRAME = 4; // Number of bytes in
	public final float NUMBER_OF_FRAME_PER_SECOND = 44100.0F; // Number
	public final static int FRAME_SIZE = (int) AUDIO_SAMPLERATE * NUMBER_OF_CHANNELS * NUMBER_OF_BITS_IN_CHANNEL
			/ Byte.SIZE / 50 * 6 + 1600;
	private static final boolean ENDIAN = false;
	public static int writeIndex; // write buffer index

	private DatagramSocket sendSocket;
	private DatagramPacket sendPacket;
	InetAddress address = null;

	boolean stopaudioCapture = false;
	ByteArrayOutputStream byteOutputStream;
	AudioFormat adFormat;
	TargetDataLine targetDataLine;
	AudioInputStream InputStream;
	SourceDataLine sourceLine;
	private AudioInputStream audioInputStream;
	private byte buffer[] = new byte[AudioBuffer.AUDIO_BUFFER_SIZE + 6000];
	private AudioBuffer[] audioBuffers;

	public AudioServer(AudioBuffer[] audioBuffers) {
		debug.Debug.log("AudioSender : Create");
		this.audioBuffers = audioBuffers;
		adFormat = getAudioFormat();
		DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, adFormat);

		try {
			targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
			audioInputStream = new AudioInputStream(this.targetDataLine);
			targetDataLine.open(adFormat);
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private AudioFormat getAudioFormat() {
		return new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, AUDIO_SAMPLERATE, NUMBER_OF_BITS_IN_CHANNEL,
				NUMBER_OF_CHANNELS, NUMBER_OF_BYTES_IN_FRAME, NUMBER_OF_FRAME_PER_SECOND, ENDIAN);
	}

	public void run() {
		try {
			
			int readSize;
			targetDataLine.start();
			while (true) {
				if(ClientManager.clientCount != 0) {
					Server.control.serverControl();
				}
				readSize = audioInputStream.read(buffer, 0, FRAME_SIZE);
				debug.Debug.log("Audio Write	Index : " + writeIndex);
//				debug.Debug.log("Read size : " + readSize
//		                  + ", Remain data : " + targetDataLine.available() + "  writeIndex" + writeIndex);
				audioBuffers[writeIndex].setBuffer(buffer);
				writeIndex = (writeIndex + 1) % Server.BUFFER_SIZE;
				// debug.Debug.log("Source Write");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
