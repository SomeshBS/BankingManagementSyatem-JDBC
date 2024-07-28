package com.virtual.bank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

import com.virtual.bank.customexceptions.AccountCreationException;
import com.virtual.bank.customexceptions.AccountNotFoundException;
import com.virtual.bank.customexceptions.InsufficientBalanceException;
import com.virtual.bank.customexceptions.InvalidSecurityPinException;
import com.virtual.bank.customexceptions.TransactionFailedException;
import com.virtual.bank.manager.AccountManager;
import com.virtual.bank.manager.TransactionManager;
import com.virtual.bank.manager.UserManager;

public class VirtualBank {
	private static final String url = "jdbc:mysql://localhost:3306/banking_system";
	private static final String username = "root";
	private static final String password = "Somesh@268";

	public static void main(String[] args) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		try {
			Connection connection = DriverManager.getConnection(url, username, password);
			Scanner scanner = new Scanner(System.in);
			UserManager userManager = new UserManager(connection, scanner);
			AccountManager accountManager = new AccountManager(connection, scanner);
			TransactionManager transactionManager = new TransactionManager(connection, scanner);

			String email;
			long account_number;

			while (true) {
				System.out.println("*** WELCOME TO VIRTUAL BANK ***");
				System.out.println();
				System.out.println("1. Register");
				System.out.println("2. Login");
				System.out.println("3. Exit");
				System.out.println("Enter your choice: ");
				int choice1 = scanner.nextInt();
				switch (choice1) {
				case 1:
					userManager.userRegister();
					break;
				case 2:
					email = userManager.userLogin();
					if (email != null) {
						System.out.println();
						System.out.println("User Logged IN successfully");
						if (!accountManager.accountExists(email)) {
							System.out.println();
							System.out.println("1. Open new bank account");
							System.out.println("2. Exit");
							if (scanner.nextInt() == 1) {
								try {
									account_number = accountManager.openAccount(email);
									System.out.println("Account Created Successfully");
									System.out.println("Your Account Number is: " + account_number);
								} catch (AccountCreationException e) {
									System.out.println("Account creation failed");
								}

							} else {
								break;
							}
						}
						account_number = accountManager.getAccount_number(email);
						int choice2 = 0;
						while (choice2 != 5) {
							System.out.println();
							System.out.println("1. Debit Money");
							System.out.println("2. Credit Money");
							System.out.println("3. Transfer Money");
							System.out.println("4. Check Balance");
							System.out.println("5. Log Out");
							System.out.println("Enter your choice: ");
							choice2 = scanner.nextInt();
							switch (choice2) {
							case 1:
								try {
									transactionManager.debitMoney(account_number);
								} catch (InsufficientBalanceException | TransactionFailedException e) {
									e.printStackTrace();
								}
								break;
							case 2 : 
								transactionManager.creditMoney(account_number);
								break;
							case 3: 
								try {
									transactionManager.transferMoney(account_number);
								} catch (InsufficientBalanceException | InvalidSecurityPinException
										| AccountNotFoundException | TransactionFailedException e) {
									e.printStackTrace();
								}
								break;
							case 4: 
								transactionManager.getBalance(account_number);
								break;
							case 5 :
								 break;
                            default:
                                System.out.println("Enter Valid Choice!");
                                break;
							}
						}
					}

				case 3:
					System.out.println("THANK YOU FOR VISITING VIRTUAL BANK!!!");
					System.out.println("Exiting System!");
					return;
				default:
					System.out.println("Enter Valid Choice");
					break;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
}
