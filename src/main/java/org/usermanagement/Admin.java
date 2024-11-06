package org.usermanagement;

import org.hospital.Hospital;

import java.text.DecimalFormat;
import java.util.Scanner;
import java.sql.*;

public class Admin extends User {

    public Admin(User user){
        setUsername(user.getUsername());
        setPassword(user.getPassword());
    }

    private static void addHospital() throws SQLException {
        String hospitalName;
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Enter Hospital Name: ");
            hospitalName = scanner.nextLine();
            if (Hospital.getHospitalByName(hospitalName) == null) {
                System.out.println("Hospital Added.");
                break;
            }
            System.out.println("A Hospital With That Name Already Exists.");
        }
        Hospital hospital = new Hospital(hospitalName);
        hospital.addHospital();
    }

    private void viewClinics(Hospital hospital) throws SQLException {
        hospital.displayClinics();

    }

    private void viewHospitals() throws SQLException {
        int option;
        String choice;
        Hospital.displayHospitals();
        if (Hospital.count() == 0) {
            return;
        }
        Scanner scanner = new Scanner(System.in);
        System.out.print("Would you like to select or add a hospital ?(y/a/n): ");
        choice = scanner.nextLine().toLowerCase();
        if(choice.equals("n"))
            return;
        if (choice.equals("a")) {
            addHospital();
            return;
        }
        Hospital hospital;
        String hospitalName;
        while(true) {
            System.out.print("Enter hospital's name: ");
            hospitalName = scanner.nextLine();
            System.out.println(hospitalName);
            if(Hospital.getHospitalByName(hospitalName) == null) {
                System.out.println("Hospital not found\n");
                continue;
            }
            hospital = Hospital.getHospitalByName(hospitalName);
            break;
        }
        System.out.println("[ 1 ]: Delete Hospital");
        System.out.println("[ 2 ]: Edit Hospital Info");
        System.out.println("[ 3 ]: Display Clinics");
        System.out.println("[ 4 ]: Exit");
        System.out.print("Option: ");
        option = scanner.nextInt();
        outerLoop:
        while (true) {
            switch (option) {
                case 1:
                    hospital.delete();
                    System.out.println("Hospital Deleted.");
                    break outerLoop;
                case 2:
                    editHospitalInfo(hospital);
                    break outerLoop;
                case 3:
                    viewClinics(hospital);
                    break outerLoop;
                case 4:
                    break outerLoop;
                default:
                    System.out.println("Invalid Option.");
            }
        }
    }

    private static void editHospitalInfo(Hospital hospital) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        int option;
        System.out.println("[ 1 ]: Edit Name");
        System.out.println("[ 2 ]: Exit");
        System.out.print("Option: ");
        option = scanner.nextInt();
        outerLoop:
        while (true) {
            switch (option) {
                case 1:
                    String hospitalName;
                    while (true) {
                        System.out.print("Enter Hospital New Name: ");
                        hospitalName = scanner.nextLine();
                        if (Hospital.getHospitalByName(hospitalName) == null) {
                            break;
                        }
                        System.out.println("A Hospital With That Name Already Exists.");
                    }
                    hospital.setName(hospitalName);
                case 2:
                    break outerLoop;
                default:
                    System.out.println("Invalid Option.");
            }
        }
    }

    public void adminMenu () throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Admin Dashboard");
        System.out.println("[ 1 ]: Display All Hospitals");
        System.out.println("[ 2 ]: Display All Clinics");
        System.out.println("[ 3 ]: Exit");
        outerLoop:
        while (true) {
            System.out.print("Option: ");
            int option = scanner.nextInt();
            scanner.nextLine();
            switch (option) {
                case 1:
                    viewHospitals();
                    break outerLoop;
                case 2:
//                    Clinic.displayClinics();
                    break outerLoop;
                case 3:
                    break outerLoop;
                default:
                    System.out.println("Invalid Option.");
            }
        }
    }
}