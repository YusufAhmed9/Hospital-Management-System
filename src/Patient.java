
import java.util.Scanner;

public class Patient extends User{

    public Patient(String username, String password){
        setUsername(username);
        setPassword(password);
    }
    public Patient(User user){
        setUsername(user.getUsername());
        setPassword(user.getPassword());
    }

    private void editUserInfo(){
        Scanner scanner = new Scanner(System.in);
        int option;
        System.out.println("Username: " + getUsername());
        System.out.println("Password: " + getPassword() + "\n");
        System.out.println("Which one would you like to edit ?");
        while (true) {
            System.out.println("1. Username");
            System.out.println("2. Password");
            System.out.print("Choose an option: ");
            option = scanner.nextInt();

            if(option != 1 && option != 2){
                System.out.println("Error: Please enter a valid option\n");
                continue;
            }
            break;
        }

        //needs file overwrite "Will learn"
        if(option == 1){
            //Edits username
        }
        else{
            //Edits Password
        }
    }

    public void Interface(){
        Scanner scanner = new Scanner(System.in);
        int option;
        System.out.println("Welcome" + getUsername() + "\n");
        System.out.println("What would you like to do ?\n");

        while (true){
            System.out.println("1. Edit your information");
            System.out.println("2. View hospitals");
            System.out.println("3. Log out");
            System.out.print("Choose an option: ");
            option = scanner.nextInt();

            if(option > 3 || option < 1){
                System.out.println("Error: Please enter a valid option\n");
                continue;
            }

            break;
        }

        if(option == 1){
            editUserInfo();
        }
    }
}
