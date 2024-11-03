package org.example;

import org.usermanagement.Admin;
import org.usermanagement.Patient;
import org.usermanagement.User;

import java.util.ArrayList;
import java.sql.*;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
//        User user = new User("1", "test_user", "aa");
        Patient patient = new Patient("1", "test_user", "aa");
        try {
            patient.Interface();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}