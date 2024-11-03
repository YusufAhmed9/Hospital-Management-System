package org.hospital;

import org.example.DatabaseConnection;
import org.usermanagement.Reservation;
import org.usermanagement.Review;
import org.usermanagement.User;

import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Hospital {
    private String id;
    private String name;
    private float rating;

    public Hospital(String name) {
        setName(name);
    }

    public Hospital(String id, String name, float rating) {
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

    public void setName(String name) {
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
        Connection connection = DatabaseConnection.getInstance().getConnection();
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM hospital WHERE name = '" + name + "'");
        if (rs.next()) {
            return new Hospital(rs.getString("id"), rs.getString("name"), rs.getFloat("rating"));
        }
        return null;
    }

    public static void createHospital(Hospital hospital) throws SQLException {
        Connection connection =  DatabaseConnection.getInstance().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO hospitals(name, reservationPrice, rating) values(?, ?, ?)");
        preparedStatement.setString(1, hospital.getName());
        preparedStatement.setFloat(3, hospital.getRating());
        preparedStatement.executeUpdate();
    }

    public static void displayHospitals() throws SQLException {
        Connection connection = DatabaseConnection.getInstance().getConnection();
        if (count() > 0) {
            DecimalFormat df = new DecimalFormat("0.00");
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM hospital");
            System.out.printf("%15s", "ID |");
            System.out.printf("%40s", "Name |");
            System.out.printf("%15s", "Rating |\n");
            while (rs.next()) {
                System.out.printf("%15s", rs.getString("id") + " |");
                System.out.printf("%40s", rs.getString("Name") + " |");
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
        Connection connection = DatabaseConnection.getInstance().getConnection();
        String query = "SELECT SpecialityId FROM HospitalSpeciality WHERE hospital_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, getId());

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                int specialityId = rs.getInt("SpecialityId");

                String query1 = "SELECT name FROM Specialities WHERE id = ?";

                try (PreparedStatement statement1 = connection.prepareStatement(query1)) {

                    statement1.setInt(1, specialityId);

                    ResultSet rs1 = statement1.executeQuery();

                    while (rs1.next()) {
                        String speciality = rs1.getString("name");

                        System.out.println(speciality);
                    }
                }

                catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void displayClinics(String speciality) {

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
            Reservation reservation = new Reservation(rs.getString("id"), rs.getString("clinic_id"), rs.getString("user_id"), rs.getDate("reservation_date"));
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
}
