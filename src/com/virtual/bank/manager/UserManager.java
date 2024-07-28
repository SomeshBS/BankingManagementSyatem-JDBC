package com.virtual.bank.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import com.virtual.bank.model.User;

public class UserManager {
	User user = new User();
	private Connection connection;
	private Scanner scanner;
	
	public UserManager(Connection connection, Scanner scanner) {
		super();
		this.connection = connection;
		this.scanner = scanner;
	}
	
	public void userRegister() {
		scanner.nextLine();
		System.out.print("Enter full Name : ");
		user.setFull_name(scanner.nextLine());
		
		System.out.print("Enter Email Address : ");
		user.setEmail(scanner.nextLine());
		
		System.out.print("Enter password Address : ");
		user.setPassword(scanner.nextLine());
		
		if(userExists(user.getEmail())) {
			System.out.println("ERROR : USER WITH EMAIL ID ALREADY EXISTS");
		}
		
		String register_query = "INSERT INTO user(full_name, email, password) VALUES(?,?,?)";
		
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(register_query);
			preparedStatement.setString(1, user.getFull_name());
			preparedStatement.setString(2, user.getEmail());
			preparedStatement.setString(3, user.getPassword());
			int affectedRows = preparedStatement.executeUpdate();
			if(affectedRows>0) {
				System.out.println("USER REGISTERED SUCCESSFULLY :)");
			}else {
				System.out.println("ERROR : USER REGISTERED Failed :( ");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public String userLogin() {
		scanner.nextLine();
		
		System.out.print("Enter Email Address : ");
		user.setEmail(scanner.nextLine());
		
		System.out.print("Enter password Address : ");
		user.setPassword(scanner.nextLine());
		
		String login_query = "SELECT * FROM user WHERE email = ? AND password=?";
		
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(login_query);
			preparedStatement.setString(1, user.getEmail());
			preparedStatement.setString(2, user.getPassword());
			ResultSet resultSet = preparedStatement.executeQuery();
			if(resultSet.next()) {
				return user.getEmail();
			}else {
				return null;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	public boolean userExists(String email) {
		String userExists_query = "SELECT email FROM user WHERE email =?";
		PreparedStatement preparedStatement;
		try {
			preparedStatement = connection.prepareStatement(userExists_query);
			preparedStatement.setString(1, email);
			
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				return true;
			}else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
		
	}
	
	
	
	
	
	
}
