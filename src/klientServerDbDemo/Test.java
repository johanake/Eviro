package klientServerDbDemo;
import java.io.IOException;
import java.util.Locale;

import javax.swing.JOptionPane;

public class Test extends Thread {

	public Test() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws InterruptedException {
		// Object[] addCommand = {1, "Kjell & Company"};
		// controller.getConnect().connectDB(addCommand); //lägger till en kund med namnet "Kjell & Company"
		String name = "test";
		Client client = new Client(name, "127.0.0.1" , 1234);
		
		Object[] getCommand = {2,1};
		try {
			sleep(1000);
			client.messageSender(getCommand);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
