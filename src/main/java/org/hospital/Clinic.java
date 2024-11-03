package org.hospital;

import org.example.DatabaseConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class  Clinic {
    private String doctorName;
    private String clinicId;

    public Clinic(String clinicId, String doctorName) throws SQLException {
        setDoctorName(doctorName);
        setClinicId(clinicId);
    }

    public void setClinicId(String clinicId) {
        this.clinicId = clinicId;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public String getClinicId() {
        return clinicId;
    }

    public static Clinic getClinic(String id) throws SQLException {
        Connection connection = DatabaseConnection.getInstance().getConnection();
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM hospitals WHERE id = '" + id + "'");
        if (rs.next()) {
            return new Clinic(rs.getString("id"), rs.getString("doctor_name"));
        }
        return null;
    }

    @Override
    public String toString() {
        return " doctorName='" + doctorName + '\n' +
                ", speciality=";
    }
}
