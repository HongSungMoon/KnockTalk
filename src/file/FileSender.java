package file;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Vector;

import database.Database;
import debug.Debug;

public class FileSender {

	private Socket socket = null;
	private int startIndex;
	private int endIndex;
	private int index;
	private String [] arrIndex;
	private DataOutputStream dataOutputStream = null;
	private DataInputStream dataInputStream = null;
	private FileInputStream fileInputStream = null;
	private File file = null;
	private String filePath;
	private Database database = null;
	final private String START_PATH = "/home/pi/TestServer5/";
	final private String END_PATH = ".avi";
	public FileSender(Socket socket, String [] arrIndex, Database database, DataInputStream dataInputStream,
			DataOutputStream dataOutputStream) {
		this.socket = socket;
		this.database = database;
		this.dataOutputStream = dataOutputStream;
		this.dataInputStream = dataInputStream;
		this.arrIndex = arrIndex;
		sendRecent();
	}

	public FileSender(Socket socket, int index, Database database, DataInputStream dataInputStream,
			DataOutputStream dataOutputStream) {
		this.socket = socket;
		this.index = index;
		this.database = database;
		this.dataOutputStream = dataOutputStream;
		this.dataInputStream = dataInputStream;
		sendOne();
	}

	private void sendOne() {
		FileInfo fileInfo = database.get_file_Info(index);
		String filePath = setFilePath(fileInfo.getFile_name());
		Long fileSize = getFileSize(filePath);
		String sendMsg = Integer.toString(fileInfo.getFile_index()) + "," + fileInfo.getFile_name()  + ".avi,"
				+ fileSize;
		// infoBuffer = sendMsg.getBytes();
		debug.Debug.log(sendMsg + "  byte : " + sendMsg.getBytes().length);
		try {
			dataOutputStream.writeUTF(sendMsg);
			int byteSize = 1024;
			byte[] buffer = new byte[byteSize];
			initFileStream(filePath);
			try {
				fileInputStream = new FileInputStream(file);
			} catch (FileNotFoundException e1) {
				System.out.println("fileInputStream Error");
			}

			int n = 0;
			int count = 0;
			int check = 0;
			System.out.println("Sender Start");
			while (count < fileSize) {
				try {
					n = fileInputStream.read(buffer);
					dataOutputStream.write(buffer);
					count += n;
					check++;
					// System.out.println("Sending n : " + n + " count : " +
					// count + " check : " + check);
				} catch (IOException e) {
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String endMsg = "END";

		try {
			if (dataInputStream.readUTF().equals(endMsg)) {
				debug.Debug.log("Video Send Complete");
				dataOutputStream.close();
				dataInputStream.close();
				socket.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setPathName(String fileName) {
		filePath = setFilePath(fileName);
	}

	public String setFilePath(String fileName) {
		return START_PATH + fileName + END_PATH;
	}

	public void initFileStream(String filePath) {
		try {
			file = new File(filePath);
			fileInputStream = new FileInputStream(filePath);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Long getFileSize(String filePath) {
		file = new File(filePath);
		return file.length();
	}

	public void sendRecent() {

		for (int i = 0; i < arrIndex.length; i++) {
			// byte[] infoBuffer = new byte[5000];
			FileInfo fileInfo = database.get_file_Info(Integer.parseInt(arrIndex[i]));
			String filePath = setFilePath(fileInfo.getFile_name());
			Long fileSize = getFileSize(filePath);
			String sendMsg = Integer.toString(fileInfo.getFile_index()) + "," + fileInfo.getFile_name() + ".avi,"
					+ fileSize;
			// infoBuffer = sendMsg.getBytes();
			debug.Debug.log(sendMsg + "  byte : " + sendMsg.getBytes().length);
			try {
				dataOutputStream.writeUTF(sendMsg);
				int byteSize = 1024;
				byte[] buffer = new byte[byteSize];
				initFileStream(filePath);
				try {
					fileInputStream = new FileInputStream(file);
				} catch (FileNotFoundException e1) {
					System.out.println("fileInputStream Error");
				}

				int n = 0;
				int count = 0;
				int check = 0;
				System.out.println("Sender Start");
				while (count < fileSize) {
					try {
						n = fileInputStream.read(buffer);
						dataOutputStream.write(buffer);
						count += n;
						check++;
						// System.out.println("Sending n : " + n + " count : " +
						// count + " check : " + check);
					} catch (IOException e) {
					}
				}
				debug.Debug.log("Video Send    index : " + i);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		String endMsg = "END";

		try {
			if (dataInputStream.readUTF().equals(endMsg)) {
				debug.Debug.log("Video Send Complete");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
