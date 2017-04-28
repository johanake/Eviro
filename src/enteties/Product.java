package enteties;

import java.io.Serializable;

/**
 * A class which represents a product in the system
 * @author nadiaelhaddaoui
 */

public class Product implements Serializable, EntityInterface {
	private int productId;
	private String name;
	private String description;
	private double price;
	private String supplier;
	private String supplierArticleNumber;
	private int ean;
	private String stockPlace;
	private int saldo;
	private int operation;

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
	public Product(int productId, String name, String description, int price, String supplier, String supplierArticleNumber, int ean, String stockPlace, int balance) {
		this.productId = productId;
		this.name = name;
		this.description = description;
		this.price = price;
		this.supplier = supplier;
		this.supplierArticleNumber = supplierArticleNumber;
		this.ean = ean;
		this.stockPlace = stockPlace;
		this.saldo = balance;
	}

	@Override
	public void setOperation(int operation) {
		this.operation = operation;
	}

	@Override
	public int getOperation() {
		return operation;
	}

	@Override
	public Object[] getAllInObjects() {
		return new Object[] { productId, name, description, price, supplier, supplierArticleNumber, ean, stockPlace, saldo };
	}

}
