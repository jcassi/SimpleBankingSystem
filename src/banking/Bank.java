package banking;

import javax.xml.crypto.Data;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Bank {

    private List<Account> accounts;
    private Account loggedInAccount;
    private Database database;

    public Bank(Database database) {

        accounts = new ArrayList<Account>();
        this.database = database;
    }

    public void close() {

        database.close();
    }

    public void createAccount() {
        Account account = new Account();
        accounts.add(account);
        System.out.println("Your card has been created");
        System.out.printf("Your card number:%n%s%n", account.getCardNumber());
        System.out.printf("Your card PIN:%n%s%n", account.getPin());
        this.database.addAccount(account);
    }

    public boolean logIn() {
        String cardNumber = requestCardNumber();

        String pin = requestPin();

        Account account = getAccountByCardNumber(cardNumber);
        if (account != null && account.getPin().equals(pin)) {
            this.loggedInAccount = account;
            System.out.println("You have successfully logged in!");
            return true;
        } else {
            System.out.println("Wrong card number or PIN!");
            return false;
        }
    }

    public void logOut() {
        loggedInAccount = null;
    }

    public void printBalance() {
        System.out.println("Balance: " + loggedInAccount.getBalance());
    }

    private Account getAccountByCardNumber(String cardNumber) {
        Account account = null;
        account = database.getAccount(cardNumber);
        return account;
    }

    public void addIncome(int amount) {
        if (amount < 0) {
            throw  new IllegalArgumentException();
        }
        loggedInAccount.addIncome(amount);
        database.setBalance(loggedInAccount.getCardNumber(), loggedInAccount.getBalance());
    }

    public void transfer() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter card number:");
        String destinationCardNumber =  scanner.nextLine();

        Account destinationAccount  = database.getAccount(destinationCardNumber);
        if (destinationCardNumber.equals(loggedInAccount.getCardNumber())) {
            System.out.println("You can't transfer money to the same account!");
            return;
        } else if (!Luhn.checkSum(destinationCardNumber)) {
            System.out.println("Probably you made a mistake in the card number. Please try again!");
            return;
        } else if (destinationAccount == null) {
            System.out.println("Such a card does not exist.");
            return;
        }
        System.out.println("Enter how much money you want to transfer:");
        int amount =  Integer.valueOf(scanner.nextLine());
        if (amount > loggedInAccount.getBalance()) {
            System.out.println("Not enough money!");
            return;
        } else {
            database.transfer(loggedInAccount, amount, destinationAccount);
        }

    }

    public void closeAccount() {
        this.database.closeAccount(this.loggedInAccount.getCardNumber());

    }

    private String requestCardNumber() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your card number:");
        return scanner.nextLine();
    }

    private String requestPin() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your PIN:");
        return String.format("%04d", scanner.nextInt());
    }

}
