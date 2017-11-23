package servo;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ShellCommander {
	
	public void openDoor() throws Exception {
		
		String command = "python /home/pi/TestServer5/src/servo/servo.py";
		shellCmd(command);
		
	}

	public void shellCmd(String command) throws Exception {
		
		Runtime runtime = Runtime.getRuntime();
		Process process = runtime.exec(command);
		InputStream is = process.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		String line;
		while((line = br.readLine()) != null)
			System.out.println(line);

	}
}
