package com.virtual.bank.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import javax.security.auth.login.AccountNotFoundException;

import com.virtual.bank.customexceptions.AccountCreationException;
import com.virtual.bank.model.Account;
import com.virtual.bank.model.User;

public class AccountManager {

	User user = new User();
	Account account = new Account();
	
	private Connection connection;
	private Scanner scanner;
	
	
	
	public AccountManager(Connection connection, Scanner scanner) {
		super();
		this.connection = connection;
		this.scanner = scanner;
	}

	public long generateAccountNumber() {
		Statement statement;
		try {
			statement = connection.createStatement();
			 ResultSet resultSet = statement.executeQuery("SELECT account_number from Accounts ORDER BY account_number DESC LIMIT 1");
			 if(resultSet.next()) {
				 long lastAccNo = resultSet.getLong("account_number");
				 return lastAccNo+1;
			 }else {
				 return 100100100;
			 }
		} catch (SQLException e) {
			e.printStackTrace(); 
		}
      return 100100100;
	}
	
	public boolean accountExists(String email) {
		String accountExists_query = "SELECT account_number FROM accounts WHERE email =?";
		PreparedStatement preparedStatement;
		try {
			preparedStatement = connection.prepareStatement(accountExists_query);
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
	
	public long openAccount(String email) throws AccountCreationException {
		if(!accountExists(email)) {
			String openAccount_query = "INSERT INTO Accounts(account_number, full_name, email, balance, security_pin) VALUES(?, ?, ?, ?, ?)";
			scanner.nextLine();
			System.out.print("Enter full Name : ");
			account.setFull_name(scanner.nextLine());
			System.out.print("Enter balance : ");
			account.setbalance(scanner.nextDouble());
			scanner.nextLine();
			System.out.print("Enter Security Pin : ");
			account.setSecurity_pin(scanner.nextLine());
			account.setAccount_number(generateAccountNumber());
			//account.setEmail(user.getEmail());
			try {
				PreparedStatement preparedStatement = connection.prepareStatement(openAccount_query);
				preparedStatement.setLong(1, account.getAccount_number());
				preparedStatement.setString(2, account.getFull_name());
				//preparedStatement.setString(3, account.getEmail());
				preparedStatement.setString(3, email);
				preparedStatement.setDouble(4, account.getbalance());
				preparedStatement.setString(5, account.getSecurity_pin());
				int affectedRows = preparedStatement.executeUpdate();
				if(affectedRows>0) {
					System.out.println("ACCOUNT CREATED SUCCESSFULLY");
					return account.getAccount_number();
				}else {
					System.out.println();
					throw new AccountCreationException("ACCOUNT CREATION FAILED");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		throw new AccountCreationException("ACCOUNT CREATION FAILED");
	}
	
    public long getAccount_number(String email) {
        String getAccount_number_query = "SELECT account_number from Accounts WHERE email = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(getAccount_number_query);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return resultSet.getLong("account_number");
            }else {
            	throw new RuntimeException("ACCOUNT NOT FOUND") ;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        throw new RuntimeException("ACCOUNT NOT FOUND") ;
    }
	
}
