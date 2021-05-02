package banking;

import org.sqlite.SQLiteDataSource;

import javax.swing.plaf.nimbus.State;
import java.sql.*;

public class Database {

    private Connection connection;
    private Statement statement;
    private boolean connectedToDatabase;

    public Database(String fileName) {
        String url = "jdbc:sqlite:" + fileName;
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);

        try {
            this.connection = dataSource.getConnection();
            this.statement = connection.createStatement();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS card(" +
                    "id INTEGER," +
                    "number TEXT," +
                    "pin TEXT," +
                    "balance INTEGER DEFAULT 0)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        this.connectedToDatabase = true;
    }

    public void close() {
        if ( connectedToDatabase) {
            try {
                statement.close();
                connection.close();
                connectedToDatabase = false;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void addAccount(Account account) {
        if (!connectedToDatabase) {
            throw new IllegalStateException();
        }

        try {
            this.statement.executeUpdate("INSERT INTO card (number, pin, balance) " +
                    "VALUES (" + account.getCardNumber() +
                    ", " + account.getPin() +
                    ", " + account.getBalance() + ");");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void closeAccount(String cardNumber) {
        String delete = "DELETE FROM card WHERE number = ?";
        try(PreparedStatement preparedStatement = this.connection.prepareStatement(delete)) {
            preparedStatement.setString(1, cardNumber);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setBalance(String cardNumber, int newBalance) {
        if (!connectedToDatabase) {
            throw new IllegalStateException();
        }

        String updateBalance = "UPDATE card SET balance = ? WHERE number = ?";
        try(PreparedStatement preparedStatement = this.connection.prepareStatement(updateBalance)) {
            preparedStatement.setInt(1, newBalance);
            preparedStatement.setString(2, cardNumber);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Account getAccount(String cardNumber) {
        Account account = null;
        String getAccount = "SELECT * FROM card WHERE number = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(getAccount)){
            preparedStatement.setString(1, cardNumber);
            ResultSet resultSet = preparedStatement.executeQuery();
            account = new Account(resultSet.getString("number"), resultSet.getString("pin"), resultSet.getInt("balance"));
        } catch (SQLException e) {

        }
        if (account == null) {
            throw new IllegalStateException();
        }
        return account;
    }

    public void transfer(Account origin, int amount, Account destination) {
        setBalance(origin.getCardNumber(), origin.getBalance() - amount);
        setBalance(destination.getCardNumber(), destination.getBalance() + amount);
    }
}


