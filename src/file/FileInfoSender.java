package file;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Vector;

import database.Database;

public class FileInfoSender {
	
	private Socket socket;
	private int sendIndex;
	private Database database;
	private DataOutputStream dataOutputStream;
	private DataInputStream dataInputStream;
	Vector<FileInfo> infos = new Vector<FileInfo>();
	
	public FileInfoSender(Socket socket, Database database, DataInputStream dataInputStream, DataOutputStream dataOutputStream) {
		this.socket = socket;
		this.database = database;
		this.dataOutputStream = dataOutputStream;
		this.dataInputStream = dataInputStream;
		sendIndex = database.get_index_number();
		getInfoVector();
		sendInfos();
	}
	
	public void getInfoVector() {
		infos = database.get_all_file_Info();
		for(int i=0; i<infos.size(); i++) {
			System.out.println("file Index = " + infos.get(i).getFile_index() + "   file_name = " + infos.get(i).getFile_name());
		}
	}
	
	public void sendInfos() {
		try {
			dataOutputStream.writeInt(sendIndex);
			String info;
			for(int i=0; i<sendIndex; i++) {
				FileInfo fileInfo = infos.get(i);
				info = Integer.toString(fileInfo.getFile_index()) + "," + fileInfo.getFile_name();
				debug.Debug.log("SendInfos i = " + i + "info = " + info);
				dataOutputStream.writeUTF(info);
			}
			String endMsg = dataInputStream.readUTF();
			if(endMsg.equals("END")) {
				debug.Debug.log("Send Infos Complete");
				dataOutputStream.close();
				dataInputStream.close();
				socket.close();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
