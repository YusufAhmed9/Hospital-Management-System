package org.usermanagement;

import org.example.DatabaseConnection;
import org.hospital.Clinic;
import org.hospital.Hospital;

import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class Patient extends User {
    public Patient(String username, String password) {
        setUsername(username);
        setPassword(password);
    }

    public Patient(String id, String username, String password) {
        setId(id);
        setUsername(username);
        setPassword(password);
    }

    private void editUserInfo() throws SQLException {
        Connection connection = DatabaseConnection.getInstance().getConnection();
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
                    user = getUser(modifiedUsername);
                    if (user == null) {
                        break;
                    }
                    System.out.println("Username already exists.");
                }

                PreparedStatement preparedStatement = connection.prepareStatement("UPDATE user SET username = ? WHERE username = ?");
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
                PreparedStatement preparedStatement = connection.prepareStatement("UPDATE user SET password = ? WHERE username = ?");
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

    private void viewHospitals() throws SQLException {
        Connection connection = DatabaseConnection.getInstance().getConnection();
        int option;
        String choice;
        DecimalFormat df = new DecimalFormat("0.00");
        Hospital.displayHospitals();
        if (Hospital.count() == 0) {
            return;
        }
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

            if(Hospital.getHospitalByName(hospitalName) == null) {
                System.out.println("Hospital not found\n");
                continue;
            }
            hospital = Hospital.getHospitalByName(hospitalName);
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
                hospital.showReviews();
                System.out.println("[1]: Write a review");
                System.out.println("[2]: Exit");
                while (true) {
                    System.out.print("Option: ");
                    reviewOption = scanner.nextInt();
                    if (reviewOption == 1) {
                        writeReview(hospital);
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
                hospital.displaySpecialities();
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

    private void reserve() throws SQLException {
        Hospital.displayHospitals();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Hospital ID To Reserve: ");
            String hospitalId = scanner.nextLine();
            Hospital hospital = Hospital.getHospital(hospitalId);
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
//                price = duration * hospital.getReservationPrice();
                Reservation reservation = new Reservation(hospital.getId(), getId(), new Date());
                reservation.create();
//                System.out.println("The Price Of Your Reservation At " + hospital.getName() + " Is " + price);
                break;
            }
            else {
                System.out.println("No Hospital With That ID.");
            }
        }
    }

    private void deleteReservation() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Reservation ID To Delete: ");
            int reservationId = scanner.nextInt();
            Reservation reservation = Reservation.getReservation(reservationId);
            if (reservation != null) {
                reservation.delete();
                break;
            }
            System.out.println("No Reservation With That ID.");
        }
    }

    private void writeReview(Hospital hospital) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        String content;
        int rating;
        while (true) {
            System.out.print("Rating (Out Of 10): ");
            rating = scanner.nextInt();
            scanner.nextLine();
            if (rating < 1 || rating > 10) {
                System.out.println("Rating Must Be Between 1 and 10.");
                continue;
            }
            break;
        }
        System.out.print("Review Content ( Optional ): ");
        content = scanner.nextLine();
        Review review = new Review(hospital.getId(), getId(), rating, content);
        review.create();
    }

    private ArrayList<Reservation> getReservations() throws SQLException {
        Connection connection = DatabaseConnection.getInstance().getConnection();
        ArrayList<Reservation> reservations = new ArrayList<Reservation>();
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM reservation WHERE user_id = " + getId());
        while (rs.next()) {
            Reservation reservation = new Reservation(rs.getString("id"), rs.getString("clinic_id"), rs.getString("user_id"), rs.getDate("reservation_date"));
            reservations.add(reservation);
        }
        return reservations;
    }

    private void showReservations() throws SQLException {;
        ArrayList<Reservation> reservations = getReservations();
        Scanner scanner = new Scanner(System.in);
        int option;
        if (!reservations.isEmpty()) {
            System.out.printf("%15s", "ID |");
            System.out.printf("%40s", "Doctor |");
            System.out.printf("%15s", "Date |\n");
            for (Reservation reservation : reservations) {
                Clinic clinic = Clinic.getClinic(reservation.getClinicId());
                System.out.printf("%15s", reservation.getId() + " |");
                System.out.printf("%40s", clinic.getDoctorName() + " |");
                System.out.printf("%15s", reservation.getReservationDate()  + " |");
            }
            System.out.println("[1]: Cancel a reservation");
            System.out.println("[2]: Exit");
            System.out.print("Option: ");
            option = scanner.nextInt();
            while (true) {
                if (option == 1) {
                    deleteReservation();
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

    private ArrayList<Review> getReviews() throws SQLException {
        Connection connection = DatabaseConnection.getInstance().getConnection();
        ArrayList<Review> reviews = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM review WHERE user_id = " + getId());
        while (rs.next()) {
            Review review = new Review(rs.getString("id"), rs.getString("hospitalId"), rs.getString("userId"), rs.getInt("rating"), rs.getString("content"));
            reviews.add(review);
        }
        return reviews;
    }

    private void showPatientReviews() throws SQLException {
        ArrayList<Review> reviews = getReviews();
        if (!reviews.isEmpty()) {
            System.out.printf("%40s", "Hospital |");
            System.out.printf("%15s", "Rating |");
            System.out.print(" Content\n");
            for (Review review : reviews) {
                Hospital hospital = Hospital.getHospital(review.getHospitalId());
                if (hospital == null) {
                    return;
                }
                System.out.printf("%40s", hospital.getName() + " |");
                System.out.printf("%15s", review.getRating()  + " |");
                System.out.print(" " + review.getContent() + "\n");
            }
        }
        else {
            System.out.println("You Have No Reviews Yet.");
        }
    }

    public void Interface() throws SQLException {
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
            editUserInfo();
        }
        else if(option == 2){
             viewHospitals();
        }
        else if (option == 3) {
            showReservations();
        }
        else if (option == 4) {
            showPatientReviews();
        }
    }
}
