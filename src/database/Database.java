package database;

import java.security.interfaces.RSAKey;
import java.sql.*;
import java.util.*;

import file.FileInfo;


public class Database {
	Connection conn = null;
	PreparedStatement pstmt = null;
	
	String jdbc_driver = "com.mysql.jdbc.Driver";
	String jdbc_url = "jdbc:mysql://localhost/knock_talk"; 
	
	void connect() {
		try {
			Class.forName(jdbc_driver);
			conn = DriverManager.getConnection(jdbc_url,"root","1234");
			debug.Debug.log("Database Connection");	
			System.out.println("Database Connection");
		} catch (Exception e) {
			e.printStackTrace();
			debug.Debug.log("Database Connection Fail");	
		}
	}
	
	void disconnect() {
		if(pstmt != null) {
			try {
				pstmt.close();
				debug.Debug.log("Database close");	
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} 
		if(conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public boolean insertDB(String fileName) {
		
		connect();
		
		String sql ="insert into file(file_name) values(?)";
		
		try {
			pstmt =  conn.prepareStatement(sql);

			pstmt.setString(1, fileName);
			pstmt.executeUpdate();
			debug.Debug.log("insert Database");
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		finally {
			disconnect();
		}
		return true;
	}

	public int get_index_number() { 
		connect();
		String sql = "select * from file;";
		int index_number =0;
		try {
			pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			rs.last();
			String num= rs.getString("file_index");
			index_number = Integer.parseInt(num);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return index_number;
	}
	
	public FileInfo get_file_Info(int index) {
		
		FileInfo fileInfo = new FileInfo();
		connect();
		String sql = "select * from file where file_index = ?";
		String a = Integer.toString(index);
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,a);
			ResultSet rs = pstmt.executeQuery();
			rs.next();
			fileInfo.setFile_index(Integer.parseInt(rs.getString("file_index")));
			fileInfo.setFile_name(rs.getString("file_name"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fileInfo;
		
	}
	
	public Vector<FileInfo> get_all_file_Info() {
		connect();
		String sql = "select * from file order by file_index asc;";
		Vector<FileInfo> infos = new Vector<FileInfo>();
		try {
			pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				String fileName = rs.getString("file_name");
				int fileIndex = rs.getInt("file_index");
				FileInfo fileInfo = new FileInfo(fileIndex, fileName);
				infos.add(fileInfo);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return infos;
	}

}
