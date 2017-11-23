package file;

public class FileInfo {
	
	private String file_name;
	private int file_index;
	
	public FileInfo() {
		
	}
	
	public FileInfo(int file_index, String file_name){
		this.file_name = file_name;
		this.file_index = file_index;
	}
	
	public String getFile_name() {
		return file_name;
	}

	
	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}

	
	public void setFile_index(int file_index) {
		this.file_index = file_index;
	}
	
	public int getFile_index() {
		return file_index;
	}
	
}
