package enteties;

import java.io.Serializable;

public class ChatMessage implements Serializable, Entity {

	private int operation;
	private Object[] data;
	
	public ChatMessage() {
	}
	
	public ChatMessage(Object[] data) {
		this.data = data;
	}

	public void setOperation(int operation) {
		this.operation = operation;
	}

	public int getOperation() {
		return operation;
	}

	public void setData(Object[] data) {

		this.data = data;
		
	}

	public Object[] getData() {
		return data;
	}
	
	

}
