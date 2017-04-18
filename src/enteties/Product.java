package enteties;
import java.io.Serializable;

/**
 * A class which represents a product in the system
 * @author nadiaelhaddaoui
 *
 */

public class Product implements Serializable{
	private int productId;
	private String name;
	private String description;
	private double price;
	private String supplier;
	private String supplierArticleNumber;
	private int ean;
	private String stockPlace;
	private int balance;
	
	/**
	 * Product constructor
	 * @param productId A unique Id
	 * @param name The name of this product
	 * @param description The description of this product
	 * @param price The price of this product
	 * @param supplier The products supplier
	 * @param supplierArticleNumber The suppliers article number
	 * @param ean The products barcode
	 * @param stockPlace Where the product is to be stocked
	 * @param balance The amount of products
	 */
	public Product(int productId, String name, String description, int price, String supplier,
			String supplierArticleNumber, int ean, String stockPlace, int balance) {
		this.productId = productId;
		this.name = name;
		this.description = description;
		this.price = price;
		this.supplier = supplier;
		this.supplierArticleNumber = supplierArticleNumber;
		this.ean = ean;
		this.stockPlace = stockPlace;
		this.balance = balance;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getSupplier() {
		return supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	public String getSupplierArticleNumber() {
		return supplierArticleNumber;
	}

	public void setSupplierArticleNumber(String supplierArticleNumber) {
		this.supplierArticleNumber = supplierArticleNumber;
	}

	public int getEan() {
		return ean;
	}

	public void setEan(int ean) {
		this.ean = ean;
	}

	public String getStockPlace() {
		return stockPlace;
	}

	public void setStockPlace(String stockPlace) {
		this.stockPlace = stockPlace;
	}

	public int getBalance() {
		return balance;
	}

	public void setBalance(int balance) {
		this.balance = balance;
	}
	

}
