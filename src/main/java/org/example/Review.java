package org.example;

import java.sql.*;
import java.util.ArrayList;

public class Review {
    private int id;
    private int hospitalId;
    private int userId;
    private int rating;
    private String content;

    public Review(int id, int hospitalId, int userId, int rating, String content) {
        setId(id);
        setHospitalId(hospitalId);
        setUserId(userId);
        setRating(rating);
        setContent(content);
    }

    public Review(int hospitalId, int userId, int rating, String content) {
        setRating(rating);
        setHospitalId(hospitalId);
        setUserId(userId);
        setContent(content);
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

    public static void createReview(Connection conn, Review review) throws SQLException {
        Hospital hospital = Hospital.getHospital(conn, review.getHospitalId());
        PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO reviews(userId, hospitalId, rating, content) values(?, ?, ?, ?)");
        preparedStatement.setInt(1, review.getUserId());
        preparedStatement.setInt(2, review.getHospitalId());
        preparedStatement.setInt(3, review.getRating());
        preparedStatement.setString(4, review.getContent());
        preparedStatement.executeUpdate();
        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery("SELECT AVG(rating) FROM reviews WHERE hospitalId = " + hospital.getId());
        float avgRating = rs.getFloat(1);
        hospital.setRating(avgRating);
        hospital.edit(conn, hospital);
    }

    @Override
    public String toString() {
        return "Rating: " + rating + " Content: " + content;
    }

}
