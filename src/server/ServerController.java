package server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import enteties.Customer;
import enteties.Invoice;
import enteties.Product;

/**
 * Handles most of the logic between the server and the database.
 * 
 * @author Mattias Sundquist, Peter Folke
 *
 */
public class ServerController {
	public static final int AddCustomer = 1;
	public static final int GetCustomer = 2;
	public static final int GetAllCustomers = 3;
	public static final int UpdateCustomer = 4;

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
	public Object commandHandler(Object obj) {
		Customer customer = null;
		Invoice invoice = null;
		Product product = null;
		int command = 0;
		Object returnObj = null;
		
		//kontrollerar vilken typ av objekt som tas emot, castar objekt till rätt variabel och hämtar sedan command från det
		if (Customer.class.isInstance((Customer) obj)){
			customer = (Customer) obj;
			command = customer.getCommand();
		} else if (Invoice.class.isInstance((Invoice) obj)){
			invoice = (Invoice) obj;
//			com = invoice.getCom();
		} else if (Product.class.isInstance((Product) obj)){
			product = (Product) obj;
//			com = product.getCom();
		}
		
		//switchsatsen kollar vad kommandot är, skickar sedan objektet till rätt metod som returnerar det som skall
		//skickas tillbak till klienten
		switch ((int) command) {
			case AddCustomer:
				returnObj = addCustomer(customer);
				break;
			
			case GetCustomer:	
				returnObj = getCustomer(customer);
				break;
			
			case GetAllCustomers:
				returnObj = getAllCustomers();
				break;
			
			case UpdateCustomer:
				returnObj = updateCustomer(customer);
		}
		
		return returnObj;				
	}

	/**
	 * Objects are sent here by the commandHandler method if the first element in the object array represents adding a
	 * new customer to the database. The data in the Object is transformed into Strings which is then sent to the
	 * database.
	 * 
	 * @param data The data which is to be transformed and sent to the database.
	 */
	private Customer addCustomer(Customer c) {
		String query = "INSERT INTO customer (name, adress, zipCode, city, phoneNumber, email, organisationNumber, creditLimit) "
					+ "VALUES (\"" + c.getName() + "\",\"" + c.getAdress() + "\",\"" + c.getZipCode() + "\",\""
					+ c.getTown() + "\",\"" + c.getPhoneNumber() + "\",\"" + c.getEmail() + "\",\"" + c.getVatNumber()
					+ "\"," + 0 + ")";
		database.executeInsertDeleteQuery(query);
		return c;
	}


	//exempelmetod för att hämta en eller flera kunder ur databasen med hjälp av valfritt fält
	private ArrayList<Customer> getCustomer(Customer c){
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
		return createCustomerList(database.executeSelectQuery(query));
	}


	// hämtar alla kunder returnerar dem i en HashMap med customerId som nyckel
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
		return customerMap;
	}

	//test och exempel på en updatemetod som avnvänder PreparedStatement
	private Customer updateCustomer(Customer c){ 
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
		return c;
	}

	//genererar ett customer-objekt från den första raden i en ResultSet
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
	
	//genererar en lista med alla customer-objekt som finns i ResultSet 
	private ArrayList<Customer> createCustomerList(ResultSet rs){
		ArrayList<Customer> customerList = new ArrayList<Customer>();
		try {
			while (rs.next()) {
				customerList.add(new Customer(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4), 
						rs.getString(5), rs.getString(6), rs.getString(7), rs.getInt(8)));
			}			
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		return customerList;
	}	

}
