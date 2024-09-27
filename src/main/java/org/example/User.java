package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class User {
    private String username;
    private String password;

    public User() {
        setUsername("Username");
        setPassword("Password");
    }

    public User(String username, String password) {
        setUsername(username);
        setPassword(password);
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

    public static void getUsers(Connection conn, ArrayList<User> users) throws SQLException {
        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM users");
        while (rs.next()) {
            User user = new User(rs.getString("username"), rs.getString("password"));
            users.add(user);
        }
    }

    public static void createUser(Connection conn, String username, String password) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO users(username, password) values(?, ?)");
        preparedStatement.setString(1, username);
        preparedStatement.setString(2, password);
        preparedStatement.executeUpdate();
    }

    public static User getUserByUsername(Connection conn, String username) throws SQLException {
        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM users WHERE username = '" + username + "'");
        if (rs.next()) {
            return new User(rs.getString("username"), rs.getString("password"));
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
                User user = getUserByUsername(conn, username);
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
        } else {
            while (true) {
                System.out.print("Username: ");
                username = scanner.nextLine();
                System.out.print("Password: ");
                password = scanner.nextLine();
                User user = getUserByUsername(conn, username);
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
