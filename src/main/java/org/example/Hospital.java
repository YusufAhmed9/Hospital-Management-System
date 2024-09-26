package org.example;

import java.sql.*;
import java.util.ArrayList;

public class Hospital {
    private int id;
    private String name;
    private float reservationPrice;
    private int rating;

    Hospital(String name, float reservationPrice) {
        setName(name);
        setRating(0);
        setReservationPrice(reservationPrice);
    }

    Hospital(String name, float reservationPrice, int rating) {
        setName(name);
        setRating(rating);
        setReservationPrice(reservationPrice);
    }

    Hospital(int id, String name, float reservationPrice, int rating) {
        setId(id);
        setName(name);
        setRating(rating);
        setReservationPrice(reservationPrice);
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

    public void setReservationPrice(float reservationPrice) {
        this.reservationPrice = reservationPrice;
    }

    public float getReservationPrice() {
        return reservationPrice;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getRating() {
        return rating;
    }

    public static Hospital getHospitalById(Connection conn, int id) throws SQLException {
        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM hospitals WHERE id = " + id);
        if (rs.next()) {
            return new Hospital(rs.getInt("id"), rs.getString("name"), rs.getFloat("reservationPrice"), rs.getInt("rating"));
        }
        return null;
    }

    public static Hospital getHospitalByName(Connection conn, String name) throws SQLException {
        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM hospitals WHERE name = '" + name + "'");
        if (rs.next()) {
            return new Hospital(rs.getInt("id"), rs.getString("name"), rs.getFloat("reservationPrice"), rs.getInt("rating"));
        }
        return null;
    }

    public static void createHospital(Connection conn, Hospital hospital) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO hospitals(name, reservationPrice, rating) values(?, ?, ?)");
        preparedStatement.setString(1, hospital.getName());
        preparedStatement.setFloat(2, hospital.getReservationPrice());
        preparedStatement.setInt(3, hospital.getRating());
        preparedStatement.executeUpdate();
    }

    public static void getHospitals(Connection conn, ArrayList<Hospital> hospitals) throws  SQLException {
        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM hospitals");
        while (rs.next()) {
            Hospital hospital = new Hospital(rs.getString("name"), rs.getFloat("reservationPrice"), rs.getInt("rating"));
            hospitals.add(hospital);
        }
    }

    public static void displayHospitals(Connection conn) throws SQLException {
        if (count(conn) > 0) {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM hospitals");
            System.out.printf("%15s", "ID |");
            System.out.printf("%15s", "Name |");
            System.out.printf("%20s", "Reservation Price |");
            System.out.printf("%15s", "Rating |\n");
            while (rs.next()) {
                System.out.printf("%15s", rs.getInt("id") + " |");
                System.out.printf("%15s", rs.getString("Name") + " |");
                System.out.printf("%20s", rs.getFloat("reservationPrice") + " |");
                System.out.printf("%15s", rs.getInt("rating") + " |\n");
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
        preparedStatement.setInt(3, hospital.getRating());
        preparedStatement.setInt(4, getId());
        preparedStatement.executeUpdate();
    }

    public static int count(Connection conn) throws SQLException {
        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery("SELECT COUNT(*) FROM hospitals");
        return rs.getInt(1);
    }

    @Override
    public String toString() {
        return this.name;
    }
}
