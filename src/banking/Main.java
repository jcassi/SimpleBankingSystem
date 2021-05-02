package banking;

import java.util.Scanner;

import banking.Bank;

public class Main {

    private static Bank bank;

    private static boolean isLoggedIn = false;
    private static boolean isExitSelected = false;

    public static void main(String[] args) {

        isLoggedIn = false;
        isExitSelected = false;

        Database database = new Database(getFileName(args));
        bank = new Bank(database);
        Scanner scanner = new Scanner(System.in);
        while (!isExitSelected) {
            if (isLoggedIn) {
                printLoggedInMenu();
            } else {
                printMainMenu();
            }

            int option = Integer.valueOf(scanner.nextLine());
            if (isLoggedIn) {
                LoggedInMenuOption(option);
            } else {
                MainMenuOption(option);
            }
        }
        bank.close();
    }

    private static  String getFileName(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-fileName")) {
                return args[i + 1];
            }
        }
        return "";
    }

    public static void printMainMenu() {
        System.out.printf("1. Create an account%n2.Log into account%n0.Exit%n");
    }

    public static void printLoggedInMenu() {

        System.out.printf("1. Balance%n2. Add income%n3. Do transfer%n4. Close Account%n5. Log out%n0. Exit%n");
    }

    public static void MainMenuOption(int option) {
        switch (option) {
            case 1:
                bank.createAccount();
                break;
            case 2:
                isLoggedIn = bank.logIn();
                break;
            case 0:
                isExitSelected = true;
                break;
        }

    }

    public static void LoggedInMenuOption(int option) {
        switch (option) {
            case 1:
                bank.printBalance();
                break;
            case 2:
                System.out.println("Enter income:");
                Scanner scanner = new Scanner(System.in);
                int amount = scanner.nextInt();
                bank.addIncome(amount);
                System.out.println("Income was added");
                break;
            case 3:
                bank.transfer();
                break;
            case 4:
                bank.closeAccount();
                break;
            case 5:
                bank.logOut();
                isLoggedIn = false;
                break;
            case 0:
                isExitSelected = true;
                break;
        }
    }
}