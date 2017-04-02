package klientServerDbDemo;
import java.io.IOException;
import java.util.Locale;

import javax.swing.JOptionPane;

public class Test extends Thread {

	public Test() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws InterruptedException {
		Object[] addCommand = {1, "Ica"}; //1 = kommando för att lägga till kund, ica är namn på kund
		Object[] getCommand = {2, 3}; //2 = kommando för att hämta namn på kund med ett visst id, 1 = id

		String name = "test";
		Client client = new Client(name, "127.0.0.1" , 1234);
		
		try {
			sleep(1000);
			client.messageSender(getCommand);
//			client.messageSender(addCommand);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
