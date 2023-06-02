import java.util.*;
import java.io.*;

public class Sign_In {
    public static void menu(ArrayList<User> users, ArrayList<Teacher> teachers) {
        int answer = -1;
        do {
            answer = signInMenuPrompt();
            if(answer == 1) {
                User.insert(users);
            } else if(answer == 2) {
                Teacher.insert(users, teachers);
            } else if(answer != 0) {
                System.out.println("Invalid input, please try again.");

            }
        } while( answer != 0);
    }

    public static int signInMenuPrompt() {
        System.out.println("\n\nYou are on the sign-in menu");
        System.out.println("0- Cancel");
        System.out.println("1- User");
        System.out.println("2- Teacher");
        int answer = Ler.umInt();
        return answer;
    }






    public static String insertPassword(){
        System.out.println("Create a PIN of 4 digits:");
        String password = "";
        String password2 = "";
        boolean check = false;
        do {
            password = Ler.umaString();
            if(!checkPin(password)){
                continue;
            }
            System.out.println("Please confirm the PIN");
            password2 = Ler.umaString();
            if(password.equals(password)){
                break;
            }
            else {
                System.out.println("The two PINs don't match please try again!");
            }
        } while (!check);
        if(password.equals(password2) && checkPin(password) && checkPin(password2)){
            return password;
        }

        return insertPassword();

    }

    public static boolean checkPin(String pin) {
        boolean allDigits = false;
        if (pin.length() != 4) {
            System.out.println("You have not entered a 4-digit pin");
        } else {
            allDigits = true;
            for (int i = 0; i < 4; i++) {
                if (!Character.isDigit(pin.charAt(i))) {
                    allDigits = false;
                    break; // break gets you out of the for cycle
                }
            }
        }
        if (allDigits) {
            return true;
        } else {
            System.out.println("Enter a number");
            return false;
        }
    }
}
