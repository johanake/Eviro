package server;

import org.jasypt.util.password.StrongPasswordEncryptor;
import org.jasypt.util.text.BasicTextEncryptor;
import org.jasypt.util.text.StrongTextEncryptor;

import java.io.*;
import java.util.Properties;

import javax.swing.JOptionPane;
public class enryptTest {

	public enryptTest() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws IOException {
//		Properties properties = new Properties();
//		FileReader reader = new FileReader("config");
//		properties.load(reader);
		StrongPasswordEncryptor pcryptor = new StrongPasswordEncryptor();
		BasicTextEncryptor tcryptor = new BasicTextEncryptor();
		tcryptor.setPassword("eviroadmin");
		System.out.println(pcryptor.encryptPassword("password"));


	}

}
