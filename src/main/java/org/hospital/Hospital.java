package org.hospital;

import org.example.DatabaseConnection;
import org.usermanagement.Reservation;
import org.usermanagement.Review;
import org.usermanagement.User;

import javax.security.sasl.SaslClient;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;

public class Hospital {
    private String id;
    private String name;
    private float rating;

    public Hospital(String name) throws SQLException {
        setName(name);
    }

    public Hospital(String id, String name, float rating) throws SQLException {
        setId(id);
        setName(name);
        setRating(rating);
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setName(String name) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        while (!isValidHospitalName(name)) {
            System.out.println("Hospital Name Can't Be Empty.");
            System.out.print("Enter Hospital Name: ");
            name = scanner.nextLine();
        }
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public float getRating() {
        return rating;
    }

    public static Hospital getHospital(String id) throws SQLException {
        Connection connection = DatabaseConnection.getInstance().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM hospital WHERE id = ?");
        preparedStatement.setString(1, id);
        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next()) {
            return new Hospital(rs.getString("id"), rs.getString("name"), rs.getFloat("rating"));
        }
        return null;
    }

    public static Hospital getHospitalByName(String name) throws SQLException {
        String query = "SELECT * FROM hospital WHERE name = ?";
        Connection connection = DatabaseConnection.getInstance().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1,name);
        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next()) {
            return new Hospital(rs.getString("id"), rs.getString("name"), rs.getFloat("rating"));
        }
        return null;
    }

    public void addHospital() throws SQLException {
        Connection connection =  DatabaseConnection.getInstance().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO hospital(name, rating) values(?, ?)");
        preparedStatement.setString(1, getName());
        preparedStatement.setFloat(2, getRating());
        preparedStatement.executeUpdate();
        Hospital hospital = getHospitalByName(getName());
        if (hospital != null) {
            setId(hospital.getId());
        }
    }

    public static void displayHospitals() throws SQLException {
        Connection connection = DatabaseConnection.getInstance().getConnection();
        if (count() > 0) {
            int option;
            Scanner scanner = new Scanner(System.in);
            DecimalFormat df = new DecimalFormat("0.00");
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM hospital");
            System.out.printf("%15s", "ID |");
            System.out.printf("%40s", "Name |");
            System.out.printf("%15s", "Rating |\n");
            while (rs.next()) {
                System.out.printf("%15s", rs.getString("id") + " |");
                System.out.printf("%40s", rs.getString("name") + " |");
                System.out.printf("%15s", df.format(rs.getFloat("rating")) + " |\n");
            }
        }
        else {
            System.out.println("No Hospitals To Display.");
        }
    }

    public void delete() throws SQLException {
        Connection connection = DatabaseConnection.getInstance().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM hospital WHERE id = ?");
        preparedStatement.setString(1, getId());
        preparedStatement.executeUpdate();
    }

    public void edit(Hospital hospital) throws SQLException {
        Connection connection = DatabaseConnection.getInstance().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("UPDATE hospital SET name = ?, rating = ? WHERE id = ?");
        preparedStatement.setString(1, hospital.getName());
        preparedStatement.setFloat(2, hospital.getRating());
        preparedStatement.setString(3, getId());
        preparedStatement.executeUpdate();
    }

    public static int count() throws SQLException {
        Connection connection = DatabaseConnection.getInstance().getConnection();
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT COUNT(*) FROM hospital");
        if (rs.next()) {
            return rs.getInt(1);
        }
        return 0;
    }

    public void displaySpecialities() throws SQLException {
        ArrayList<Speciality> specialities = getSpecialities();
        if (specialities.isEmpty()) {
            System.out.println("No Specialities For This Hospital");
            return;
        }
        System.out.printf("%30s", "Speciality Name |\n");
        for (Speciality speciality : specialities) {
            System.out.printf("%30s", speciality.getName() + " |\n");
        }
    }

    private ArrayList<Speciality> getSpecialities() throws SQLException {
        ArrayList<Speciality> specialities = new ArrayList<Speciality>();
        Connection connection = DatabaseConnection.getInstance().getConnection();
        String query = "SELECT * FROM speciality JOIN clinic ON speciality.id = clinic.speciality_id WHERE clinic.hospital_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, getId());
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            Speciality speciality = new Speciality(resultSet.getString("name"), resultSet.getString("id"));
            specialities.add(speciality);
        }
        return specialities;
    }

    public void displayClinics(String specialityId) throws SQLException {
        ArrayList<Clinic> clinics = getClinics(specialityId);
        if (clinics.isEmpty()) {
            System.out.println("No Clinics Found For This Hospital.");
            return;
        }
        System.out.printf("%30s", "Doctor Name |");
        System.out.printf("%15s", "Hospital |");
        System.out.printf("%15s", "Speciality |");
        System.out.print(" Reservation Price\n");
        for (Clinic clinic : clinics) {
            System.out.printf("%30s", clinic.getDoctorName() + " |");
            System.out.printf("%15s", Hospital.getHospital(clinic.getHospitalId()).getName()  + " |");
            System.out.printf("%15s", Speciality.getSpecialityById(clinic.getSpecialityId()).getName()  + " |");
            System.out.print(" " + clinic.getReservationPrice() + "\n");
        }
    }

    public void displayClinics() throws SQLException {
        ArrayList<Clinic> clinics = getClinics();
        if (clinics.isEmpty()) {
            System.out.println("No Clinics Found For This Hospital.");
            return;
        }
        System.out.printf("%30s", "Doctor Name |");
        System.out.printf("%15s", "Hospital |");
        System.out.printf("%15s", "Speciality |");
        System.out.print(" Reservation Price\n");
        for (Clinic clinic : clinics) {
            System.out.printf("%30s", clinic.getDoctorName() + " |");
            System.out.printf("%15s", Hospital.getHospital(clinic.getHospitalId()).getName()  + " |");
            System.out.printf("%15s", Speciality.getSpecialityById(clinic.getSpecialityId()).getName()  + " |");
            System.out.print(" " + clinic.getReservationPrice() + "\n");
        }
    }

    private ArrayList<Clinic> getClinics() throws SQLException {
        Connection connection = DatabaseConnection.getInstance().getConnection();
        ArrayList<Clinic> clinics = new ArrayList<Clinic>();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM clinic WHERE hospital_id = ?");
        preparedStatement.setString(1, getId());
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            Clinic clinic = new Clinic(
                        rs.getString("doctor_name"),
                        rs.getString("speciality_id"),
                        rs.getString("id"),
                        rs.getString("hospital_id"),
                        rs.getFloat("reservation_price")
                    );
            clinics.add(clinic);
        }
        return clinics;
    }

    private ArrayList<Clinic> getClinics(String specialityId) throws SQLException {
        Connection connection = DatabaseConnection.getInstance().getConnection();
        ArrayList<Clinic> clinics = new ArrayList<Clinic>();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM clinic WHERE hospital_id = ? AND speciality_id = ?");
        preparedStatement.setString(1, getId());
        preparedStatement.setString(2, specialityId);
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            Clinic clinic = new Clinic(
                    rs.getString("doctor_name"),
                    rs.getString("speciality_id"),
                    rs.getString("id"),
                    rs.getString("hospital_id"),
                    rs.getFloat("reservation_price")
            );
            clinics.add(clinic);
        }
        return clinics;
    }

    private ArrayList<Review> getReviews() throws SQLException {
        Connection connection = DatabaseConnection.getInstance().getConnection();
        ArrayList<Review> reviews = new ArrayList<Review>();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM review WHERE hospital_id = ?");
        preparedStatement.setString(1, getId());
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            Review review = new Review(rs.getString("id"), rs.getString("hospital_id"), rs.getString("user_id"), rs.getInt("rating"), rs.getString("content"));
            reviews.add(review);
        }
        return reviews;
    }

    public void showReviews() throws SQLException {
        Connection connection = DatabaseConnection.getInstance().getConnection();
        ArrayList<Review> reviews =  getReviews();
        if (!reviews.isEmpty()) {
            System.out.printf("%15s", "User |");
            System.out.printf("%15s", "Rating |");
            System.out.print(" Content\n");
            for (Review review : reviews) {
                User user = User.getUser(review.getUserId());
                System.out.printf("%15s", user.getUsername() + " |");
                System.out.printf("%15s", review.getRating()  + " |");
                System.out.print(" " + review.getContent() + "\n");
            }
        }
        else {
            System.out.println("No Reviews For This Hospital Yet.");
        }
    }

    private ArrayList<Reservation> getReservations() throws SQLException {
        Connection connection = DatabaseConnection.getInstance().getConnection();
        ArrayList<Reservation> reservations = new ArrayList<Reservation>();
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM reservations WHERE clinic = " + getId());
        while (rs.next()) {
            Reservation reservation = new Reservation(rs.getString("id"), rs.getString("clinic_id"), rs.getString("user_id"), DayOfWeek.valueOf(rs.getString("reservation_day")), rs.getTime("reservation_time").toLocalTime());
            reservations.add(reservation);
        }
        return reservations;
    }

    public void showReservations() throws SQLException {
        ArrayList<Reservation> reservations =  getReservations();
        if (!reservations.isEmpty()) {
            System.out.printf("%15s", "ID |");
            System.out.printf("%15s", "User |");
            for (Reservation reservation : reservations) {
                User user = User.getUser(reservation.getUserId());
                System.out.printf("%15s", reservation.getId() + " |");
                System.out.printf("%15s", user.getUsername() + " |");
            }
        }
        else {
            System.out.println("No Reservations For This Hospital Yet.");
        }
    }

    public boolean isValidHospitalName(String hospitalName) {
        return !hospitalName.isEmpty();
    }

    public boolean hasSpecialities() throws SQLException {
        Connection connection = DatabaseConnection.getInstance().getConnection();
        String query = "SELECT * FROM speciality JOIN clinic ON speciality.id = clinic.speciality_id WHERE clinic.hospital_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, getId());
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet.next();
    }
}
