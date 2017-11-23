package file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Vector;

import database.Database;

public class FileManager extends Thread {

	private Socket socket = null;
	private DataInputStream dataInputStream = null;
	private DataOutputStream dataOutputStream = null;
	private FileSender fileSender = null;
	private FileInfoSender fileInfoSender = null;

	public FileManager(Socket socket) {
		this.socket = socket;
		try {
			dataInputStream = new DataInputStream(socket.getInputStream());
			dataOutputStream = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			debug.Debug.log("Buffer Init Failed");
		}
		// fileCheck = new FileCheck();
	}

	public void run() {
		try {
			String msg = dataInputStream.readUTF();
			if (msg.equals("GETT")) {
				Database database = new Database();
				int max_index = database.get_index_number();
				dataOutputStream.writeInt(max_index);
				String stringIndex = dataInputStream.readUTF();
				debug.Debug.log("client wanted index : " + stringIndex);
				String []arrIndex = stringIndex.split(",");
				FileSender fileSender = new FileSender(socket, arrIndex, database, dataInputStream, dataOutputStream);
//				int endIndex = database.get_index_number();
//				int startIndex = getStartIndex(index, endIndex);
//				if (index != endIndex) {
//					dataOutputStream.writeInt(endIndex - startIndex + 1);
//					debug.Debug
//							.log("startIndex : " + startIndex + "    endIndex = " + endIndex + "     fileSenderStart");
//					fileSender = new FileSender(socket, startIndex, endIndex, database, dataInputStream,
//							dataOutputStream);	
//				} else {
//					dataOutputStream.writeInt(0);
//					debug.Debug.log("startIndex = endIndex");
//				}
			} else if (msg.equals("GETO")) {
				int index = dataInputStream.readInt();
				debug.Debug.log("client wanted index : " + index);
				Database database = new Database();
				fileSender = new FileSender(socket, index, database, dataInputStream, dataOutputStream);
			} else if (msg.equals("GETI")) {
				// int startIndex = dataInputStream.readInt();
				Database database = new Database();
				fileInfoSender = new FileInfoSender(socket, database, dataInputStream, dataOutputStream);
			}

		} catch (IOException e) {
			debug.Debug.log("Get Client Info Failed");
		}
		try {
			dataOutputStream.close();
			dataInputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private int getStartIndex(int index, int endIndex) {
		if (endIndex - index > 10)
			return endIndex - 9;
		return index + 1;
	}

}
