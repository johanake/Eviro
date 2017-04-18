package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import enteties.Customer;

public class Test extends Thread{
	private Socket socket;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	
	public Test() {
		try {
			socket = new Socket("localhost", 1234);
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
			Object[] msg = {2, new Customer(0, null, null, 0, null, null, "work@robinovergaard.com", 0)};
			oos.writeObject(msg);
			oos.flush();
			Customer c = (Customer) ois.readObject();
			System.out.println(c.getCustomerId() + " " + c.getName() + " " + c.getAdress() + " " + c.getEmail());
			
			Thread.sleep(1000);
			
			Object[] msg2 = {3};
			oos.writeObject(msg2);
			oos.flush();
			HashMap<Integer, Customer> customerMap = (HashMap<Integer, Customer>) ois.readObject();
			Customer c2 = customerMap.get(1);
			System.out.println(c2.getCustomerId() + c2.getName());
			
			c2.setVatNumber(1234567);
			Object[] msg3 = {4,c2};
			oos.writeObject(msg3);
			oos.flush();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new Server(1234);
		new Test();
	}

}
