package threadControl;

import server.Deliver;

public class ThreadControl {
	
	public ThreadControl() {
		
	}
	
	synchronized public void clientControl(Deliver deliver) {
		notifyAll();
		try {
			wait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			deliver.remove();
		}
	}
	
	synchronized public void serverControl() {
		notifyAll();
		try {
			wait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
