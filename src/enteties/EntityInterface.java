package enteties;

public interface EntityInterface {
	
	public void setOperation(int operation);
	
	public int getOperation();
	
	public Object[] getAllInObjects();
	
	public String[] getColumnNames();
	
	public String getTableName();

}
