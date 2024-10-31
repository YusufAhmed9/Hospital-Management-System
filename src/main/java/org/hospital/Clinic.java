package org.hospital;

import java.sql.Connection;
import java.sql.SQLException;

public class  Clinic {
    private String doctorName;
    private int clinicId;

    public Clinic(String doctorName, Connection conn) throws SQLException {
        setDoctorName(doctorName);
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDoctorName() {
        return doctorName;
    }


    @Override
    public String toString() {
        return " doctorName='" + doctorName + '\n' +
                ", speciality=";
    }
}
