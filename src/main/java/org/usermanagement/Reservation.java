package org.usermanagement;

import org.example.DatabaseConnection;
import org.hospital.Clinic;
import org.hospital.ClinicAvailability;
import org.hospital.DayOfWeek;

import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class Reservation {
    private String id;
    private String clinicId;
    private String userId;
    private DayOfWeek reservationDay;
    private LocalTime reservationTime;

    public Reservation (String id, String  clinicId, String  userId, DayOfWeek reservationDay, LocalTime reservationTime) {
        setId(id);
        setClinicId(clinicId);
        setUserId(userId);
        setReservationDay(reservationDay);
        setReservationTime(reservationTime);
    }

    public Reservation (String clinicId, String  userId, DayOfWeek reservationDay, LocalTime reservationTime) {
        setClinicId(clinicId);
        setUserId(userId);
        setReservationDay(reservationDay);
        setReservationTime(reservationTime);
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

    public void setReservationDay(DayOfWeek reservationDay) {
        this.reservationDay = reservationDay;
    }

    public DayOfWeek getReservationDay() {
        return reservationDay;
    }

    public void setReservationTime(LocalTime reservationTime) {
        this.reservationTime = reservationTime;
    }

    public LocalTime getReservationTime() {
        return reservationTime;
    }

    public static Reservation getReservation(int id) throws SQLException {
        Connection connection = DatabaseConnection.getInstance().getConnection();
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM reservation WHERE id = " + id);
        if (rs.next()) {
            return new Reservation(rs.getString("id"), rs.getString("clinic_id"), rs.getString("user_id"), DayOfWeek.valueOf(rs.getString("reservation_day")), rs.getTime("reservation_time").toLocalTime());
        }
        return null;
    }

    public void create() throws SQLException {
        Connection connection = DatabaseConnection.getInstance().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO reservation(user_id, clinic_id, reservation_day, reservation_time) values(?, ?, ?, ?)");
        preparedStatement.setString(1, getUserId());
        preparedStatement.setString(2, getClinicId());
        preparedStatement.setString(3, getReservationDay().name());
        preparedStatement.setTime(4, Time.valueOf(getReservationTime()));
        preparedStatement.executeUpdate();
    }

    public void delete() throws SQLException {
        Connection connection = DatabaseConnection.getInstance().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM reservation WHERE id = ?");
        preparedStatement.setString(1, getId());
        preparedStatement.executeUpdate();
    }

    public static void reservationMenu(String clinicId, String userId) throws SQLException {
        ArrayList<ClinicAvailability> clinicAvailabilities = ClinicAvailability.getClinicAvailabilities(clinicId);
        Scanner scanner = new Scanner(System.in);
        if (clinicAvailabilities.isEmpty()) {
            System.out.println("No Available Times For This Clinic.");
            return;
        }
        System.out.println("Available Times For This Clinic: ");
        int slotNumber = 1;
        for (ClinicAvailability availability : clinicAvailabilities) {
            System.out.println("Day: " + availability.getDay());
            LocalTime currentSlot = availability.getStartTime();
            while (!currentSlot.isAfter(availability.getEndTime())) {
                System.out.println("[" + slotNumber + "] " + currentSlot + " - " + currentSlot.plusMinutes(30));
                currentSlot = currentSlot.plusMinutes(30);
                slotNumber++;
            }
        }
        System.out.println("Select a slot by entering the slot number:");
        int option = scanner.nextInt();
        if (option < 1 || option >= slotNumber) {
            System.out.println("Invalid Option.");
            return;
        }
        int selectedSlot = 1;
        ClinicAvailability selectedAvailability = null;
        LocalTime reservationTime = null;
        outerLoop:
        for (ClinicAvailability availability : clinicAvailabilities) {
            LocalTime currentSlot = availability.getStartTime();

            while (!currentSlot.isAfter(availability.getEndTime())) {
                if (selectedSlot == option) {
                    selectedAvailability = availability;
                    reservationTime = currentSlot;
                    break outerLoop;
                }
                currentSlot = currentSlot.plusMinutes(30);
                selectedSlot++;
            }
        }
    }
}
