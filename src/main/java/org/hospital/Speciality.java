package org.hospital;

import org.example.DatabaseConnection;

import java.sql.*;

public class Speciality {
    private String name;
    private String id;

    public Speciality(String name) {
        setName(name);
    }

    public Speciality(String name, String id) {
        setName(name);
        setId(id);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() throws SQLException {
        return id;
    }

    public void addSpeciality() throws SQLException {
        if (specialityExists())
            return;
        Connection connection = DatabaseConnection.getInstance().getConnection();
        String query2 = "INSERT INTO speciality (name) VALUES (?)";
        try (PreparedStatement statement2 = connection.prepareStatement(query2)) {
            statement2.setString(1, getName());
            statement2.executeUpdate();
            System.out.println("Speciality added");
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public boolean specialityExists() throws SQLException {
        String query = "SELECT * FROM speciality WHERE name = ?";
        Connection connection = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, getName());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next())
                return true;
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static Speciality getSpecialityByName(String specialityName) throws SQLException {
        String query = "SELECT * FROM speciality WHERE name = ?";
        Connection connection = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, specialityName);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next())
                return new Speciality(resultSet.getString("name"), resultSet.getString("id"));
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static Speciality getSpecialityById(String specialityId) throws SQLException {
        String query = "SELECT * FROM speciality WHERE id = ?";
        Connection connection = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, specialityId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next())
                return new Speciality(resultSet.getString("name"), resultSet.getString("id"));
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
