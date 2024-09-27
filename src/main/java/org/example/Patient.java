package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class Patient extends User {
    public Patient(String username, String password) {
        setUsername(username);
        setPassword(password);
    }

    private void editUserInfo(Connection conn) throws SQLException {
        int option;
        String choice;
        Scanner scanner = new Scanner(System.in);
        do {
            System.out.println("Username: " + getUsername());
            System.out.println("Password: " + getPassword() + "\n");
            System.out.println("Which one would you like to edit ?");
            while (true) {
                System.out.println("[1]: Username");
                System.out.println("[2]: Password");
                System.out.print("Choose an option: ");

                if (!scanner.hasNextInt()) {
                    System.out.println("Error: Please enter a valid option\n");
                    scanner.next();
                    continue;
                }

                option = scanner.nextInt();
                scanner.nextLine();
                if(option != 1 && option != 2){
                    System.out.println("Error: Please enter a valid option\n");
                    continue;
                }
                break;
            }

            if (option == 1) {
                User user;
                String modifiedUsername;
                while (true) {
                    System.out.print("Enter the modified username: ");
                    modifiedUsername = scanner.nextLine();
                    user = getUserByUsername(conn, modifiedUsername);
                    if (user == null) {
                        break;
                    }
                    System.out.println("Username already exists.");
                }

                PreparedStatement preparedStatement = conn.prepareStatement("UPDATE users SET username = ? WHERE username = ?");
                preparedStatement.setString(1, modifiedUsername);
                preparedStatement.setString(2, getUsername());
                preparedStatement.executeUpdate();
                setUsername(modifiedUsername);
            }
            else{
                String modifiedPassword;
                while(true){
                    System.out.print("Enter the modified Password: ");
                    modifiedPassword = scanner.nextLine();
                    if(modifiedPassword.equals(getPassword()) || modifiedPassword.length() < 8){
                        System.out.println("Password already in use");
                        continue;
                    }
                    break;
                }
                PreparedStatement preparedStatement = conn.prepareStatement("UPDATE users SET password = ? WHERE username = ?");
                preparedStatement.setString(1, modifiedPassword);
                preparedStatement.setString(2, getUsername());
                preparedStatement.executeUpdate();
                setPassword(modifiedPassword);
            }
            System.out.print("Would you like to edit something else ?(y/n)");
            choice = scanner.nextLine().toLowerCase();
        }
        while (choice.equals("y"));
    }

    private void viewHospitals(Connection connection) throws SQLException {
        String choice;
        Hospital.displayHospitals(connection);
        Scanner scanner = new Scanner(System.in);
        System.out.print("Would you like to select a hospital ?(y/n): ");
        choice = scanner.nextLine().toLowerCase();
        if(choice.equals("n"))
            return;
        Hospital hospital;
        String hospitalName;
        while(true) {
            System.out.print("Enter hospital's name:");
            hospitalName = scanner.nextLine();

            if(Hospital.getHospital(connection, hospitalName) == null) {
                System.out.println("Hospital not found\n");
                continue;
            }
            hospital = Hospital.getHospital(connection, hospitalName);
            break;
        }
        System.out.println("\nName:" + hospital.getName() +
                "\nRating: " + hospital.getRating() + "\n"
        );
        System.out.println("Specialities: ");
        hospital.displaySpecialities();
        System.out.println("Would you like to choose a speciality ? (y/n): ");
        choice = scanner.nextLine().toLowerCase();
        if(choice.equals("n"))
            return;
        System.out.print("Enter specialty: ");
        choice = scanner.nextLine();
        hospital.displayClinics(choice);
    }

    public void Interface(Connection conn) throws SQLException {
        int option;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome" + getUsername() + "\n");
        System.out.println("What would you like to do ?\n");

        while (true){
            System.out.println("[1]: Edit your information");
            System.out.println("[2]: View hospitals");
            System.out.println("[3]: Log out");
            System.out.print("Choose an option: ");
            option = scanner.nextInt();

            if(option > 3 || option < 1){
                System.out.println("Error: Please enter a valid option\n");
                continue;
            }

            break;
        }
        if(option == 1){
            editUserInfo(conn);
        }
        else if(option == 2){
             viewHospitals(conn);
        }
    }
}
