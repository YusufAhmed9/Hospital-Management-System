package org.hospital;

import org.example.DatabaseConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Clinic {
    private String doctorName;
    private String id;
    private String hospitalId;
    private String specialityId;
    private float reservationPrice;

    public Clinic(String doctorName, String specialityId, String id, String hospitalId, float reservationPrice) throws SQLException {
        setDoctorName(doctorName);
        setSpecialityId(specialityId);
        setId(id);
        setHospitalId(hospitalId);
        setReservationPrice(reservationPrice);
    }

    public void setReservationPrice(float reservationPrice) {
        Scanner scanner = new Scanner(System.in);
        while (!isValidReservationPrice(reservationPrice)) {
            System.out.print("Enter a valid price: ");
            reservationPrice = scanner.nextFloat();
        }
        this.reservationPrice = reservationPrice;
    }

    public void setDoctorName(String doctorName) {
        Scanner scanner = new Scanner(System.in);
        while (!isValidDoctorName(doctorName)) {
            System.out.print("Enter a valid name: ");
            doctorName = scanner.nextLine();
        }
        this.doctorName = doctorName;
    }

    public void setSpecialityId(String specialityId) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        while (!isValidSpeciality(specialityId)) {
            System.out.print("Speciality doesn't exist, would you like to add a speciality ? (y/n): ");
            String choice = scanner.nextLine().toLowerCase();
            if (choice.equals("n"))
                return;
            System.out.print("Enter speciality name:");
            String specialityName = scanner.nextLine();
            Speciality speciality = new Speciality(specialityName);
            speciality.addSpeciality();
        }
        this.specialityId = specialityId;
    }

    public void setHospitalId(String hospitalId) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        while (!isValidHospitalId(hospitalId)) {
            System.out.print("Hospital doesn't exist, would you like to add a hospital ? (y/n): ");
            String choice = scanner.nextLine().toLowerCase();
            if (choice.equals("n"))
                return;
            System.out.print("Enter hospital name:");
            String hospitalName = scanner.nextLine();
            Hospital hospital = new Hospital(hospitalName);
            hospital.addHospital();
            this.hospitalId = hospital.getId();
        }
        this.hospitalId = hospitalId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public String getSpecialityId() {
        return specialityId;
    }

    public String getHospitalId() {
        return hospitalId;
    }

    public String getId() {
        return id;
    }

    public float getReservationPrice() {
        return reservationPrice;
    }

    public static Clinic getClinic(String id) throws SQLException {
        Connection connection = DatabaseConnection.getInstance().getConnection();
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM hospitals WHERE id = '" + id + "'");
        if (rs.next()) {
            return new Clinic(rs.getString("doctor_name"), rs.getString("speciality_id"), rs.getString("id"), rs.getString("hospital_id"), rs.getFloat("reservation_price"));
        }
        return null;
    }

    private boolean isValidDoctorName(String doctorName) {
        return !doctorName.isEmpty();
    }

    private boolean isValidHospitalId(String hospitalId) throws SQLException {
        return Hospital.getHospitalByName(hospitalId) != null;
    }

    private boolean isValidSpeciality(String specialityId) throws SQLException {
        return Speciality.getSpecialityById(specialityId) != null;
    }

    private boolean isValidReservationPrice(float reservationPrice) {
        return reservationPrice >= 0;
    }
}
