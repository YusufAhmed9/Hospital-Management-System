package org.usermanagement;

import org.example.DatabaseConnection;

import java.sql.*;
import java.util.Scanner;

public class User {
    private String name;
    private String username;
    private String password;
    private String id;

    public User() {
        setUsername("Username");
        setPassword("Password");
    }

    public User(String id,  String username, String password) {
        setId(id);
        setUsername(username);
        setPassword(password);
    }

    public void setId(String id) {
        this.id = id;
    }

    public String  getId() {
        return id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public static void createUser(Connection conn, String username, String password) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO users(username, password) values(?, ?)");
        preparedStatement.setString(1, username);
        preparedStatement.setString(2, password);
        preparedStatement.executeUpdate();
    }

    public static User getUserByName(String username) throws SQLException {
        Connection connection = DatabaseConnection.getInstance().getConnection();
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM user WHERE username = '" + username + "'");
        if (rs.next()) {
            return new User(rs.getString("id"), rs.getString("username"), rs.getString("password"));
        }
        return null;
    }

    public static User getUser(String id) throws SQLException {
        Connection connection = DatabaseConnection.getInstance().getConnection();
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM users WHERE id = " + id);
        if (rs.next()) {
            return new User(rs.getString("id"), rs.getString("username"), rs.getString("password"));
        }
        return null;
    }

    public static void loginMenu(Connection conn, User currentUser) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome To Hospital Management System");
        int option;
        while (true) {
            System.out.println("[ 1 ]: Register");
            System.out.println("[ 2 ]: Login");
            System.out.print("Enter an option: ");
            option = scanner.nextInt();
            if (option != 1 && option != 2) {
                System.out.println("Please enter a valid option\n");
                continue;
            }
            break;
        }
        String username;
        String password;
        if (option == 1) {
            while (true) {
                System.out.print("Username: ");
                username = scanner.nextLine();
                if (username.isEmpty()) {
                    System.out.println("Username Can't Be Empty.");
                    continue;
                }
                User user = getUser(username);
                if (user == null) {
                    break;
                }
                System.out.println("Username already exists.");
            }
            while (true) {
                System.out.print("Password: ");
                password = scanner.nextLine();
                if (password.length() >= 8) {
                    break;
                }
                System.out.println("Password Must Contain At Least 8 Characters");
            }
            createUser(conn, username, password);
        }
        else {
            while (true) {
                System.out.print("Username: ");
                username = scanner.nextLine();
                System.out.print("Password: ");
                password = scanner.nextLine();
                User user = getUser(username);
                if (user != null) {
                    if (user.getPassword().equals(password)) {
                        currentUser = user;
                        System.out.println("Logged In Successfully.");
                        break;
                    }
                }
                System.out.println("Wrong Username Or Password.");
            }
        }
    }
}
