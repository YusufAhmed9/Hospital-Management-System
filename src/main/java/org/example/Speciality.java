package org.example;

import java.sql.*;
import java.util.ArrayList;

public class Speciality {
    private String name;
    private int id;
    private ArrayList<Clinic> clinics = new ArrayList();

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

    private void addSpeciality(String name, Connection conn) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO specialities(name) VALUES(?)");
        preparedStatement.setString(1, name);
        preparedStatement.executeUpdate();
    }

    public Speciality getSpeciality(Connection conn) throws SQLException {
        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM specialities WHERE name = '" + getName() +"'");
        return rs.next() ? new Speciality(rs.getInt("id"), rs.getString("name")) : null;
    }

    public void addClinic(Clinic clinic) {
        if(clinics.contains(clinic))
            return;
        clinics.add(clinic);
    }

    public void displayClinics() {
        for(Clinic clinic : clinics) {
            System.out.println(clinic.toString());
        }
    }
}
