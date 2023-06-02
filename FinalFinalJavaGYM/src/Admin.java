import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

public class Admin {
    private static String so = System.getProperty("user.name");
    private final static String name = "Admin";
    private final static String password = "0000";


    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public static void deleteUser(ArrayList<User> users, ArrayList<Lesson> lessons,ArrayList<Teacher> teachers) {
        ConcurrentHashMap<Integer,Weekday> idsToWeekday;
        idsToWeekday= new ConcurrentHashMap<>(5);
        try {
            ObjectInputStream isWeekday = new ObjectInputStream(new FileInputStream(
                    "C:\\Users\\"+so+"\\Desktop\\Weekday.dat"));
            idsToWeekday = (ConcurrentHashMap<Integer, Weekday>) isWeekday.readObject();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        ConcurrentHashMap<Integer,Weekend> idsToWeekend;
        idsToWeekend= new ConcurrentHashMap<>(2);
        try {
            ObjectInputStream isWeekend = new ObjectInputStream(new FileInputStream(
                    "C:\\Users\\"+so+"\\Desktop\\Weekend.dat"));
            idsToWeekend = (ConcurrentHashMap<Integer, Weekend>) isWeekend.readObject();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }

        showInfoAllUsers(users);
        System.out.println("\n0- Cancel");
        System.out.println("Insert the ID of the Account to delete");
        int id = Ler.umInt();
        if(id==0)
            return;
        boolean found = false;
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId() == id) {
                found = true;
                if(users.get(i) instanceof Teacher ){
                    deleteTeacher(id,teachers,lessons,users);
                    try {
                        ObjectInputStream isWeekday = new ObjectInputStream(new FileInputStream(
                                "C:\\Users\\"+so+"\\Desktop\\Weekday.dat"));
                        idsToWeekday = (ConcurrentHashMap<Integer, Weekday>) isWeekday.readObject();
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    } catch (ClassNotFoundException e) {
                        System.out.println(e.getMessage());
                    }
                    try {
                        ObjectInputStream isWeekend = new ObjectInputStream(new FileInputStream(
                                "C:\\Users\\"+so+"\\Desktop\\Weekend.dat"));
                        idsToWeekend = (ConcurrentHashMap<Integer, Weekend>) isWeekend.readObject();
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    } catch (ClassNotFoundException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                }

                for(int j=0;j<lessons.size(); j++){
                    if (users.get(i).getAssignedLessons().contains(lessons.get(j))) {
                        for(int h=1; h<8; h++) {
                            if(h>=1 && h<=5) {
                                if(idsToWeekday.get(h) != null){
                                    for (Lesson t : idsToWeekday.get(h).hmMorningLessons.values())
                                    {
                                        if(lessons.get(j).equals(t)){
                                            t.setParticipants((t.getParticipants()-1));
                                            t.usersInLesson.remove(users.get(i));
                                        }
                                    }
                                    for (Lesson t : idsToWeekday.get(h).hmEveningLessons.values())
                                    {
                                        if(lessons.get(j).equals(t)){
                                            t.setParticipants((t.getParticipants()-1));
                                            t.usersInLesson.remove(users.get(i));
                                        }
                                    }
                                }
                            } else if(h==6 || h==7) {
                                if(idsToWeekend.get(h) != null){
                                    for(Lesson t : idsToWeekend.get(h).hmLessons.values()){
                                        if(lessons.get(j).equals(t)){
                                            t.setParticipants((t.getParticipants()-1));
                                            t.usersInLesson.remove(users.get(i));
                                        }
                                    }
                                }
                            }
                        }
                        lessons.get(j).setParticipants(lessons.get(j).getParticipants() -1);
                        lessons.get(j).usersInLesson.remove(users.get(i));
                    }
                }
                users.remove(users.get(i));
                break;
            }
        }
        if(!found){
            System.out.println("There is no User with that ID");
        }

        try {
            ObjectOutputStream osUsers = new ObjectOutputStream(
                    new FileOutputStream("C:\\Users\\"+so+"\\Desktop\\User.dat"));
            osUsers.writeInt(User.getLast());
            osUsers.writeObject(users);
            osUsers.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        try {
            ObjectOutputStream osLesson = new ObjectOutputStream(
                    new FileOutputStream("C:\\Users\\"+so+"\\Desktop\\Lesson.dat"));
            osLesson.writeInt(Lesson.getLast());
            osLesson.writeObject(lessons);
            osLesson.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        try {
                ObjectOutputStream osSchedule = new ObjectOutputStream(new FileOutputStream("C:\\Users\\"+so+"\\Desktop\\Weekday.dat"));
                osSchedule.writeObject(idsToWeekday);
                osSchedule.flush();
        } catch(IOException e) {
                System.out.println(e.getMessage());
        }

        try {
            ObjectOutputStream osSchedule = new ObjectOutputStream(new FileOutputStream("C:\\Users\\"+so+"\\Desktop\\Weekend.dat"));
            osSchedule.writeObject(idsToWeekend);
            osSchedule.flush();
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }
        try {
            ObjectOutputStream osTeachers = new ObjectOutputStream(
                    new FileOutputStream("C:\\Users\\"+so+"\\Desktop\\Teacher.dat"));
            osTeachers.writeInt(User.getLast());
            osTeachers.writeObject(teachers);
            osTeachers.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }


    public static void deleteTeacher(int id, ArrayList<Teacher> teachers, ArrayList<Lesson> lessons, ArrayList<User> users){
        ConcurrentHashMap<Integer,Weekday> idsToWeekday;
        idsToWeekday= new ConcurrentHashMap<>(5);
        try {
            ObjectInputStream isWeekday = new ObjectInputStream(new FileInputStream(
                    "C:\\Users\\"+so+"\\Desktop\\Weekday.dat"));
            idsToWeekday = (ConcurrentHashMap<Integer, Weekday>) isWeekday.readObject();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        ConcurrentHashMap<Integer,Weekend> idsToWeekend;
        idsToWeekend= new ConcurrentHashMap<>(2);
        try {
            ObjectInputStream isWeekend = new ObjectInputStream(new FileInputStream(
                    "C:\\Users\\"+so+"\\Desktop\\Weekend.dat"));
            idsToWeekend = (ConcurrentHashMap<Integer, Weekend>) isWeekend.readObject();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        for(Teacher t : teachers){
            if(t.getID() == id){
                for(Iterator<Lesson> itr = lessons.iterator(); itr.hasNext();){//usar Iterator aqui
                    Lesson c =itr.next();
                    if(c.getTeacherId() == id){
                        for(int h=1; h<8; h++) {
                            if(h>=1 && h<=5) {
                                if(idsToWeekday.get(h) != null){
                                    for (Iterator <Integer> itra = idsToWeekday.get(h).hmMorningLessons.keySet().iterator(); itra.hasNext();)
                                    {
                                        Integer key = itra.next();
                                        if(c.equals(idsToWeekday.get(h).hmMorningLessons.get(key))){
                                            idsToWeekday.get(h).morning.put(key,false);
                                            idsToWeekday.get(h).hmMorningLessons.remove(key,idsToWeekday.get(h).hmMorningLessons.get(key));
                                        }
                                    }
                                    for (Iterator <Integer> itra = idsToWeekday.get(h).hmEveningLessons.keySet().iterator(); itra.hasNext();)
                                    {
                                        Integer key = itra.next();
                                        if(c.equals(idsToWeekday.get(h).hmEveningLessons.get(key))){
                                            idsToWeekday.get(h).evening.put(key,false);
                                            idsToWeekday.get(h).hmEveningLessons.remove(key,idsToWeekday.get(h).hmEveningLessons.get(key));
                                        }
                                    }
                                }
                            } else if(h==6 || h==7) {
                                if(idsToWeekend.get(h) != null){
                                    for(Iterator <Integer> itra = idsToWeekend.get(h).hmLessons.keySet().iterator(); itra.hasNext();){
                                        Integer key = itra.next();
                                        if(c.equals(idsToWeekend.get(h).hmLessons.get(key))){
                                            idsToWeekend.get(h).timeTable.put(key,false);
                                            idsToWeekend.get(h).hmLessons.remove(key,idsToWeekend.get(h).hmLessons.get(key));
                                        }
                                    }
                                }
                            }
                        }
                        itr.remove();
                    }
                }
                teachers.remove(t);
                for(int j=0;j<lessons.size(); j++){
                    if (t.getAssignedLessons().contains(lessons.get(j))) {
                        for(int h=1; h<8; h++) {
                            if(h>=1 && h<=5) {
                                if(idsToWeekday.get(h) != null){
                                    for (Lesson l : idsToWeekday.get(h).hmMorningLessons.values())
                                    {
                                        if(lessons.get(j).equals(l)){
                                            l.setParticipants((l.getParticipants()-1));
                                            l.usersInLesson.remove(t);
                                        }
                                    }
                                    for (Lesson l : idsToWeekday.get(h).hmEveningLessons.values())
                                    {
                                        if(lessons.get(j).equals(l)){
                                            l.setParticipants((l.getParticipants()-1));
                                            l.usersInLesson.remove(t);
                                        }
                                    }
                                }
                            } else if(h==6 || h==7) {
                                if(idsToWeekend.get(h) != null){
                                    for(Lesson l : idsToWeekend.get(h).hmLessons.values()){
                                        if(lessons.get(j).equals(l)){
                                            l.setParticipants((l.getParticipants()-1));
                                            l.usersInLesson.remove(t);
                                        }
                                    }
                                }
                            }
                        }
                        lessons.get(j).setParticipants(lessons.get(j).getParticipants() -1);
                        lessons.get(j).usersInLesson.remove(t);
                    }
                }
                users.remove(t);
                break;
            }
        }
        try {
            ObjectOutputStream osUsers = new ObjectOutputStream(
                    new FileOutputStream("C:\\Users\\"+so+"\\Desktop\\User.dat"));
            osUsers.writeInt(User.getLast());
            osUsers.writeObject(users);
            osUsers.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        try {
            ObjectOutputStream osTeachers = new ObjectOutputStream(
                    new FileOutputStream("C:\\Users\\"+so+"\\Desktop\\Teacher.dat"));
            osTeachers.writeInt(User.getLast());
            osTeachers.writeObject(teachers);
            osTeachers.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        try {
            ObjectOutputStream osLesson = new ObjectOutputStream(
                    new FileOutputStream("C:\\Users\\"+so+"\\Desktop\\Lesson.dat"));
            osLesson.writeInt(Lesson.getLast());
            osLesson.writeObject(lessons);
            osLesson.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        try {
            ObjectOutputStream osSchedule = new ObjectOutputStream(new FileOutputStream("C:\\Users\\"+so+"\\Desktop\\Weekday.dat"));
            osSchedule.writeObject(idsToWeekday);
            osSchedule.flush();
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }

        try {
            ObjectOutputStream osSchedule = new ObjectOutputStream(new FileOutputStream("C:\\Users\\"+so+"\\Desktop\\Weekend.dat"));
            osSchedule.writeObject(idsToWeekend);
            osSchedule.flush();
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void authentication(ArrayList<User> users, ArrayList<Teacher> teachers, ArrayList<Lesson> lessons){
        if(guess(3,password)){
            menu(users,teachers,lessons);
        }
        else {
            System.out.println("\nYou have wasted your guesses!");
        }
    }

    public static boolean guess(int maxGuesses, String password){
        String guess;
        int guessCount = 0;
        boolean outOfGuesses = false;
        /*
         * "guess == password" doesn't work because it only works for numbers.
         * For Strings, you must use "guess.equals(password)"
         */
        while (!outOfGuesses && guessCount < maxGuesses) {
            System.out.print("Enter password: ");
            guess = Ler.umaString();
            guessCount++;
            if (guess.equals(password))
                return true;
        }
        return false;

    }

    public static void menu(ArrayList<User> users, ArrayList<Teacher> teachers, ArrayList<Lesson> lessons) {
        int answer;
        do {
            answer = menuPrompt();
            if(answer == 1) {
                int userAnswer ;
                do {
                    userAnswer = usersPrompt();
                    if(userAnswer == 1) {
                        showInfoAllUsers(users);
                    } else if(userAnswer == 2) {
                        searchUserByID(users);
                    } else if(userAnswer == 3) {
                        searchUserByName(users);
                    } else if(userAnswer==0){
                        break;
                    }

                } while(userAnswer!=0);
            } else if(answer == 2) {
                int teacherAnswer ;
                do {
                    teacherAnswer = teachersPrompt();
                    if(teacherAnswer == 1) {
                        showInfoAllTeachers(teachers);
                    } else if(teacherAnswer == 2) {
                        searchTeacherByID(teachers);
                    } else if(teacherAnswer == 3) {
                        searchTeacherByName(teachers);
                    } else if(teacherAnswer == 0){
                        break;
                    }
                } while(teacherAnswer != 0);
            } else if(answer == 3) {
                deleteUser(users,lessons,teachers);
            }
        } while(answer !=0);
    }

    public static int menuPrompt(){
        System.out.println("0- Cancel");
        System.out.println("1- Check users");
        System.out.println("2- Check teachers");
        System.out.println("3- Delete account");
        int answer = Ler.umInt();
        return answer;
    }

    public static int usersPrompt() {
        System.out.println("0- Cancel");
        System.out.println("1- Show information about all the users");
        System.out.println("2- Search User by ID");
        System.out.println("3- Search User by Name");
        int answer = Ler.umInt();
        return answer;
    }

    public static void showInfoAllUsers(ArrayList<User> users) {
        for(User t : users) {
            System.out.println(t);
            System.out.println("-----------------");
        }
    }

    public static void searchUserByID(ArrayList<User> users) {
        System.out.println("Insert the ID of the User you want to search: ");
        int id = Ler.umInt();
        boolean found =false;
        for(int i=0; i<users.size(); i++) {
            if(users.get(i).getId() == id && users.get(i) != null){
                found = true;
                System.out.println(users.get(i));
                System.out.println("-----------------");
            }
        }
        if(!found){
            System.out.println("You inserted an ID that doesn't exist");
        }
    }


    public static void searchUserByName(ArrayList<User> users) {
        System.out.println("What is the name of the User you want to search: ");
        String name = Ler.umaString();
        for(int i=0; i<users.size(); i++) {
            if(users.get(i).getName().equals(name)) {
                System.out.println(users.get(i));
                System.out.println("-----------------");
            }
        }
    }

    public static int teachersPrompt() {
        System.out.println("0- Cancel");
        System.out.println("1- Show information about all the Teachers");
        System.out.println("2- Search Teacher by ID");
        System.out.println("3- Search Teacher by Name");
        int answer = Ler.umInt();
        return answer;
    }

    public static void showInfoAllTeachers(ArrayList<Teacher> teachers) {
        for(int i=0; i<teachers.size(); i++) {
            System.out.println(teachers.get(i));
            System.out.println("-----------------");
        }
    }

    public static void searchTeacherByID(ArrayList<Teacher> teachers) {
        System.out.println("Insert the ID of the Teacher you want to search: ");
        int id = Ler.umInt();
        boolean found =false;
        for(int i=0; i<teachers.size(); i++) {
            if(teachers.get(i).getId() == id && teachers.get(i) != null){
                found = true;
                System.out.println(teachers.get(i));
                System.out.println("-----------------");
            }
        }
        if(!found){
            System.out.println("You inserted an ID that doesn't exist");
        }
    }

    public static void searchTeacherByName(ArrayList<Teacher> teachers) {
        System.out.println("What is the name of the Teacher you want to search: ");
        String name = Ler.umaString();
        for(int i=0; i<teachers.size(); i++) {
            if(teachers.get(i).getName().equals(name)) {
                System.out.println(teachers.get(i));
                System.out.println("-----------------");
            }
        }
    }



}
