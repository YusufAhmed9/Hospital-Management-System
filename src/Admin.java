

public class Admin extends User {
    public Admin(User user){
        setUsername(user.getUsername());
        setPassword(user.getPassword());
    }
}