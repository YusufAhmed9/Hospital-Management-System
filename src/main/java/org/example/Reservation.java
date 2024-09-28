package org.example;

import java.sql.*;

public class Reservation {
    private int id;
    private int hospitalId;
    private int userId;
    private int duration;
    private float price;

    Reservation (int id, int hospitalId, int userId, int duration, float price) {
        setId(id);
        setHospitalId(hospitalId);
        setUserId(userId);
        setDuration(duration);
        setPrice(price);
    }

    Reservation (int hospitalId, int userId, int duration, float price) {
        setHospitalId(hospitalId);
        setUserId(userId);
        setDuration(duration);
        setPrice(price);
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setHospitalId(int hospitalId) {
        this.hospitalId = hospitalId;
    }

    public int getHospitalId() {
        return hospitalId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getDuration() {
        return duration;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getPrice() {
        return price;
    }

    public static Reservation getReservation(Connection conn, int id) throws SQLException {
        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM reservations WHERE id = " + id);
        if (rs.next()) {
            return new Reservation(rs.getInt("id"), rs.getInt("hospitalId"), rs.getInt("userId"), rs.getInt("duration"), rs.getFloat("price"));
        }
        return null;
    }

    public static void createReservation(Connection conn, Reservation reservation) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO reservations(userId, hospitalId, duration, price) values(?, ?, ?, ?)");
        preparedStatement.setInt(1, reservation.getUserId());
        preparedStatement.setInt(2, reservation.getHospitalId());
        preparedStatement.setInt(3, reservation.getDuration());
        preparedStatement.setFloat(4, reservation.getPrice());
        preparedStatement.executeUpdate();
    }

    public void delete(Connection conn) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement("DELETE FROM reservations WHERE id = ?");
        preparedStatement.setInt(1, getId());
        preparedStatement.executeUpdate();
    }
}
