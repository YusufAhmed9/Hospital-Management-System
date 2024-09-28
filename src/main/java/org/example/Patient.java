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

    public Patient(int id, String username, String password) {
        setId(id);
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
                    user = getUser(conn, modifiedUsername);
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
            else {
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

    private void reserve() {

    }

    private void writeReview(Connection conn) throws SQLException {
        Hospital.displayHospitals(conn);
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Hospital ID To Review: ");
            int hospitalId = scanner.nextInt();
            Hospital hospital = Hospital.getHospital(conn, hospitalId);
            if (hospital != null) {
                String content;
                int rating;
                while (true) {
                    System.out.print("Rating (Out Of 10) : ");
                    rating = scanner.nextInt();
                    scanner.nextLine();
                    if (rating < 1 || rating > 10) {
                        System.out.println("Rating Must Be Between 1 and 10.");
                        continue;
                    }
                    break;
                }
                while (true) {
                    System.out.print("Review Content: ");
                    content = scanner.nextLine();
                    if (content.isEmpty()) {
                        System.out.println("Review Content Can't Be Empty.");
                        continue;
                    }
                    break;
                }
                Review review = new Review(hospital.getId(), getId(), rating, content);
                Review.createReview(conn, review);
                break;
            }
            System.out.println("No Hospital With That ID.");
        }
    }

    private void showReviews(Connection conn) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        Hospital.displayHospitals(conn);
        while (true) {
            System.out.print("Hospital ID To Show Reviews For: ");
            int hospitalId = scanner.nextInt();
            Hospital hospital = Hospital.getHospital(conn, hospitalId);
            if (hospital != null) {
                hospital.showReviews(conn);
                break;
            }
            System.out.println("No Hospital With That ID.");
        }
    }

    public void Interface(Connection conn) throws SQLException {
        int option;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome" + getUsername() + "\n");
        System.out.println("What would you like to do ?\n");

        while (true){
            System.out.println("[1]: Edit your information");
            System.out.println("[2]: View hospitals");
            System.out.println("[3]: Reserve");
            System.out.println("[4]: Write A Review");
            System.out.println("[5]: Show Reviews");
            System.out.println("[6]: Log out");
            System.out.print("Choose an option: ");
            option = scanner.nextInt();

            if(option > 6 || option < 1){
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
        else if (option == 3) {
            if (Hospital.count(conn) > 0) {
                reserve();
            }
            else {
                System.out.println("No Hospitals To Reserve.");
            }
        }
        else if (option == 4) {
            if (Hospital.count(conn) > 0) {
                writeReview(conn);
            }
            else {
                System.out.println("No Hospitals To Review.");
            }
        }
        else if (option == 5) {
            if (Hospital.count(conn) > 0) {
                showReviews(conn);
            }
            else {
                System.out.println("No Hospitals To Show Reviews For.");
            }
        }
    }
}
