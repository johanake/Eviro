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
	public static final int AddCustomer = 1;
	public static final int GetCustomer = 2;
	public static final int GetAllCustomers = 3;
	public static final int UpdateCustomer = 4;
	
	public Test() {
		try {
			socket = new Socket("localhost", 1234);
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
			
			//Hämtar alla kunder med email work@robinovergaard.com
			Customer c1 = new Customer(0, null, null, 0, null, null, "work@robinovergaard.com", 0);
			c1.setCommand(GetCustomer);
			oos.writeObject(c1);
			oos.flush();
			
			ArrayList<Customer> customerList = ((ArrayList<Customer>) ois.readObject());
			for (Customer c: customerList){
				System.out.println("Från ArrayList genom GetCustomer: " + c.getCustomerId() + " " + c.getName() + " " + c.getAdress() + " " + c.getEmail());
			}
			
			Thread.sleep(2000);
			
			//Hämtar alla kunder
			Customer c2 = new Customer(0, null, null, 0, null, null, null, 0);
			c2.setCommand(GetAllCustomers);
			oos.writeObject(c2);
			oos.flush();
			HashMap<Integer, Customer> customerMap = (HashMap<Integer, Customer>) ois.readObject();
			for (Customer c : customerMap.values()){
				System.out.println("Från HashMap genom GetAllCustomers: " + c.getCustomerId() + " " + c.getName() + " " + c.getAdress() + " " + c.getEmail());
			}
			
			Thread.sleep(2000);
			
			//Updaterar organisationsnummer för customer med id 1
			Customer c3 = customerMap.get(1);
			c3.setVatNumber((int) (1000000*Math.random()));
			c3.setCommand(UpdateCustomer);
			oos.writeObject(c3);
			oos.flush();
			c3 = (Customer) ois.readObject();
			System.out.println("Uppdaterat vatNumber genom UpdateCustomers: " + c3.getCustomerId() + " " + c3.getName() + " " + c3.getAdress() + " " + c3.getEmail() + " "  + c3.getVatNumber());
		

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new Server(1234);
		new Test();
	}

}
