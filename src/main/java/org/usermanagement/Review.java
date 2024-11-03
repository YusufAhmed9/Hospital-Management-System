package org.usermanagement;

import org.example.DatabaseConnection;
import org.hospital.Hospital;

import java.sql.*;

public class Review {
    private String  id;
    private String  hospitalId;
    private String  userId;
    private int rating;
    private String content;

    public Review(String id, String hospitalId, String userId, int rating, String content) {
        setId(id);
        setHospitalId(hospitalId);
        setUserId(userId);
        setRating(rating);
        setContent(content);
    }

    public Review(String hospitalId, String userId, int rating, String content) {
        setRating(rating);
        setHospitalId(hospitalId);
        setUserId(userId);
        setContent(content);
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getHospitalId() {
        return hospitalId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getRating() {
        return rating;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void create() throws SQLException {
        Hospital hospital = Hospital.getHospital(getHospitalId());
        if (hospital == null) {
            return;
        }
        Connection connection = DatabaseConnection.getInstance().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO review(user_id, hospital_id, rating, content) values(?, ?, ?, ?)");
        preparedStatement.setString(1, getUserId());
        preparedStatement.setString(2, getHospitalId());
        preparedStatement.setInt(3, getRating());
        preparedStatement.setString(4, getContent());
        preparedStatement.executeUpdate();
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT AVG(rating) FROM review WHERE hospital_id = " + hospital.getId());
        float avgRating = rs.getFloat(1);
        hospital.setRating(avgRating);
        hospital.edit(hospital);
    }

    @Override
    public String toString() {
        return "Rating: " + rating + " Content: " + content;
    }

}
