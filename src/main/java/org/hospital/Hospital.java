package org.hospital;

import org.usermanagement.Reservation;
import org.usermanagement.Review;
import org.usermanagement.User;

import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Hospital {
    private int id;
    private String name;
    private float rating;

    Hospital(int id, String name, float reservationPrice, float rating) {
        setId(id);
        setName(name);
        setRating(rating);
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
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

    public static Hospital getHospital(Connection conn, int id) throws SQLException {
        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM hospitals WHERE id = " + id);
        if (rs.next()) {
            return new Hospital(rs.getInt("id"), rs.getString("name"), rs.getFloat("reservationPrice"), rs.getInt("rating"));
        }
        return null;
    }

    public static Hospital getHospital(Connection conn, String name) throws SQLException {
        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM hospitals WHERE name = '" + name + "'");
        if (rs.next()) {
            return new Hospital(rs.getInt("id"), rs.getString("name"), rs.getFloat("reservationPrice"), rs.getFloat("rating"));
        }
        return null;
    }

    public static void createHospital(Connection conn, Hospital hospital) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO hospitals(name, reservationPrice, rating) values(?, ?, ?)");
        preparedStatement.setString(1, hospital.getName());
        preparedStatement.setFloat(2, hospital.getReservationPrice());
        preparedStatement.setFloat(3, hospital.getRating());
        preparedStatement.executeUpdate();
    }

    public static void displayHospitals(Connection conn) throws SQLException {
        if (count(conn) > 0) {
            DecimalFormat df = new DecimalFormat("0.00");
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM hospitals");
            System.out.printf("%15s", "ID |");
            System.out.printf("%40s", "Name |");
            System.out.printf("%20s", "Reservation Price |");
            System.out.printf("%15s", "Rating |\n");
            while (rs.next()) {
                System.out.printf("%15s", rs.getInt("id") + " |");
                System.out.printf("%40s", rs.getString("Name") + " |");
                System.out.printf("%20s", rs.getFloat("reservationPrice") + " |");
                System.out.printf("%15s", df.format(rs.getFloat("rating")) + " |\n");
            }
        }
        else {
            System.out.println("No Hospitals To Display.");
        }
    }

    public void delete(Connection conn) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement("DELETE FROM hospitals WHERE id = ?");
        preparedStatement.setInt(1, getId());
        preparedStatement.executeUpdate();
    }

    public void edit(Connection conn, Hospital hospital) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement("UPDATE hospitals SET name = ?, reservationPrice = ?, rating = ? WHERE id = ?");
        preparedStatement.setString(1, hospital.getName());
        preparedStatement.setFloat(2, hospital.getReservationPrice());
        preparedStatement.setFloat(3, hospital.getRating());
        preparedStatement.setInt(4, getId());
        preparedStatement.executeUpdate();
    }

    public static int count(Connection conn) throws SQLException {
        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery("SELECT COUNT(*) FROM hospitals");
        return rs.getInt(1);
    }

    public void addSpeciality(Speciality speciality, Connection conn) throws SQLException {
        if (speciality.specialityExists(getId(), conn))
            return;
        PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO HospitalSpeciality(HospitalId, SpecialityId) VALUES(?, ?)");
        preparedStatement.setInt(1, getId());
        preparedStatement.setInt(2, speciality.getId());
        preparedStatement.executeUpdate();
    }

    public void displaySpecialities(Connection conn) throws SQLException {
        String query = "SELECT SpecialityId FROM HospitalSpeciality WHERE HospitalId = ?";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, getId());

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                int specialityId = rs.getInt("SpecialityId");

                String query1 = "SELECT name FROM Specialities WHERE id = ?";

                try (PreparedStatement statement1 = conn.prepareStatement(query1)) {

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

    private ArrayList<Review> getReviews(Connection conn) throws SQLException {
        ArrayList<Review> reviews = new ArrayList<Review>();
        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM reviews WHERE hospitalId = " + getId());
        while (rs.next()) {
            Review review = new Review(rs.getInt("id"), rs.getInt("hospitalId"), rs.getInt("userId"), rs.getInt("rating"), rs.getString("content"));
            reviews.add(review);
        }
        return reviews;
    }

    public void showReviews(Connection conn) throws SQLException {
        ArrayList<Review> reviews =  getReviews(conn);
        if (!reviews.isEmpty()) {
            System.out.printf("%15s", "User |");
            System.out.printf("%15s", "Rating |");
            System.out.print(" Content\n");
            for (Review review : reviews) {
                User user = User.getUser(conn, review.getUserId());
                System.out.printf("%15s", user.getUsername() + " |");
                System.out.printf("%15s", review.getRating()  + " |");
                System.out.print(" " + review.getContent() + "\n");
            }
        }
        else {
            System.out.println("No Reviews For This Hospital Yet.");
        }
    }

    private ArrayList<Reservation> getReservations(Connection conn) throws SQLException {
        ArrayList<Reservation> reservations = new ArrayList<Reservation>();
        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM reservations WHERE hospitalId = " + getId());
        while (rs.next()) {
            Reservation reservation = new Reservation(rs.getInt("id"), rs.getInt("hospitalId"), rs.getInt("userId"), rs.getInt("duration"), rs.getFloat("price"));
            reservations.add(reservation);
        }
        return reservations;
    }

    public void showReservations(Connection conn) throws SQLException {
        ArrayList<Reservation> reservations =  getReservations(conn);
        if (!reservations.isEmpty()) {
            System.out.printf("%15s", "ID |");
            System.out.printf("%15s", "User |");
            System.out.printf("%15s", "Duration |");
            System.out.printf("%15s", "Price |\n");
            for (Reservation reservation : reservations) {
                User user = User.getUser(conn, reservation.getUserId());
                System.out.printf("%15s", reservation.getId() + " |");
                System.out.printf("%15s", user.getUsername() + " |");
                System.out.printf("%15s", reservation.getDuration()  + " |");
                System.out.printf("%15s", reservation.getPrice() + "\n");
            }
        }
        else {
            System.out.println("No Reservations For This Hospital Yet.");
        }
    }
}
