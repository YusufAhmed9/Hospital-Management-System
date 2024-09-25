
import java.util.ArrayList;
import java.sql.*;

public class Main {
    static User currentUser = null;
    public static void main(String[] args) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:hospital.db");
            ArrayList<User> users = new ArrayList<User>();
            ArrayList<Hospital> hospitals = new ArrayList<Hospital>();
            User.getUsers(conn, users);
            User.loginMenu(conn, currentUser);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        finally {
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
}