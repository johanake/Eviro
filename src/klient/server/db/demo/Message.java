package klient.server.db.demo;
import java.io.Serializable;

import javax.swing.ImageIcon;

public class Message implements Serializable {
	private static final long serialVersionUID = -7522116645588027727L;
	private String mReciever;
	private String mSender;
	private String mMessage;
	private ImageIcon mImage;
	
	public Message(String reciever, String sender, String message) {
		this(reciever, sender, message, null);
	}
	
	public Message(String reciever, String sender, ImageIcon image) {
		this(reciever, sender, null, image);
	}
	
	public Message(String reciever, String sender, String message, ImageIcon image) {
		mReciever = reciever;
		mSender = sender;
		mMessage = message;
		mImage = image;
	}

	public String getmReciever() {
		return mReciever;
	}

	public void setmReciever(String mReciever) {
		this.mReciever = mReciever;
	}

	public String getmSender() {
		return mSender;
	}

	public void setmSender(String mSender) {
		this.mSender = mSender;
	}

	public String getmMessage() {
		return mMessage;
	}

	public void setmMessage(String mMessage) {
		this.mMessage = mMessage;
	}

	public ImageIcon getmImage() {
		return mImage;
	}

	public void setmImage(ImageIcon mImage) {
		this.mImage = mImage;
	}

}
