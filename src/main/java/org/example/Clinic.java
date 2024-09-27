package org.example;

import java.sql.Connection;
import java.sql.SQLException;

public class  Clinic {
    private String doctorName;
    private Speciality speciality;
    public Clinic(String doctorName, Speciality speciality1, Connection conn) throws SQLException {
        setDoctorName(doctorName);
        speciality = new Speciality(speciality1.getName(), conn);
    }
    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public Speciality getSpeciality(Connection conn) throws SQLException {
        return speciality.getSpeciality(conn);
    }

    @Override
    public String toString() {
        return " doctorName='" + doctorName + '\'' +
                ", speciality=" + speciality.getName();
    }
}
