package org.usermanagement;

import org.hospital.Hospital;

import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
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
        int option;
        String choice;
        DecimalFormat df = new DecimalFormat("0.00");
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
                "\nRating: " + df.format(hospital.getRating()) + "\n"
        );
        System.out.println("[1]: View Reviews");
        System.out.println("[2]: View Specialities");
        while (true) {
            System.out.print("Option: ");
            option = scanner.nextInt();
            if (option == 1) {
                int reviewOption;
                hospital.showReviews(connection);
                System.out.println("[1]: Write a review");
                System.out.println("[2]: Exit");
                while (true) {
                    System.out.print("Option: ");
                    reviewOption = scanner.nextInt();
                    if (reviewOption == 1) {
                        writeReview(connection, hospital);
                        break;
                    }
                    else if (reviewOption == 2) {
                        break;
                    }
                    else {
                        System.out.println("Invalid Option.");
                    }
                }
                break;
            }
            else if (option == 2) {
                System.out.println("Specialities: ");
                hospital.displaySpecialities(connection);
                System.out.print("Would you like to choose a speciality ? (y/n): ");
                scanner.nextLine();
                choice = scanner.nextLine().toLowerCase();
                System.out.print(choice);
                if(choice.equals("n"))
                    return;
                System.out.print("Enter speciality: ");
                choice = scanner.nextLine();
                hospital.displayClinics(choice);
                break;
            }
            else {
                System.out.println("Invalid option.");
            }
        }
    }

    private void reserve(Connection conn) throws SQLException {
        Hospital.displayHospitals(conn);
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Hospital ID To Reserve: ");
            int hospitalId = scanner.nextInt();
            Hospital hospital = Hospital.getHospital(conn, hospitalId);
            if (hospital != null) {
                int duration;
                float price;
                while (true) {
                    System.out.print("Duration In Days: ");
                    duration = scanner.nextInt();
                    if (duration > 0) {
                        break;
                    }
                    System.out.println("Duration Can't Be Less Than Or Equal To 0.");
                }
                price = duration * hospital.getReservationPrice();
                Reservation reservation = new Reservation(hospital.getId(), getId(), duration, price);
                Reservation.createReservation(conn, reservation);
                System.out.println("The Price Of Your Reservation At " + hospital.getName() + " Is " + price);
                break;
            }
            else {
                System.out.println("No Hospital With That ID.");
            }
        }
    }

    private void deleteReservation(Connection conn) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Reservation ID To Delete: ");
            int hospitalId = scanner.nextInt();
            Reservation reservation = Reservation.getReservation(conn, hospitalId);
            if (reservation != null) {
                reservation.delete(conn);
                break;
            }
            System.out.println("No Reservation With That ID.");
        }
    }

    private void writeReview(Connection conn, Hospital hospital) throws SQLException {
        Scanner scanner = new Scanner(System.in);
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
    }

    private ArrayList<Reservation> getReservations(Connection conn) throws SQLException {
        ArrayList<Reservation> reservations = new ArrayList<Reservation>();
        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM reservations WHERE userId = " + getId());
        while (rs.next()) {
            Reservation reservation = new Reservation(rs.getInt("id"), rs.getInt("hospitalId"), rs.getInt("userId"), rs.getInt("duration"), rs.getFloat("price"));
            reservations.add(reservation);
        }
        return reservations;
    }

    private void showReservations(Connection conn) throws SQLException {
        ArrayList<Reservation> reservations = getReservations(conn);
        Scanner scanner = new Scanner(System.in);
        int option;
        if (!reservations.isEmpty()) {
            System.out.printf("%15s", "ID |");
            System.out.printf("%40s", "Hospital |");
            System.out.printf("%15s", "Duration |");
            System.out.printf("%15s", "Price |\n");
            for (Reservation reservation : reservations) {
                Hospital hospital = Hospital.getHospital(conn, reservation.getHospitalId());
                System.out.printf("%15s", reservation.getId() + " |");
                System.out.printf("%40s", hospital.getName() + " |");
                System.out.printf("%15s", reservation.getDuration()  + " |");
                System.out.printf("%15s", reservation.getPrice() + " |\n");
            }
            System.out.println("[1]: Cancel a reservation");
            System.out.println("[2]: Exit");
            System.out.print("Option: ");
            option = scanner.nextInt();
            while (true) {
                if (option == 1) {
                    deleteReservation(conn);
                    break;
                }
                else if (option != 2) {
                    System.out.println("Invalid Option.");
                }
            }
        }
        else {
            System.out.println("You Have No Reservations Yet.");
        }
    }

    private ArrayList<Review> getReviews(Connection conn) throws SQLException {
        ArrayList<Review> reviews = new ArrayList<Review>();
        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM reviews WHERE userId = " + getId());
        while (rs.next()) {
            Review review = new Review(rs.getInt("id"), rs.getInt("hospitalId"), rs.getInt("userId"), rs.getInt("rating"), rs.getString("content"));
            reviews.add(review);
        }
        return reviews;
    }

    private void showPatientReviews(Connection conn) throws SQLException {
        ArrayList<Review> reviews = getReviews(conn);
        if (!reviews.isEmpty()) {
            System.out.printf("%40s", "Hospital |");
            System.out.printf("%15s", "Rating |");
            System.out.print(" Content\n");
            for (Review review : reviews) {
                Hospital hospital = Hospital.getHospital(conn, review.getHospitalId());
                System.out.printf("%40s", hospital.getName() + " |");
                System.out.printf("%15s", review.getRating()  + " |");
                System.out.print(" " + review.getContent() + "\n");
            }
        }
        else {
            System.out.println("You Have No Reviews Yet.");
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
            System.out.println("[3]: Your Reservations");
            System.out.println("[4]: Your Reviews");
            System.out.println("[5]: Log out");
            System.out.print("Choose an option: ");
            option = scanner.nextInt();
            if(option > 5 || option < 1){
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
            showReservations(conn);
        }
        else if (option == 4) {
            showPatientReviews(conn);
        }
    }
}
