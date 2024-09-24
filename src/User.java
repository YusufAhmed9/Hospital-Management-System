public class User {
    private String username;
    private String password;
    protected final String fileName = "Date.csv";

    public User(){
        setUsername("Username");
        setPassword("Password");
    }

    public User(String username, String password){
        setUsername(username);
        setPassword(password);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}
