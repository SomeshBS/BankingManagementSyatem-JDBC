package com.virtual.bank.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import com.virtual.bank.customexceptions.AccountNotFoundException;
import com.virtual.bank.customexceptions.InsufficientBalanceException;
import com.virtual.bank.customexceptions.InvalidSecurityPinException;
import com.virtual.bank.customexceptions.TransactionFailedException;
import com.virtual.bank.model.Account;

public class TransactionManager extends Account {

	private Connection connection;
	private Scanner scanner;

	public TransactionManager(Connection connection, Scanner scanner) {
		super();
		this.connection = connection;
		this.scanner = scanner;
	}
	
	public void getBalance(long account_number){
        scanner.nextLine();
        System.out.print("Enter Security Pin: ");
        String security_pin = scanner.nextLine();
        try{
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT balance FROM accounts WHERE account_number = ? AND security_pin = ?");
            preparedStatement.setLong(1, account_number);
            preparedStatement.setString(2, security_pin);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                double balance = resultSet.getDouble("balance");
                System.out.println("Your Current Balance: "+balance);
            }else{
                System.out.println("Invalid Pin!");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

	public void creditMoney(long account_number) throws SQLException {
		scanner.nextLine();
		System.out.print("Enter Amount umber : ");
		double amount = scanner.nextDouble();
		scanner.nextLine();
		System.out.print("Enter Seurity Pin : ");
		String secPin = scanner.nextLine();

		connection.setAutoCommit(false);
		if (account_number != 0) {
			PreparedStatement preparedStatement = connection
					.prepareStatement("SELECT * FROM accounts WHERE account_number =? AND security_pin=?");
			preparedStatement.setLong(1, account_number);
			preparedStatement.setString(2, secPin);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				PreparedStatement preparedStatement1 = connection
						.prepareStatement("UPDATE accounts SET balance=balance+? WHERE account_number=?");
				preparedStatement1.setDouble(1, amount);
				preparedStatement1.setLong(2, account_number);
				int affectedRows = preparedStatement1.executeUpdate();
				if (affectedRows > 0) {
					System.out.println("RS." + amount + " CREDITED SUCCESSFULLY :)");
					connection.commit();
					connection.setAutoCommit(true);
				} else {
					System.out.println("TRANSACTION FAILED :(");
					connection.rollback();
					connection.setAutoCommit(true);
				}
			} else {
				System.out.println("ERROR : INVALID SECURITY PIN");
			}
			connection.setAutoCommit(true);
		}
	}

	public void debitMoney(long account_number) throws TransactionFailedException, InsufficientBalanceException, SQLException {
		scanner.nextLine();
		System.out.print("Enter Amount umber : ");
		double amount = scanner.nextDouble();
		scanner.nextLine();
		System.out.print("Enter Seurity Pin : ");
		String secPin = scanner.nextLine();

		try {
			connection.setAutoCommit(false);
			if (account_number != 0) {
				PreparedStatement preparedStatement = connection
						.prepareStatement("SELECT * FROM accounts WHERE account_number =? AND security_pin=?");
				preparedStatement.setLong(1, account_number);
				preparedStatement.setString(2, secPin);
				ResultSet resultSet = preparedStatement.executeQuery();
				if (resultSet.next()) {
					double currentbalance = resultSet.getDouble("balance");
					if (amount <= currentbalance) {
						PreparedStatement preparedStatement1 = connection
								.prepareStatement("UPDATE accounts SET balance=balance-? WHERE account_number=?");
						preparedStatement1.setDouble(1, amount);
						preparedStatement1.setLong(2, account_number);
						int affectedRows = preparedStatement1.executeUpdate();
						if (affectedRows > 0) {
							System.out.println("RS." + amount + " DEBITED SUCCESSFULLY :)");
							connection.commit();
							connection.setAutoCommit(true);
						} else {
							connection.rollback();
							connection.setAutoCommit(true);
							throw new TransactionFailedException("Transaction Failed");
						}
					} else {
						throw new InsufficientBalanceException("INSUFFICIENT balance");
					}
				} else {
					System.out.println("ERROR : INVALID SECURITY PIN");
				}
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		connection.setAutoCommit(true);
	}
	
	public void transferMoney(long sender_account_number) throws TransactionFailedException, 
	InsufficientBalanceException, InvalidSecurityPinException, AccountNotFoundException, SQLException {
		scanner.nextLine();
		System.out.print("Enter the reciver's Account No : ");
		long reciver_account_number = scanner.nextLong();
		System.out.print("Enter the Amount you wnat to transfer : ");
		double amount = scanner.nextDouble();
		scanner.nextLine();
		System.out.print("Enter the Security PIN : ");
		String security_pin = scanner.nextLine();
		
		try {
			connection.setAutoCommit(false);
			if(sender_account_number!=0 && reciver_account_number!=0) {
				PreparedStatement preparedStatement = connection.
						prepareStatement("SELECT * FROM accounts WHERE account_number = ? AND security_pin=?");
				preparedStatement.setLong(1, sender_account_number);
				preparedStatement.setString(2, security_pin);
				ResultSet resultSet = preparedStatement.executeQuery();
				if(resultSet.next()) {
					double current_balance= resultSet.getDouble("balance");
					if(amount<=current_balance) {
						PreparedStatement debitPreparedStatement = connection.prepareStatement("UPDATE accounts SET balance=balance-? WHERE account_number=?");
						PreparedStatement creditPreparedStatement = connection.prepareStatement("UPDATE accounts SET balance=balance+? WHERE account_number=?");
						
						debitPreparedStatement.setDouble(1, amount);
						debitPreparedStatement.setLong(2, sender_account_number);
						creditPreparedStatement.setDouble(1, amount);
						creditPreparedStatement.setLong(2, reciver_account_number);
						
						int debitAffectedRows = debitPreparedStatement.executeUpdate();
						int creditAffetedRows = creditPreparedStatement.executeUpdate();
						
						if(debitAffectedRows>0 && creditAffetedRows>0) {
							System.out.println("RS."+amount+" TRANSFERED SUCCESSFULLY :) ");
							connection.commit();
							connection.setAutoCommit(true);
						}else {
							connection.rollback();
							connection.setAutoCommit(true);
							throw new TransactionFailedException("Transction Failed");
							
						}
					}else {
						throw new InsufficientBalanceException("Insufficient Amount");
					}
				}else {
					throw new InvalidSecurityPinException("Invalid Security PIN");
				}
			}else {
				throw new AccountNotFoundException("Account not Found");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		connection.setAutoCommit(true);
	}
	
}
