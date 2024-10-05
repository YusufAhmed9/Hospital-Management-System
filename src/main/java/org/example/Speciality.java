package org.example;

import java.sql.*;
import java.util.ArrayList;

public class Speciality {
    private String name;
    private int id;

    public Speciality(String name, Connection conn) throws SQLException {
        setName(name);
        addSpeciality(name, conn);
    }

    private Speciality(int id, String name){
        setId(id);
        setName(name);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    private void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    private void addSpeciality(String name, Connection conn) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO specialities(name) VALUES(?)");
        preparedStatement.setString(1, name);
        preparedStatement.executeUpdate();
        setId(getSpeciality(conn).getId());
    }

    public Speciality getSpeciality(Connection conn) throws SQLException {
        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM specialities WHERE name = '" + getName() +"'");
        return rs.next() ? new Speciality(rs.getInt("id"), rs.getString("name")) : null;
    }

    public boolean specialityExists(int HospitalId, Connection conn) throws SQLException {
        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM HospitalSpeciality WHERE HospitalId = " + HospitalId + " AND SpecialityId = '" + getId() + "'");
        return rs.next() ? true : false;
    }
}
