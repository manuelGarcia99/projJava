import java.io.*;
import java.util.ArrayList;

public class Login {
    public static void menu(ArrayList<User> users, ArrayList<Teacher> teachers, ArrayList<Lesson> lessons) {
        int answer = -1;
        do {
            answer = loginMenuPrompt();
            if(answer ==1) {
                User.enterID(users, lessons);
            } else if(answer == 2) {
                Teacher.enterIDT(teachers,lessons );
            } else if(answer == 3){
                Admin.authentication(users, teachers,lessons);
            } else if(answer!=0) {
                System.out.println("Invalid input, please try again.");
                menu(users, teachers, lessons);
            }
        } while(answer!=0);
    }

    public static int loginMenuPrompt() {
        System.out.println("\n\nYou are on the login menu");
        System.out.println("0- Cancel");
        System.out.println("1- User");
        System.out.println("2- Teacher");
        System.out.println("3- Admin");
        int answer = Ler.umInt();
        return answer;
    }


}

