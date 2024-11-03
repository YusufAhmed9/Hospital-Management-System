package org.usermanagement;

import org.example.DatabaseConnection;

import java.sql.*;
import java.util.Date;

public class Reservation {
    private String id;
    private String clinicId;
    private String userId;
    Date reservationDate;

    public Reservation (String id, String  clinicId, String  userId, Date reservationDate) {
        setId(id);
        setClinicId(clinicId);
        setUserId(userId);
        setReservationDate(reservationDate);
    }

    Reservation (String clinicId, String  userId, Date reservationDate) {
        setClinicId(clinicId);
        setUserId(userId);
        setReservationDate(reservationDate);
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setClinicId(String clinicId) {
        this.clinicId = clinicId;
    }

    public String getClinicId() {
        return clinicId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public Date getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(Date reservationDate) {
        this.reservationDate = reservationDate;
    }

    public static Reservation getReservation(int id) throws SQLException {
        Connection connection = DatabaseConnection.getInstance().getConnection();
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM reservation WHERE id = " + id);
        if (rs.next()) {
            return new Reservation(rs.getString("id"), rs.getString("clinic_id"), rs.getString("user_id"), rs.getDate("reservation_date"));
        }
        return null;
    }

    public void create() throws SQLException {
        Connection connection = DatabaseConnection.getInstance().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO reservation(user_id, clinic_id, reservation_date) values(?, ?, ?)");
        preparedStatement.setString(1, getUserId());
        preparedStatement.setString(2, getClinicId());
        preparedStatement.setDate(3, new java.sql.Date(getReservationDate().getTime()));
        preparedStatement.executeUpdate();
    }

    public void delete() throws SQLException {
        Connection connection = DatabaseConnection.getInstance().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM reservation WHERE id = ?");
        preparedStatement.setString(1, getId());
        preparedStatement.executeUpdate();
    }
}
