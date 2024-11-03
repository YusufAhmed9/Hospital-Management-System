package org.usermanagement;

import org.example.DatabaseConnection;
import org.hospital.Hospital;

import java.util.Scanner;
import java.sql.*;

public class Admin extends User {

    public Admin(User user){
        setUsername(user.getUsername());
        setPassword(user.getPassword());
    }

    private static void addHospital() throws SQLException {
        String name;
        float reservationPrice;
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Hospital Name: ");
            name = scanner.nextLine();
            if (name.isEmpty()) {
                System.out.println("Name Can't Be Empty.");
                continue;
            }
            break;
        }
        while (true) {
            System.out.print("Reservation Price: ");
            reservationPrice = scanner.nextFloat();
            if (reservationPrice <= 0) {
                System.out.println("Price Can't Be Equal To Or Less Than 0.");
                continue;
            }
            break;
        }
        Hospital hospital = new Hospital(name);
        Hospital.createHospital(hospital);
    }

    private static void deleteHospital() throws SQLException {
        Connection connection = DatabaseConnection.getInstance().getConnection();
        Hospital.displayHospitals();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Hospital ID To Delete: ");
            String id = scanner.nextLine();
            Hospital hospital = Hospital.getHospital(id);
            if (hospital != null) {
                hospital.delete();
                break;
            }
            System.out.println("No Hospital With That ID.");
        }
    }

    private static void editHospitalInfo() throws SQLException {
        Hospital.displayHospitals();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Hospital ID To Edit: ");
            String id = scanner.nextLine();
            Hospital hospital = Hospital.getHospital(id);
            if (hospital != null) {
                while (true) {
                    int editOption;
                    System.out.println("[ 1 ]: Edit Name");
                    System.out.println("[ 2 ]: Edit Reservation Price");
                    System.out.print("Option: ");
                    editOption = scanner.nextInt();
                    if (editOption == 1) {
                        scanner.nextLine();
                        String name;
                        while (true) {
                            System.out.print("New Name: ");
                            name = scanner.nextLine();
                            if (!name.isEmpty()) {
                                hospital.setName(name);
                                break;
                            }
                            System.out.println("Name Can't Be Empty.");
                        }
                        break;
                    }
                    else if (editOption == 2) {
                        float reservationPrice;
                        while (true) {
                            System.out.print("New Reservation Price: ");
                            reservationPrice = scanner.nextFloat();
                            if (reservationPrice > 0) {
//                                hospital.setReservationPrice(reservationPrice);
                                break;
                            }
                            System.out.println("Reservation Price Can't Be Equal To Or Less Than 0.");
                        }
                        break;
                    }
                    else {
                        System.out.println("Invalid Option.");
                    }
                }
                hospital.edit(hospital);
                break;
            }
            System.out.println("No Hospital With That ID.");
        }
    }

    public static void adminMenu () throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Admin Dashboard");
        System.out.println("[ 1 ]: Add Hospital");
        System.out.println("[ 2 ]: Delete Hospital");
        System.out.println("[ 3 ]: Edit Hospital Info");
        System.out.println("[ 4 ]: Display All Hospitals");
        System.out.println("[ 5 ]: Exit");
        while (true) {
            System.out.print("Option: ");
            int option = scanner.nextInt();
            scanner.nextLine();
            if (option <= 4 && option >= 1) {
                if (option == 1) {
                    addHospital();
                }
                else if (option == 2) {
                    if (Hospital.count() > 0) {
                        deleteHospital();
                    }
                    else {
                        System.out.println("No Hospitals To Delete.");
                    }
                }
                else if (option == 3) {
                    if (Hospital.count() > 0) {
                      editHospitalInfo();
                    }
                    else {
                        System.out.println("No Hospitals To Edit.");
                    }
                }
                else {
                    Hospital.displayHospitals();
                }
            }
            else if (option == 5) {
                break;
            }
            else {
                System.out.println("Invalid Option.");
            }
        }
    }
}