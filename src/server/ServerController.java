package server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import enteties.Customer;

/**
 * Handles most of the logic between the server and the database.
 * 
 * @author Mattias Sundquist, Peter Folke
 *
 */
public class ServerController {

	private ConnectDB database;

	/**
	 * Gets an instance of the ConnectDB class
	 */
	public ServerController() {
		database = new ConnectDB();
	}

	/**
	 * Handles objects coming from the server. Finds out what operation to perform based on the first element in the
	 * object array.
	 * 
	 * @param obj The object coming from the server.
	 */
	public void commandHandler(Object obj) {

		Object[] data = (Object[]) obj;
		switch ((int) data[0]) {
		case 1:
			addCustomer(data);
			break;
		}
	}

	/**
	 * Objects are sent here by the commandHandler method if the first element in the object array represents adding a
	 * new customer to the database. The data in the Object is transformed into Strings which is then sent to the
	 * database.
	 * 
	 * @param data The data which is to be transformed and sent to the database.
	 */
	private void addCustomer(Object[] data) {

		Customer c;
		if (data[1] instanceof Customer) {
			c = (Customer) data[1];

			String commandstring = c.toString();
			database.executeInsertDeleteQuery(commandstring);
		}

	}


	//exempelmetod för att hämta något ur databasen
	private Customer getCustomer(Customer c){

		String query = "SELECT * FROM customer WHERE ";
		if (c.getCustomerId() > 0){
			query += "customerId = " + c.getCustomerId();
		}	
		else if (c.getName() != null){
			query += "name = '" + c.getName() + "'";
		}	
		else if (c.getVatNumber() != 0){
			query += "organisationNumber = " + c.getVatNumber();
		}
		else if (c.getPhoneNumber() != null){
			query += "phoneNumber = '" + c.getPhoneNumber() + "'";
		}
		else if (c.getEmail() != null){
			query += "email = '" + c.getEmail() + "'";
		}
		c = createCustomerObject(database.executeSelectQuery(query));

		return c;
	}

	
	private HashMap<Integer, Customer> getAllCustomers(){
		
		HashMap<Integer, Customer> customerMap = new HashMap<Integer, Customer>();
		String query = "SELECT * FROM customer";
		ResultSet rs = database.executeSelectQuery(query);
		Customer c;
		try {
			while(rs.next()){
				c = createCustomerObject(rs);
				customerMap.put(c.getCustomerId(), c);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return  customerMap;
	}
	
	
	private void updateCustomer(Customer c){ 
		
		String query = "UPDATE customer SET name = ?, adress = ?, zipCode = ?, city = ?, phoneNumber = ?, email = ?, organisationNumber = ? WHERE customerId = ?";
		PreparedStatement ps = database.executeUpdateQuery(query);
		try {
			ps.setString(1, c.getName());
			ps.setString(2, c.getAdress());
			ps.setInt(3, c.getZipCode());
			ps.setString(4, c.getTown());
			ps.setString(5, c.getPhoneNumber());
			ps.setString(6, c.getEmail());
			ps.setInt(7, c.getVatNumber());
			ps.setInt(8, c.getCustomerId());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	//generera ett customer-objekt
	private Customer createCustomerObject(ResultSet rs){
		
		Customer c = null;
		try {
			if (rs.isBeforeFirst()){
				rs.next();
			}
			c = new Customer(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4), 
					rs.getString(5), rs.getString(6), rs.getString(7), rs.getInt(8));
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		return c;
	}

	//exempel på hur commandHandler kan skicka tillbaka data
	public void commandHandler(Object obj, ObjectOutputStream oos) {
		Object[] data = (Object[]) obj;
		
		switch ((int) data[0]) {
		case 1:
			addCustomer(data);
			break;
		case 2:	
			try {
				oos.writeObject(getCustomer((Customer) data[1]));
				oos.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case 3:
			try {
				oos.writeObject(getAllCustomers());
				oos.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case 4:
			updateCustomer((Customer) data[1]);
		}				
	}

}
