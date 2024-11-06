package org.hospital;

import org.example.DatabaseConnection;

import java.sql.*;
import java.time.LocalTime;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

public class ClinicAvailability {
    private String id;
    private String clinicId;
    private String day;
    private LocalTime startTime;
    private LocalTime endTime;

    public ClinicAvailability(String clinicId, String day, LocalTime startTime, LocalTime endTime) {
        setClinicId(clinicId);
        setDay(day);
        setStartTime(startTime);
        setEndTime(endTime);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClinicId() {
        return clinicId;
    }

    public void setClinicId(String clinicId) {
        this.clinicId = clinicId;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public void addAvailability() throws SQLException {
        Connection connection = DatabaseConnection.getInstance().getConnection();
        String query = "INSERT INTO clinic_availability(clinic_id, day, start_time, end_time) VALUES(?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, getClinicId());
        preparedStatement.setString(2, getDay());
        preparedStatement.setTime(3, Time.valueOf(getStartTime()));
        preparedStatement.setTime(4, Time.valueOf(getEndTime()));
    }

    public static ArrayList<ClinicAvailability> getClinicAvailabilities(String clinicId) throws SQLException {
        ArrayList<ClinicAvailability> clinicAvailabilities = new ArrayList<ClinicAvailability>();
        Connection connection = DatabaseConnection.getInstance().getConnection();
        String query = "SELECT * FROM clinic_availability WHERE clinic_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, clinicId);
        preparedStatement.setBoolean(2, true);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            ClinicAvailability clinicAvailability = new ClinicAvailability(
                    resultSet.getString("clinic_id"),
                    resultSet.getString("day"),
                    resultSet.getTime("start_time").toLocalTime(),
                    resultSet.getTime("end_time").toLocalTime()
            );
            clinicAvailabilities.add(clinicAvailability);
        }
        return clinicAvailabilities;
    }

    public static Map.Entry<DayOfWeek, LocalTime> availabilityMenu(String clinicId) throws SQLException {
        ArrayList<ClinicAvailability> clinicAvailabilities = ClinicAvailability.getClinicAvailabilities(clinicId);
        Scanner scanner = new Scanner(System.in);
        if (clinicAvailabilities.isEmpty()) {
            System.out.println("This Clinic Is Not Available");
        }
        System.out.println("Available Times For This Clinic");
        int optionsCount = 1;
        for (ClinicAvailability availability : clinicAvailabilities) {
            System.out.println("Day: " + availability.getDay());
            LocalTime currentTime = availability.getStartTime();
            while (!currentTime.isAfter(availability.getEndTime())) {
                System.out.println("[" + optionsCount + "] " + currentTime + " - " + currentTime.plusMinutes(30));
                currentTime = currentTime.plusMinutes(30);
                optionsCount++;
            }
        }
        System.out.print("Option: ");
        int option = scanner.nextInt();
        if (option < 1 || option >= optionsCount) {
            System.out.println("Invalid Option.");
            return null;
        }
        int selectedOption = 1;
        DayOfWeek selectedDay = null;
        LocalTime reservationTime = null;
        outerLoop:
        for (ClinicAvailability availability : clinicAvailabilities) {
            LocalTime currentSlot = availability.getStartTime();
            selectedDay = DayOfWeek.valueOf(availability.getDay().toUpperCase());
            while (!currentSlot.isAfter(availability.getEndTime())) {
                if (selectedOption == option) {
                    reservationTime = currentSlot;
                    break outerLoop;
                }
                currentSlot = currentSlot.plusMinutes(30);
                selectedOption++;
            }
        }
        return new AbstractMap.SimpleEntry<>(selectedDay, reservationTime);
    }
}
