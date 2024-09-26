package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Tester {
    private static Connection conn = null;
    public static void testSetup(){
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:hospital.db");
            Patient p = new Patient("Y", "A");
            p.Interface(conn);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        finally {
            testCleanup();
        }
    }

    private static void testCleanup(){
        if (conn != null) {
            try {
                conn.close();
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
