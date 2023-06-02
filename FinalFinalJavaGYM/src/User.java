import org.w3c.dom.ls.LSOutput;

import java.util.ArrayList;
import java.io.*;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class User implements Serializable {
    private static String so = System.getProperty("user.name");
    private int id;
    private String name;
    private String password;
    public ArrayList<Lesson> assignedLessons;
    private static int last = 0;
    public User(String name, String password) {
        last++;
        id = last;
        this.name = name;
        this.password = password;
        assignedLessons = new ArrayList<Lesson>();
    }

    public int getId() {
        return id;
    }

    public static void setLast(int last) {
        User.last = last;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    @Override
    public String toString() {
        return "\nName: " + name +"\nID: " + id;
    }

    public static int getLast() {
        return last;
    }

    public boolean equals(Object obj) {
        if (obj != null && this.getClass() == obj.getClass()) {
            User e = (User) obj;
            return (this.id == e.id && this.name.equals(e.name));
        }
        return false;
    }

    public Object clone() {
        User copy = new User(name,password);
        return copy;
    }

    public static void enterID(ArrayList<User> users, ArrayList<Lesson> lessons) {
        int answer = enterIDPrompt();
        if (answer != 0) {
            loginUser(users, answer,lessons);
        }
    }

    public static int enterIDPrompt() {
        // nobody has ID = 0, that's why it's reserved for "Cancel"
        System.out.println("0- Cancel");
        System.out.println("Enter ID: ");
        int answer = Ler.umInt();
        return answer;
    }


    public static void loginUser(ArrayList<User> users, int id, ArrayList<Lesson> lessons) {
        boolean found = false;
        // guess from 0 to 9999 (guess has to be String, so 64 doesn't work, and you have
        // to type 0064 instead)
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId() == id) {
                found = true;
                String password = users.get(i).getPassword();
                //after the user misses the guess three times, it comes back
                guess(3, password, id, users, lessons);
                break;
            }
        }
        if (!found) {
            System.out.println("There is no User with that ID");
        }
    }

    public static void guess(int maxGuesses, String password, int id, ArrayList<User> users, ArrayList<Lesson> lessons) {
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
                outOfGuesses = true;
        }
        if (outOfGuesses) {
            // o guess corresponde a password
            menuUser(users, id,lessons);
        } else {
            System.out.println("Try again");
        }
    }

    public ArrayList<Lesson> getAssignedLessons() {
        return assignedLessons;
    }

    public static void menuUser(ArrayList<User> users, int id, ArrayList<Lesson> lessons) {
        int answer = 0;
        do {
            System.out.println("\n\nYou are on the User's menu");
            System.out.println("0- Logout");
            System.out.println("1- Check information");
            System.out.println("2- Check lessons");
            System.out.println("3- Change PIN");
            System.out.println("4- Sign up for lessons");
            answer = Ler.umInt();
            switch(answer) {
                case 0 : break;
                case 1:
                    for (int i = 0; i < users.size(); i++) {
                        if (users.get(i).getId() == id) {
                            System.out.println(users.get(i));
                            break;
                        }
                    }
                    break;
                case 2:
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
                    for (int i = 0; i< users.size(); i++)
                    {
                        for(int j=0;j<lessons.size(); j++){
                            if (users.get(i).getId() == id && users.get(i).assignedLessons.contains(lessons.get(j))) {
                                System.out.println(lessons.get(j));
                                System.out.println("..........");
                                for(int h=1; h<8; h++) {
                                    if(h>=1 && h<=5) {
                                        if(idsToWeekday.get(h) != null){
                                            for(Integer key: idsToWeekday.get(h).hmMorningLessons.keySet()){
                                                if(idsToWeekday.get(h).hmMorningLessons.get(key).equals(lessons.get(j))){
                                                    System.out.println(Teacher.integerToDay(h)+ ": " + key + "h");
                                                }
                                            }
                                            for(Integer key: idsToWeekday.get(h).hmEveningLessons.keySet()){
                                                if(idsToWeekday.get(h).hmEveningLessons.get(key).equals(lessons.get(j))){
                                                    System.out.println(Teacher.integerToDay(h)+ ": " + key + "h");
                                                }
                                            }
                                        }
                                    } else if(h==6 || h==7) {
                                        if(idsToWeekend.get(h) != null){
                                            for(Integer key: idsToWeekend.get(h).hmLessons.keySet()){
                                                if(idsToWeekend.get(h).hmLessons.get(key).equals(lessons.get(j))){
                                                    System.out.println(Teacher.integerToDay(h)+ ": " + key + "h");
                                                }
                                            }

                                        }
                                    }
                                }
                                System.out.println("--------------------------");
                            }
                        }
                    }
                    break;

                case 3: changePin(users,id);
                    try {
                        ObjectOutputStream osUser = new ObjectOutputStream(new FileOutputStream("C:\\Users\\" + so + "\\Desktop\\User.dat"));
                        osUser.writeInt(User.getLast());
                        osUser.writeObject(users);
                        osUser.flush();
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                break;
                case 4:askSignUpForLesson(lessons,id,users);
                    break;

                default: System.out.println("Insert a valid number");
                    continue;
            }
        } while(answer != 0);
    }

    public static void insert(ArrayList<User> users) {
        System.out.println("What is your name? ");
        String name;
        do {
            name = Ler.umaString();
        } while(name.equals(""));
        String password= Sign_In.insertPassword();
        User l = new User(name,password);
        users.add(l);
        try {
            ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream("C:\\Users\\"+so+"\\Desktop\\User.dat"));
            os.writeInt(User.getLast());
            os.writeObject(users);
            os.flush();
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Your ID is : " + (User.getLast() ));
    }
    public static void changeName(ArrayList<User> users, int id) {
        System.out.println("Insert the new name: ");
        String name = Ler.umaString();
        for(int i=0; i<users.size(); i++) {
            if(users.get(i).getId() == id) {
                users.get(i).setName(name);
            }
        }
    }

    public static void changePin(ArrayList<User> users, int id) {
        for(int i=0; i<users.size(); i++) {
            if(users.get(i).getId() == id) {
                users.get(i).setPassword(insertPassword());

            }
        }

    }

    public static String insertPassword(){
        System.out.println("Insert the new PIN:");
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

    public static int signUpLessonPrompt() {
        int weekdayChoosen = -1;
        do {
            System.out.println("When do you want to sign up for a lesson? ");
            System.out.println("0- Cancel");
            System.out.println("1- Monday");
            System.out.println("2- Tuesday");
            System.out.println("3- Wednesday");
            System.out.println("4- Thursday");
            System.out.println("5- Friday");
            System.out.println("6- Saturday");
            System.out.println("7- Sunday");
            weekdayChoosen = Ler.umInt();
            if (weekdayChoosen < 0 || weekdayChoosen > 7)
            System.out.println("Invalid input");
        } while ((weekdayChoosen < 1 || weekdayChoosen > 7) && weekdayChoosen != 0);
        return weekdayChoosen;
    }

    public static void askSignUpForLesson(ArrayList<Lesson> lessons, int id, ArrayList<User> users) {
        int weekdayChoosen = signUpLessonPrompt();
        ConcurrentHashMap<Integer, Weekday> idsToWeekday;
        idsToWeekday = new ConcurrentHashMap<>(5);
        try {
            ObjectInputStream isWeekday = new ObjectInputStream(new FileInputStream(
                    "C:\\Users\\" + so + "\\Desktop\\Weekday.dat"));
            idsToWeekday = (ConcurrentHashMap<Integer, Weekday>) isWeekday.readObject();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        ConcurrentHashMap<Integer, Weekend> idsToWeekend;
        idsToWeekend = new ConcurrentHashMap<>(2);
        try {
            ObjectInputStream isWeekend = new ObjectInputStream(new FileInputStream(
                    "C:\\Users\\" + so + "\\Desktop\\Weekend.dat"));
            idsToWeekend = (ConcurrentHashMap<Integer, Weekend>) isWeekend.readObject();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        int answer;
        if (idsToWeekday != null) {
            if ((weekdayChoosen >= 1 && weekdayChoosen <= 5) && !(idsToWeekday.get(weekdayChoosen)==(null))) {
                do{
                    System.out.println("0-Cancel");
                    System.out.println("Take your pick");
                    System.out.println("Morning: " + idsToWeekday.get(weekdayChoosen).hmMorningLessons + "Evening: " + idsToWeekday.get(weekdayChoosen).hmEveningLessons);
                    answer = Ler.umInt();
                    if (answer >= 8 && answer <= 11) {
                        if(idsToWeekday.get(weekdayChoosen).hmMorningLessons.get(answer).getTeacherId()== id)
                        {
                            System.out.println("You can't sign up for your own lesson!");
                            return;
                        }
                        //don't forget to put in files

                        for(Lesson t: lessons){
                            if(t.equals(idsToWeekday.get(weekdayChoosen).hmMorningLessons.get(answer))){
                                idsToWeekday.get(weekdayChoosen).hmMorningLessons.get(answer).addParticipant(getParticipant(users, id));
                                t.addParticipant(getParticipant(users,id));
                                getParticipant(users, id).assignedLessons.add(idsToWeekday.get(weekdayChoosen).hmMorningLessons.get(answer));
                            }
                        }

                        try {
                            ObjectOutputStream osSchedule = new ObjectOutputStream(new FileOutputStream("C:\\Users\\" + so + "\\Desktop\\Weekday.dat"));
                            osSchedule.writeObject(idsToWeekday);
                            osSchedule.flush();
                        } catch (IOException e) {
                            System.out.println(e.getMessage());
                        }
                        try {
                            ObjectOutputStream osUser = new ObjectOutputStream(new FileOutputStream("C:\\Users\\" + so + "\\Desktop\\User.dat"));
                            osUser.writeInt(getLast());
                            osUser.writeObject(users);
                            osUser.flush();
                        } catch (IOException e) {
                            System.out.println(e.getMessage());
                        }
                        try {
                            ObjectOutputStream osLesson = new ObjectOutputStream(new FileOutputStream("C:\\Users\\" + so + "\\Desktop\\Lesson.dat"));
                            osLesson.writeInt(Lesson.getLast());
                            osLesson.writeObject(lessons);
                            osLesson.flush();
                        } catch (IOException e) {
                            System.out.println(e.getMessage());
                        }
                    } else if (answer >= 14 && answer <= 18) {
                        if(idsToWeekday.get(weekdayChoosen).hmEveningLessons.get(answer).getTeacherId()== id)
                        {
                            System.out.println("You can't sign up for your own lesson!");
                            return;
                        }
                        for(Lesson t: lessons){
                            if(t.equals(idsToWeekday.get(weekdayChoosen).hmEveningLessons.get(answer))){
                                idsToWeekday.get(weekdayChoosen).hmEveningLessons.get(answer).addParticipant(getParticipant(users, id));
                                t.addParticipant(getParticipant(users,id));
                                getParticipant(users, id).assignedLessons.add(idsToWeekday.get(weekdayChoosen).hmEveningLessons.get(answer));
                            }
                        }

                        try {
                            ObjectOutputStream osSchedule = new ObjectOutputStream(new FileOutputStream("C:\\Users\\" + so + "\\Desktop\\Weekday.dat"));
                            osSchedule.writeObject(idsToWeekday);
                            osSchedule.flush();
                        } catch (IOException e) {
                            System.out.println(e.getMessage());
                        }
                        try {
                            ObjectOutputStream osUser = new ObjectOutputStream(new FileOutputStream("C:\\Users\\" + so + "\\Desktop\\User.dat"));
                            osUser.writeInt(User.getLast());
                            osUser.writeObject(users);
                            osUser.flush();
                        } catch (IOException e) {
                            System.out.println(e.getMessage());
                        }
                        try {
                            ObjectOutputStream osLesson = new ObjectOutputStream(new FileOutputStream("C:\\Users\\" + so + "\\Desktop\\Lesson.dat"));
                            osLesson.writeInt(Lesson.getLast());
                            osLesson.writeObject(lessons);
                            osLesson.flush();
                        } catch (IOException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                    if(answer!=0 && !(answer>=8 && answer <= 11) && !(answer >=14 && answer<=18))
                        System.out.println("Invalid input");
                }while(answer!=0 && !(answer>=8 && answer <= 11) && !(answer >=14 && answer<=18));
            }
            else if((weekdayChoosen== 6 || weekdayChoosen == 7) && !(idsToWeekday.get(weekdayChoosen)==(null))){
                do{
                    System.out.println("0-Cancel");
                    System.out.println("Take your pick");
                    System.out.println("Day: " + idsToWeekend.get(weekdayChoosen).hmLessons );
                    answer = Ler.umInt();
                    if(idsToWeekend.get(weekdayChoosen).hmLessons.get(answer).getTeacherId()== id)
                    {
                        System.out.println("You can't sign up for your own lesson!");
                        return;
                    }
                    if (answer >= 9 && answer <= 13) {

                        //don't forget to put in files

                        for(Lesson t: lessons){
                            if(t.equals(idsToWeekend.get(weekdayChoosen).hmLessons.get(answer))){
                                idsToWeekend.get(weekdayChoosen).hmLessons.get(answer).addParticipant(getParticipant(users, id));
                                t.addParticipant(getParticipant(users,id));
                                getParticipant(users, id).assignedLessons.add(idsToWeekend.get(weekdayChoosen).hmLessons.get(answer));
                            }
                        }

                        try {
                            ObjectOutputStream osSchedule = new ObjectOutputStream(new FileOutputStream("C:\\Users\\" + so + "\\Desktop\\Weekend.dat"));
                            osSchedule.writeObject(idsToWeekend);
                            osSchedule.flush();
                        } catch (IOException e) {
                            System.out.println(e.getMessage());
                        }
                        try {
                            ObjectOutputStream osUser = new ObjectOutputStream(new FileOutputStream("C:\\Users\\" + so + "\\Desktop\\User.dat"));
                            osUser.writeInt(User.getLast());
                            osUser.writeObject(users);
                            osUser.flush();
                        } catch (IOException e) {
                            System.out.println(e.getMessage());
                        }
                        try {
                            ObjectOutputStream osLesson = new ObjectOutputStream(new FileOutputStream("C:\\Users\\" + so + "\\Desktop\\Lesson.dat"));
                            osLesson.writeInt(Lesson.getLast());
                            osLesson.writeObject(lessons);
                            osLesson.flush();
                        } catch (IOException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                    if(answer!=0 && !(answer>=9 && answer <= 13) )
                        System.out.println("Invalid input");
                }while(answer!=0 && !(answer>=9 && answer <= 13) );

            }
        }
    }

    public static boolean isThereUser(ArrayList<User> users, int id){
        for(User t : users){
            if(id == t.getId()){
                return true;
            }
        }
        return false;
    }
    public static User getParticipant(ArrayList<User> users, int id){
        for(User t : users){
            if(id == t.getId()){
                return t;
            }
        }
        return null;///VERIFY
    }


}
