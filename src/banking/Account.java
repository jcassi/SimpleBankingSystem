package banking;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Account {
    private String cardNumber;
    private String pin;
    private int balance;

    public Account() {
        cardNumber = generateCardNumber();
        pin = generatePin();
        balance = 0;
    }

    public Account(String cardNumber, String pin, int balance){
        this.cardNumber = cardNumber;
        this.pin = pin;
        this.balance = balance;
    }

    private String generateCardNumber() {
        String cardNumber = "400000";
        int digits = 9;
        long min = (long) Math.pow(digits, digits - 1);
        long accIdentifier =  ThreadLocalRandom.current().nextLong(min, min * digits);
        cardNumber = cardNumber.concat(String.format("%09d", accIdentifier));
        char luhnDigit = Luhn.getLuhnDigit(cardNumber);
        cardNumber = cardNumber.concat(String.valueOf(luhnDigit));
        return cardNumber;
    }

    private String generatePin() {
        int accIdentifier =  ThreadLocalRandom.current().nextInt(0, 10000);
        return String.format("%04d", accIdentifier);
    }

    public int getBalance() {
        return balance;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getPin() {
        return pin;
    }

    public void addIncome(int amount) {
        if (amount < 0) {
            throw  new IllegalArgumentException();
        }
        this.balance += amount;
    }
}
