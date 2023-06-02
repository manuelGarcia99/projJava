import java.io.*;
import java.util.ArrayList;


public class Main {
    private static String so = System.getProperty("user.name");

    public static void main(String[] args) {



        ArrayList<User> users = new ArrayList<>();
        try {
            ObjectInputStream isUser = new ObjectInputStream(new FileInputStream(
                    "C:\\Users\\"+ so +"\\Desktop\\User.dat"));
            int last = isUser.readInt();
            User.setLast(last);
            users = (ArrayList<User>) isUser.readObject();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }

        ArrayList<Teacher> teachers = new ArrayList<Teacher>();
        try {
            ObjectInputStream isTeacher = new ObjectInputStream(new FileInputStream(
                    "C:\\Users\\"+ so +"\\Desktop\\Teacher.dat"));
            int last = isTeacher.readInt();
            User.setLast(last);
            teachers = (ArrayList<Teacher>) isTeacher.readObject();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }

        ArrayList<Lesson> lessons = new ArrayList<Lesson>();
        try {
            ObjectInputStream isLesson = new ObjectInputStream(new FileInputStream(
                    "C:\\Users\\"+ so +"\\Desktop\\Lesson.dat"));
            int last = isLesson.readInt();
            Lesson.setLast(last);
            lessons = (ArrayList<Lesson>) isLesson.readObject();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }

        menu(users, teachers,lessons);
    }

    public static void menu(ArrayList<User> users, ArrayList<Teacher> teachers, ArrayList<Lesson> lessons) {
        int answer = -1;

        do {
            answer = menuPrompt();
            if (answer == 1) {
                Login.menu(users, teachers, lessons);
            } else if (answer == 2) {
                Sign_In.menu(users, teachers);
            }
            else if(answer != 0){
                System.out.println("Invalid input, please try again.");
                menu(users, teachers, lessons);
            }
        } while (answer != 0);
        System.out.println("Leaving program...");
    }

    public static int menuPrompt() {
        int answer;
        System.out.println("\n\nYou are on the Main Menu:");
        System.out.println("0- Leave");
        System.out.println("1- Login");
        System.out.println("2- Sign in");
        answer = Ler.umInt();
        return answer;
    }







}
